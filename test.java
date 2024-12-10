package com.epay.transaction.service;

import com.epay.transaction.dao.TokenDao;
import com.epay.transaction.dto.MerchantDto;
import com.epay.transaction.dto.OrderDto;
import com.epay.transaction.dto.TokenDto;
import com.epay.transaction.exceptions.ValidationException;
import com.epay.transaction.model.response.TransactionResponse;
import com.epay.transaction.util.enums.TokenStatus;
import com.epay.transaction.validator.TokenValidator;
import com.sbi.epay.authentication.model.AccessTokenRequest;
import com.sbi.epay.authentication.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenValidator validator;

    @Mock
    private TokenDao tokenDao;

    @Mock
    private AuthenticationService authenticationService;


    private static final String MERCHANT_API_KEY_ID = "testKeyId";
    private static final String MERCHANT_API_KEY_SECRET = "testKeySecret";
    private static final String ORDER_HASH_CODE = "orderHash_code";


    @Test
    void testGenerateTokenSuccess() {

        doNothing().when(validator).validateAccessTokenRequest(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        when(tokenDao.getActiveMerchantByKeys(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET)).thenReturn(buildMerchantData());
        when(tokenDao.saveToken(any(TokenDto.class))).thenAnswer(invocationOnMock -> {
            TokenDto tokenDto1 = invocationOnMock.getArgument(0);
            tokenDto1.setGeneratedToken("==fafoidvn234zxvvn");
            tokenDto1.setTokenType("Merchant");
            tokenDto1.setId(UUID.fromString("462c0c15-6c6c-4061-b2f2-852fa070816a"));
            tokenDto1.setStatus("Active");
            return tokenDto1;
        });

        when(authenticationService.generateAccessToken(any(AccessTokenRequest.class))).thenReturn("==fafoidvn234zxvvn");

        TransactionResponse<String> response = tokenService.generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);

        verify(validator, times(1)).validateAccessTokenRequest(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        verify(tokenDao, times(1)).getActiveMerchantByKeys(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        verify(tokenDao, times(2)).saveToken(any(TokenDto.class));

        assertNotNull(response);
        assertEquals("==fafoidvn234zxvvn", response.getData().get(0));
    }

    @Test
    void testGenerateTokenForValidationException() {

        String merchantApiKeyId = null;
        String merchantApiKeySecret = "invalidMerchantAPiKeySecret";
        doThrow(new ValidationException("1001", "Invalid Api Key")).when(validator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> validator.validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret));
        assertNotNull(validationException);
        assertEquals("1001", validationException.getErrorCode());
        assertEquals("Invalid Api Key", validationException.getErrorMessage());
    }

    @Test
    public void testGenerateTransactionTokenSuccess() {

        when(tokenDao.getActiveTransactionByHashValue(ORDER_HASH_CODE)).thenReturn(buildOrderDto());
        when(tokenDao.getActiveMerchantByMID(buildOrderDto().getMId())).thenReturn(buildMerchantData());

        when(tokenDao.saveToken(any(TokenDto.class))).thenAnswer(invocationOnMock -> {
            TokenDto tokenDto1 = invocationOnMock.getArgument(0);
            tokenDto1.setGeneratedToken("==fafoidvn234zxvvn");
            tokenDto1.setTokenType("Transaction");
            tokenDto1.setId(UUID.fromString("462c0c15-6c6c-4061-b2f2-852fa070816a"));
            tokenDto1.setStatus("Active");
            return tokenDto1;
        });

        TransactionResponse<String> transactionResponse = tokenService.generateTransactionToken(ORDER_HASH_CODE);

        verify(tokenDao, times(1)).getActiveTransactionByHashValue(ORDER_HASH_CODE);
        verify(tokenDao, times(1)).getActiveMerchantByMID(buildOrderDto().getMId());
        verify(tokenDao, times(2)).saveToken(any(TokenDto.class));

        assertNotNull(transactionResponse);
        assertEquals("==fafoidvn234zxvvn", transactionResponse.getData().get(0));


    }

    @Test
    public void testGeneratePaymentTokenSuccess() {

        when(tokenDao.getActiveTransactionByHashValue(ORDER_HASH_CODE)).thenReturn(buildOrderDto());
        when(tokenDao.getActiveMerchantByMID(buildOrderDto().getMId())).thenReturn(buildMerchantData());

        when(tokenDao.saveToken(any(TokenDto.class))).thenAnswer(invocationOnMock -> {
            TokenDto tokenDto1 = invocationOnMock.getArgument(0);
            tokenDto1.setGeneratedToken("generated_token");
            tokenDto1.setTokenType("Payment");
            tokenDto1.setId(UUID.fromString("5f310393-7949-4ea7-9e01-85cc550d528b"));
            tokenDto1.setStatus("Active");
            return tokenDto1;
        });

        TransactionResponse<String> transactionResponse = tokenService.generatePaymentToken(ORDER_HASH_CODE);

        verify(tokenDao, times(1)).getActiveTransactionByHashValue(ORDER_HASH_CODE);
        verify(tokenDao, times(1)).getActiveMerchantByMID(buildOrderDto().getMId());
        verify(tokenDao, times(2)).saveToken(any(TokenDto.class));

        assertNotNull(transactionResponse);
        assertEquals("generated_token", transactionResponse.getData().get(0));

    }


    private MerchantDto buildMerchantData() {
        return MerchantDto.builder()
                .mID("Merchant_id")
                .aek("aek_key")
                .kek("kek_key")
                .mek("mek_key")
                .accessTokenExpiryTime(24)
                .apiKey("api_key")
                .secretKey("secret_key")
                .status(TokenStatus.ACTIVE.name())
                .transactionTokenExpiryTime(24)
                .accessTokenExpiryTime(24)
                .build();
    }

    private OrderDto buildOrderDto(){
        return OrderDto.builder()
                .id(UUID.randomUUID())
                .currencyCode("INR")
                .orderHash("asfaah535")
                .customerId("57-43715_CustId")
                .expiry(24)
                .mId("merchant_id")
                .multiAccounts("No")
                .orderAmount(459.4)
                .orderRefNumber(35325775+"oder")
                .otherDetails("details")
                .sbiOrderRefNumber("sbi_3425435")
                .paymentMode("CARD")
                .build();
    }
}
