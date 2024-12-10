@Component
@RequiredArgsConstructor
public class TokenValidator extends BaseValidator {

    public void validateAccessTokenRequest(String merchantApiKeyId, String merchantApiKeySecret) {
        checkMandatoryField("Merchant Api Key Id", merchantApiKeyId);
        checkMandatoryField("Merchant Api Key Secret", merchantApiKeySecret);
        throwIfErrors();
    }

}


public class BaseValidator {

    List<ErrorDto> errorDtoList = new ArrayList<>();

    void checkMandatoryField(String value, String fieldName) {
        if (StringUtils.isEmpty(value)) {
            addError(fieldName, ErrorConstants.MANDATORY_FOUND_ERROR_CODE, ErrorConstants.MANDATORY_ERROR_MESSAGE);
        }
    }

    void checkMandatoryFields(String fieldName, String... values) {
        boolean allEmpty = Arrays.stream(values).allMatch(StringUtils::isEmpty);
        if (allEmpty) {
            addError(fieldName, ErrorConstants.MANDATORY_FOUND_ERROR_CODE, ErrorConstants.MANDATORY_ERROR_MESSAGE);
        }
    }

    void validateFieldLength(String value, int maxLength, String fieldName) {
        if (StringUtils.isNotEmpty(value) && value.length() > maxLength) {
            addError(fieldName, ErrorConstants.INVALID_ERROR_CODE, "Max allowed length is " + maxLength);
        }
    }

    void validateFieldWithRegex(String value, int maxLength, String regex, String fieldName, String message) {
        if (StringUtils.isNotEmpty(value) && (value.length() > maxLength || validate(value, regex))) {
            addError(fieldName, ErrorConstants.INVALID_ERROR_CODE, message + " " + maxLength);
        }
    }

    void validateFieldWithRegex(String value, String regex, String fieldName, String message) {
        if (StringUtils.isNotEmpty(value) && validate(value, regex)) {
            addError(fieldName, ErrorConstants.INVALID_ERROR_CODE, message);
        }
    }

    void addError(String fieldName, String errorCode, String errorMessage) {
        errorDtoList.add(ErrorDto.builder().errorCode(errorCode).errorMessage(MessageFormat.format(errorMessage, fieldName)).build());
    }

    void throwIfErrors() {
        if (!errorDtoList.isEmpty()) {
            throw new ValidationException(new ArrayList<>(errorDtoList));
        }
    }

    boolean validate(String value, String regex) {
        return !Pattern.matches(regex, value);
    }
}
