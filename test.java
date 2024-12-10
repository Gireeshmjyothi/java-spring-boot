package com.epay.transaction.validator;

import com.epay.transaction.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenValidatorTest {

    private TokenValidator tokenValidator;

    @BeforeEach
    void setUp() {
        tokenValidator = new TokenValidator();
    }

    @Test
    void validateAccessTokenRequest_ValidFields_ShouldNotThrowException() {
        // Arrange
        String validApiKeyId = "validApiKeyId";
        String validApiKeySecret = "validApiKeySecret";

        // Act & Assert
        assertDoesNotThrow(() -> tokenValidator.validateAccessTokenRequest(validApiKeyId, validApiKeySecret));
    }

    @Test
    void validateAccessTokenRequest_MissingApiKeyId_ShouldThrowValidationException() {
        // Arrange
        String missingApiKeyId = null;
        String validApiKeySecret = "validApiKeySecret";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.validateAccessTokenRequest(missingApiKeyId, validApiKeySecret));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(1, exception.getErrorList().size());
        assertEquals("Merchant Api Key Id is mandatory.", exception.getErrorList().get(0).getErrorMessage());
    }

    @Test
    void validateAccessTokenRequest_MissingApiKeySecret_ShouldThrowValidationException() {
        // Arrange
        String validApiKeyId = "validApiKeyId";
        String missingApiKeySecret = "";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.validateAccessTokenRequest(validApiKeyId, missingApiKeySecret));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(1, exception.getErrorList().size());
        assertEquals("Merchant Api Key Secret is mandatory.", exception.getErrorList().get(0).getErrorMessage());
    }

    @Test
    void validateAccessTokenRequest_MissingBothFields_ShouldThrowValidationException() {
        // Arrange
        String missingApiKeyId = "";
        String missingApiKeySecret = "";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.validateAccessTokenRequest(missingApiKeyId, missingApiKeySecret));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(2, exception.getErrorList().size());
        assertTrue(exception.getErrorList().stream()
                .anyMatch(error -> error.getErrorMessage().equals("Merchant Api Key Id is mandatory.")));
        assertTrue(exception.getErrorList().stream()
                .anyMatch(error -> error.getErrorMessage().equals("Merchant Api Key Secret is mandatory.")));
    }

    @Test
    void validateFieldLength_ValidFieldLength_ShouldNotThrowException() {
        // Arrange
        String validValue = "12345";
        int maxLength = 10;
        String fieldName = "Test Field";

        // Act & Assert
        assertDoesNotThrow(() -> tokenValidator.validateFieldLength(validValue, maxLength, fieldName));
    }

    @Test
    void validateFieldLength_ExceedsMaxLength_ShouldThrowValidationException() {
        // Arrange
        String invalidValue = "12345678901"; // 11 characters
        int maxLength = 10;
        String fieldName = "Test Field";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.validateFieldLength(invalidValue, maxLength, fieldName));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(1, exception.getErrorList().size());
        assertEquals("Max allowed length is 10", exception.getErrorList().get(0).getErrorMessage());
    }

    @Test
    void validateFieldWithRegex_ValidPattern_ShouldNotThrowException() {
        // Arrange
        String validValue = "abc123";
        String regex = "^[a-zA-Z0-9]+$";
        String fieldName = "Test Field";
        String message = "Invalid format";

        // Act & Assert
        assertDoesNotThrow(() -> tokenValidator.validateFieldWithRegex(validValue, regex, fieldName, message));
    }

    @Test
    void validateFieldWithRegex_InvalidPattern_ShouldThrowValidationException() {
        // Arrange
        String invalidValue = "abc@123";
        String regex = "^[a-zA-Z0-9]+$";
        String fieldName = "Test Field";
        String message = "Invalid format";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.validateFieldWithRegex(invalidValue, regex, fieldName, message));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(1, exception.getErrorList().size());
        assertEquals("Invalid format", exception.getErrorList().get(0).getErrorMessage());
    }

    @Test
    void checkMandatoryField_ValidField_ShouldNotThrowException() {
        // Arrange
        String validValue = "validValue";
        String fieldName = "Test Field";

        // Act & Assert
        assertDoesNotThrow(() -> tokenValidator.checkMandatoryField(validValue, fieldName));
    }

    @Test
    void checkMandatoryField_EmptyField_ShouldThrowValidationException() {
        // Arrange
        String emptyValue = "";
        String fieldName = "Test Field";

        // Act
        ValidationException exception = assertThrows(ValidationException.class, () ->
                tokenValidator.checkMandatoryField(emptyValue, fieldName));

        // Assert
        assertNotNull(exception.getErrorList());
        assertEquals(1, exception.getErrorList().size());
        assertEquals("Test Field is mandatory.", exception.getErrorList().get(0).getErrorMessage());
    }
}
