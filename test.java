import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService; // Your class containing the generateToken method

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private TokenDao tokenDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken_Success() {
        // Arrange
        String merchantApiKeyId = "validApiKey";
        String merchantApiKeySecret = "validSecret";
        MerchantDto merchantDto = new MerchantDto(); // Setup MerchantDto as needed
        String expectedToken = "generatedAccessToken";

        doNothing().when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        when(tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret)).thenReturn(merchantDto);
        doReturn(TransactionResponse.success(expectedToken)).when(tokenService).generateToken(any());

        // Act
        TransactionResponse<String> response = tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(expectedToken, response.getData());
        verify(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verify(tokenDao).getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);
    }

    @Test
    void testGenerateToken_InvalidRequest() {
        // Arrange
        String merchantApiKeyId = null;
        String merchantApiKeySecret = null;

        doThrow(new IllegalArgumentException("Invalid API Key or Secret"))
                .when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret));

        assertEquals("Invalid API Key or Secret", exception.getMessage());
        verify(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verifyNoInteractions(tokenDao);
    }

    @Test
    void testGenerateToken_MerchantNotFound() {
        // Arrange
        String merchantApiKeyId = "validApiKey";
        String merchantApiKeySecret = "validSecret";

        doNothing().when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        when(tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret));

        assertEquals("Merchant not found", exception.getMessage());
        verify(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verify(tokenDao).getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);
    }

    @Test
    void testGenerateToken_TokenGenerationFailure() {
        // Arrange
        String merchantApiKeyId = "validApiKey";
        String merchantApiKeySecret = "validSecret";
        MerchantDto merchantDto = new MerchantDto(); // Setup MerchantDto as needed

        doNothing().when(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        when(tokenDao.getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret)).thenReturn(merchantDto);
        doThrow(new RuntimeException("Token generation failed")).when(tokenService).generateToken(any());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret));

        assertEquals("Token generation failed", exception.getMessage());
        verify(tokenValidator).validateAccessTokenRequest(merchantApiKeyId, merchantApiKeySecret);
        verify(tokenDao).getActiveMerchantByKeys(merchantApiKeyId, merchantApiKeySecret);
    }
}
