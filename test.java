Thanks for sharing your ValidationException class. Based on this updated class, I’ve modified the test cases to correctly handle the structure of the ValidationException. Specifically:

The test cases now check both the errorMessage and errorMessages fields, depending on how the exception is thrown.

For cases involving multiple error messages, the errorMessages list is verified.


Here’s the updated test class:

Updated TokenValidatorTest:

package com.epay.transaction.validator;

import com.epay.transaction.exceptions.ValidationException;
import com.epay.transaction.dto.ErrorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        assertNotNull(exception.getErrorMessages());
        assertEquals(1, exception.getErrorMessages().size());
        assertEquals("Merchant Api Key Id is mandatory.",
                exception.getErrorMessages().get(0).getErrorMessage());
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
        assertNotNull(exception.getErrorMessages());
        assertEquals(1, exception.getErrorMessages().size());
        assertEquals("Merchant Api Key Secret is mandatory.",
                exception.getErrorMessages().get(0).getErrorMessage());
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
        assertNotNull(exception.getErrorMessages());
        assertEquals(2, exception.getErrorMessages().size());
        assertTrue(exception.getErrorMessages().stream()
                .anyMatch(error -> error.getErrorMessage().equals("Merchant Api Key Id is mandatory.")));
        assertTrue(exception.getErrorMessages().stream()
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
        assertNotNull(exception.getErrorMessages());
        assertEquals(1, exception.getErrorMessages().size());
        assertEquals("Max allowed length is 10",
                exception.getErrorMessages().get(0).getErrorMessage());
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
        assertNotNull(exception.getErrorMessages());
        assertEquals(1, exception.getErrorMessages().size());
        assertEquals("Invalid format",
                exception.getErrorMessages().get(0).getErrorMessage());
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
        assertNotNull(exception.getErrorMessages());
        assertEquals(1, exception.getErrorMessages().size());
        assertEquals("Test Field is mandatory.",
                exception.getErrorMessages().get(0).getErrorMessage());
    }
}

Key Changes:

1. ValidationException Handling:

Checked getErrorMessages() for lists of errors.

Ensured individual error messages are validated from ErrorDto.



2. Assertions:

Ensured assertNotNull for errorMessages to validate the presence of error lists.

Added stream() checks where multiple errors are expected.




This revised version aligns with your shared ValidationException class. Let me know if there are any further requirements!

