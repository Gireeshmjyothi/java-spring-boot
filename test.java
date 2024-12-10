@Test
    public void testGenerateTokenSuccess() {

        when(tokenService.generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET)).thenReturn(buildTransactionResponse());

        doThrow(new ValidationException("1001", "Invalid Api Key")).when(validator).validateAccessTokenRequest(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        TransactionResponse<String> actualResponse = tokenService.generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);

        assertNotNull(actualResponse);
        assertEquals(0, actualResponse.getStatus());
        assertEquals(List.of("afanvannvcaonvan414==*60Ffa"), actualResponse.getData());
        assertEquals(1, actualResponse.getTotal());
        assertEquals(1, actualResponse.getCount());

        verify(tokenService, times(1)).generateToken(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
        verify(validator, times(1)).validateAccessTokenRequest(MERCHANT_API_KEY_ID, MERCHANT_API_KEY_SECRET);
    }
