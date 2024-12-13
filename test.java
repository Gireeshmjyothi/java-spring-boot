public String generateUniqueCustomerId() {
        logger.info("CustomerId generation initiated");
        int retryCount = 1;
        String customerId = "Cust_" + RandomStringUtils.randomAlphanumeric(16);
        while (retryCount < 3 && customerRepository.existsByCustomerId(customerId)) {
            customerId = "Cust_" + RandomStringUtils.randomAlphanumeric(16);
            retryCount++;
        }
        if (retryCount == 3 && customerRepository.existsByCustomerId(customerId)) {
            throw new TransactionException(ErrorConstants.ALREADY_EXIST_ERROR_CODE, MessageFormat.format(ErrorConstants.ALREADY_EXIST_ERROR_MESSAGE, "customerId"));
        }
        logger.info("CustomerId created successfully");
        return customerId;
    }
