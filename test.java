    /*
     *
     *  Copyright (c) [2024] [State Bank of India]
     *  All rights reserved.
     *
     *  Author:@V0000001(Shilpa Kothre)
     *  Version:1.0
     *
     */
    package com.epay.transaction.service;
    
    import com.epay.transaction.dao.TokenDao;
    import com.epay.transaction.dto.MerchantDto;
    import com.epay.transaction.dto.OrderDto;
    import com.epay.transaction.dto.TokenDto;
    import com.epay.transaction.exceptions.TransactionException;
    import com.epay.transaction.model.response.TransactionResponse;
    import com.epay.transaction.util.EPayIdentityUtil;
    import com.epay.transaction.util.ErrorConstants;
    import com.epay.transaction.util.enums.TokenStatus;
    import com.epay.transaction.validator.TokenValidator;
    import com.sbi.epay.authentication.model.*;
    import com.sbi.epay.authentication.service.AuthenticationService;
    import com.sbi.epay.authentication.util.enums.TokenType;
    import com.sbi.epay.logging.utility.LoggerFactoryUtility;
    import com.sbi.epay.logging.utility.LoggerUtility;
    import lombok.RequiredArgsConstructor;
    import org.apache.commons.lang3.time.DateUtils;
    import org.springframework.stereotype.Service;
    
    import java.text.MessageFormat;
    import java.util.Arrays;
    import java.util.Date;
    import java.util.List;
    
    @RequiredArgsConstructor
    @Service
    public class TokenService {
    
        private static final LoggerUtility log = LoggerFactoryUtility.getLogger(TokenService.class);
        private final TokenDao tokenDao;
        private final AuthenticationService authService;
        private final TokenValidator tokenValidator;
    
        private static TransactionTokenRequest buildTransactionTokenRequest(OrderDto orderDto, MerchantDto merchantDto) {
            TransactionTokenRequest transactionTokenRequest = new TransactionTokenRequest();
            transactionTokenRequest.setTokenType(TokenType.TRANSACTION);
            transactionTokenRequest.setMid(orderDto.getMId());
            transactionTokenRequest.setSbiOrderReferenceNumber(orderDto.getSbiOrderRefNumber());
            transactionTokenRequest.setCustomerId(orderDto.getCustomerId());
            transactionTokenRequest.setExpirationTime(merchantDto.getTransactionTokenExpiryTime());
            return transactionTokenRequest;
        }
    
        private static PaymentTokenRequest buildPaymentTokenRequest(OrderDto orderDto, MerchantDto merchantDto) {
            PaymentTokenRequest paymentTokenRequest = new PaymentTokenRequest();
            paymentTokenRequest.setTokenType(TokenType.PAYMENT);
            paymentTokenRequest.setMid(orderDto.getMId());
            paymentTokenRequest.setSbiOrderReferenceNumber(orderDto.getSbiOrderRefNumber());
            paymentTokenRequest.setExpirationTime(merchantDto.getTransactionTokenExpiryTime());
            //TODO: We will add from transaction table
            paymentTokenRequest.setAtrnNumber("1011215354111113");
            return paymentTokenRequest;
        }
    
        public TransactionResponse<String> generateToken(String merchantApiKeyId, String merchantApiKeySecret) {
    
            log.debug("Access Token Request for merchantApiKey {0} merchantApiKeySecret {1}", merchantApiKeyId, merchantApiKeySecret);
            //Validate request header params
            tokenValidator.validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
            //Step 1 : Get Merchant Object
            MerchantDto merchantDto = tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);
            //Step 2 : Token Generation
            log.debug(" generating Access token ");
            return generateToken(buildAccessTokenRequest(merchantDto));
        }
    
        public TransactionResponse<String> generateTransactionToken(String orderHash) {
            log.debug(" Transaction Token Request for orderHash {0} ", orderHash);
            //Step 1 : Get Order Object
            OrderDto orderDto = tokenDao.getActiveTransactionByHashValue(orderHash);
            //Step 2 : Get Merchant Object
            log.info(" request for merchant Info ");
            MerchantDto merchantDto = tokenDao.getActiveMerchantByMID(orderDto.getMId());
            //Step 3 : Token Generation
            log.info(" generating transaction token ");
            return generateToken(buildTransactionTokenRequest(orderDto, merchantDto));
        }
    
        public TransactionResponse<String> generatePaymentToken(String orderHash) {
            log.info(" Transaction Token Request for orderHash {0} ", orderHash);
            //Step 1 : Get Order Object
            OrderDto orderDto = tokenDao.getActiveTransactionByHashValue(orderHash);
            //Step 2 : Get Merchant Object
            log.info(" request for merchant Info ");
            MerchantDto merchantDto = tokenDao.getActiveMerchantByMID(orderDto.getMId());
            //Step 3 : Token Generation
            log.info(" generating transaction token ");
            return generateToken(buildPaymentTokenRequest(orderDto, merchantDto));
        }
    
        public TransactionResponse<String> invalidateToken() {
            log.info(" Invalidate Token - Service");
            EPayPrincipal ePayPrincipal = EPayIdentityUtil.getUserPrincipal();
            TokenDto tokenDto = tokenDao.getActiveTokenByMID(ePayPrincipal.getMid(), ePayPrincipal.getToken()).orElseThrow(() -> new TransactionException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Active Token")));
            tokenDto.setTokenValid(false);
            tokenDto.setExpiredAt(System.currentTimeMillis());
            tokenDto.setStatus(TokenStatus.INACTIVE.name());
            tokenDto.setUpdatedAt(System.currentTimeMillis());
            tokenDao.saveToken(tokenDto);
            return TransactionResponse.<String>builder().data(List.of("Token invalidated successfully")).status(1).build();
        }
    
        private AccessTokenRequest buildAccessTokenRequest(MerchantDto merchantDto) {
            log.info(" Building AuthRequest starts ");
            AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
            accessTokenRequest.setMid(merchantDto.getMID());
            accessTokenRequest.setSecretKey(merchantDto.getSecretKey());
            accessTokenRequest.setApiKey(merchantDto.getApiKey());
            accessTokenRequest.setTokenType(TokenType.ACCESS);
            accessTokenRequest.setExpirationTime(merchantDto.getAccessTokenExpiryTime());
            log.info(" Building AuthRequest ends ");
            return accessTokenRequest;
        }
    
        private TransactionResponse<String> generateToken(TokenRequest tokenRequest) {
            //Step 1 : Save Initial Token Request in DB
            log.info(" saving token details initially ");
            TokenDto tokenDto = tokenDao.saveToken(buildTokenDTO(tokenRequest.getMid(), tokenRequest.getTokenType()));
            //Step 2 : Generate Token by Authentication Service
            log.info(" Generating token using Utility ");
            buildTokenDto(tokenRequest, tokenDto);
            //Step 3 : Save Token Request in DB with token value
            log.info(" updating token details after generating token ");
            tokenDao.saveToken(tokenDto);
            log.info(" updated token details after generating token ");
            return TransactionResponse.<String>builder().data(List.of(tokenDto.getGeneratedToken())).build();
        }
    
        private void buildTokenDto(TokenRequest tokenRequest, TokenDto tokenDto) {
            tokenDto.setGeneratedToken(getToken(tokenRequest));
            tokenDto.setTokenValid(Boolean.TRUE);
            tokenDto.setStatus(TokenStatus.ACTIVE.name());
            tokenDto.setExpiredAt(DateUtils.addMinutes(new Date(), tokenRequest.getExpirationTime()).getTime());
        }
    
        private TokenDto buildTokenDTO(String merchantId, TokenType tokenType) {
            return TokenDto.builder().isTokenValid(Boolean.FALSE).status(TokenStatus.NOT_GENERATED.name()).merchantId(merchantId).tokenType(tokenType.name()).createdAt(System.currentTimeMillis()).updatedAt(System.currentTimeMillis()).createdBy(System.getProperty("user.name")).updatedBy(System.getProperty("user.name")).build();
        }
    
    
        private String getToken(TokenRequest tokenRequest) {
            return switch (tokenRequest.getTokenType()) {
                case ACCESS -> authService.generateAccessToken((AccessTokenRequest) tokenRequest);
                case TRANSACTION -> authService.generateTransactionToken((TransactionTokenRequest) tokenRequest);
                case PAYMENT -> authService.generatePaymentToken((PaymentTokenRequest) tokenRequest);
                default ->
                        throw new TransactionException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_MESSAGE, "Token Type", "Valid Token Types are " + Arrays.toString(TokenType.values())));
            };
        }
    
    
    }
