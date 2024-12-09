@PostMapping("/access")
    @Operation(summary = "Access Token Generation")
    public TransactionResponse<String> generateAccessToken(@RequestHeader("Merchant-API-Key-Id") String merchantApiKeyId, @RequestHeader("Merchant-API-Key-Secret") String merchantApiKeySecret) {
        log.info("Access Token Request,  merchantApiKeyId : {}, merchantApiKeySecret : {}", merchantApiKeyId, merchantApiKeySecret);
        return tokenService.generateToken(merchantApiKeyId, merchantApiKeySecret);
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
