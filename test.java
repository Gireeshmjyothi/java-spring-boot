@Test
void testGenerateTokenFailure() {
    doThrow(new ValidationException("1002", "Invalid Credentials"))
            .when(validator).validateAccessTokenRequest(MERCHANT_API_KEY_ID, "invalidSecret");

    ValidationException validationException = assertThrows(ValidationException.class, 
        () -> tokenService.generateToken(MERCHANT_API_KEY_ID, "invalidSecret"));

    verify(validator, times(1)).validateAccessTokenRequest(MERCHANT_API_KEY_ID, "invalidSecret");
    verify(tokenDao, times(0)).getActiveMerchantByKeys(anyString(), anyString());
    verify(tokenDao, times(0)).saveToken(any(TokenDto.class));

    assertEquals("1002", validationException.getErrorCode());
    assertEquals("Invalid Credentials", validationException.getErrorMessage());
}



@Test
void testGenerateTransactionTokenFailureNoOrder() {
    when(tokenDao.getActiveTransactionByHashValue(ORDER_HASH_CODE)).thenReturn(null);

    ValidationException validationException = assertThrows(ValidationException.class, 
        () -> tokenService.generateTransactionToken(ORDER_HASH_CODE));

    verify(tokenDao, times(1)).getActiveTransactionByHashValue(ORDER_HASH_CODE);
    verify(tokenDao, times(0)).getActiveMerchantByMID(anyString());
    verify(tokenDao, times(0)).saveToken(any(TokenDto.class));

    assertEquals("1003", validationException.getErrorCode());
    assertEquals("Order not found", validationException.getErrorMessage());
}

@Test
void testGenerateTransactionTokenFailureNoMerchant() {
    when(tokenDao.getActiveTransactionByHashValue(ORDER_HASH_CODE)).thenReturn(buildOrderDto());
    when(tokenDao.getActiveMerchantByMID(buildOrderDto().getMId())).thenReturn(null);

    ValidationException validationException = assertThrows(ValidationException.class, 
        () -> tokenService.generateTransactionToken(ORDER_HASH_CODE));

    verify(tokenDao, times(1)).getActiveTransactionByHashValue(ORDER_HASH_CODE);
    verify(tokenDao, times(1)).getActiveMerchantByMID(buildOrderDto().getMId());
    verify(tokenDao, times(0)).saveToken(any(TokenDto.class));

    assertEquals("1004", validationException.getErrorCode());
    assertEquals("Merchant not found", validationException.getErrorMessage());
}

@Test
void testGeneratePaymentTokenFailureInvalidOrderHash() {
    when(tokenDao.getActiveTransactionByHashValue("invalidHash")).thenReturn(null);

    ValidationException validationException = assertThrows(ValidationException.class, 
        () -> tokenService.generatePaymentToken("invalidHash"));

    verify(tokenDao, times(1)).getActiveTransactionByHashValue("invalidHash");
    verify(tokenDao, times(0)).getActiveMerchantByMID(anyString());
    verify(tokenDao, times(0)).saveToken(any(TokenDto.class));

    assertEquals("1005", validationException.getErrorCode());
    assertEquals("Invalid order hash", validationException.getErrorMessage());
}

@Test
void testSaveTokenFailure() {
    when(tokenDao.getActiveTransactionByHashValue(ORDER_HASH_CODE)).thenReturn(buildOrderDto());
    when(tokenDao.getActiveMerchantByMID(buildOrderDto().getMId())).thenReturn(buildMerchantData());
    doThrow(new RuntimeException("Database error")).when(tokenDao).saveToken(any(TokenDto.class));

    RuntimeException exception = assertThrows(RuntimeException.class, 
        () -> tokenService.generatePaymentToken(ORDER_HASH_CODE));

    verify(tokenDao, times(1)).getActiveTransactionByHashValue(ORDER_HASH_CODE);
    verify(tokenDao, times(1)).getActiveMerchantByMID(buildOrderDto().getMId());
    verify(tokenDao, times(1)).saveToken(any(TokenDto.class));

    assertEquals("Database error", exception.getMessage());
}
