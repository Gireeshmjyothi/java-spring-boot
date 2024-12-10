package com.epay.transaction.service;

import com.epay.transaction.dao.TokenDao;
import com.epay.transaction.dto.MerchantDto;
import com.epay.transaction.dto.TokenDto;
import com.epay.transaction.exceptions.TransactionException;
import com.epay.transaction.model.response.TransactionResponse;
import com.epay.transaction.util.enums.TokenType;
import com.epay.transaction.validator.TokenValidator;
import com.sbi.epay.authentication.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    @Mock
    private TokenDao tokenDao;

    @Mock
    private AuthenticationService authService;

    @Mock
    private TokenValidator tokenValidator;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        // Mock inputs
        String merchantApiKeyId = "testApiKey";
        String merchantApiKeySecret = "testSecret";

        // Mock MerchantDto
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setMID("testMID");
        merchantDto.setSecretKey("testSecretKey");
        merchantDto.setApiKey("testApiKey");
        merchantDto.setAccessTokenExpiryTime(30);

        // Stub methods
        doNothing().when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        when(tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret)).thenReturn(merchantDto);
        when(authService.generateAccessToken(any())).thenReturn("generatedAccessToken");
        when(tokenDao.saveToken(any())).thenAnswer(invocation -> {
            TokenDto tokenDto = invocation.getArgument(0);
            tokenDto.setGeneratedToken("generatedAccessToken");
            return tokenDto;
        });

        // Call the method under test
        TransactionResponse<String> response = tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret);

        // Verify interactions
        verify(tokenValidator, times(1)).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verify(tokenDao, times(1)).getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);
        verify(authService, times(1)).generateAccessToken(any());
        verify(tokenDao, times(2)).saveToken(any());

        // Assertions
        assertNotNull(response);
        assertEquals(1, response.getStatus());
        assertEquals("generatedAccessToken", response.getData().get(0));
    }

    @Test
    void testGenerateTokenWithInvalidMerchant() {
        // Mock inputs
        String merchantApiKeyId = "invalidApiKey";
        String merchantApiKeySecret = "invalidSecret";

        // Stub methods
        doNothing().when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        when(tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret))
            .thenThrow(new TransactionException("404", "Merchant not found"));

        // Call the method under test and assert exception
        TransactionException exception = assertThrows(TransactionException.class, () ->
            tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret)
        );

        // Verify interactions
        verify(tokenValidator, times(1)).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verify(tokenDao, times(1)).getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);

        // Assertions
        assertEquals("404", exception.getErrorCode());
        assertEquals("Merchant not found", exception.getMessage());
    }
}
