
@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private TokenService tokenService;

    private static final String MERCHANT_API_KEY_ID = "testKeyId";
    private static final String MERCHANT_API_KEY_SECRET = "testKeySecret";

    @Test
    void testGenerateAccessToken_Success() {
        // Arrange
        TransactionResponse<String> expectedResponse = new TransactionResponse<>();
        expectedResponse.setStatus("SUCCESS");
        expectedResponse.setData("dummyAccessToken");

        when(tokenService.generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET))
                .thenReturn(expectedResponse);

        // Act
        TransactionResponse<String> actualResponse = tokenController.generateAccessToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("SUCCESS", actualResponse.getStatus());
        assertEquals("dummyAccessToken", actualResponse.getData());

        verify(tokenService, times(1)).generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
    }
}

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private TokenDao tokenDao;

    @Mock
    private AccessTokenGenerator accessTokenGenerator;

    private static final String MERCHANT_API_KEY_ID = "testKeyId";
    private static final String MERCHANT_API_KEY_SECRET = "testKeySecret";

    @Test
    void testGenerateToken_Success() {
        // Arrange
        MerchantDto mockMerchantDto = new MerchantDto();
        mockMerchantDto.setMerchantId(1L);
        mockMerchantDto.setMerchantName("Test Merchant");

        AccessTokenRequest mockAccessTokenRequest = new AccessTokenRequest();
        mockAccessTokenRequest.setMerchantId(1L);
        mockAccessTokenRequest.setMerchantName("Test Merchant");

        TransactionResponse<String> expectedResponse = new TransactionResponse<>();
        expectedResponse.setStatus("SUCCESS");
        expectedResponse.setData("dummyAccessToken");

        when(tokenDao.getActiveMerchantByKeys(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET))
                .thenReturn(mockMerchantDto);
        when(accessTokenGenerator.generate(mockAccessTokenRequest))
                .thenReturn("dummyAccessToken");

        // Act
        TransactionResponse<String> actualResponse = tokenService.generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);

        // Assert
        assertNotNull(actualResponse);
        assertEquals("SUCCESS", actualResponse.getStatus());
        assertEquals("dummyAccessToken", actualResponse.getData());

        verify(tokenValidator, times(1)).validateAccessTokenRequest(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        verify(tokenDao, times(1)).getActiveMerchantByKeys(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        verify(accessTokenGenerator, times(1)).generate(mockAccessTokenRequest);
    }
}
