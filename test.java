public class ValidationException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private List<ErrorDto> errorMessages;

    public ValidationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ValidationException(List<ErrorDto> errorMessages) {
        this.errorMessages = errorMessages;
    }

}
