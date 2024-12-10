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
