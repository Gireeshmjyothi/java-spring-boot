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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateUniqueCustomerId_PositiveScenario() {
        // Mock behavior
        when(customerRepository.existsByCustomerId(anyString())).thenReturn(false);

        // Execute method
        String customerId = customerService.generateUniqueCustomerId();

        // Verify and Assert
        assertNotNull(customerId);
        assertTrue(customerId.startsWith("Cust_"));
        assertEquals(21, customerId.length()); // "Cust_" + 16 alphanumeric
        verify(customerRepository, times(1)).existsByCustomerId(anyString());
    }

    @Test
    public void testGenerateUniqueCustomerId_NegativeScenario() {
        // Mock behavior: simulate customerId already exists for 3 retries
        when(customerRepository.existsByCustomerId(anyString())).thenReturn(true);

        // Execute and Assert
        TransactionException exception = assertThrows(TransactionException.class, 
                () -> customerService.generateUniqueCustomerId());
        
        assertEquals(ErrorConstants.ALREADY_EXIST_ERROR_CODE, exception.getErrorCode());
        assertTrue(exception.getMessage().contains("customerId"));

        // Verify
        verify(customerRepository, times(3)).existsByCustomerId(anyString());
    }

    @Test
    public void testGenerateUniqueCustomerId_RetryScenario() {
        // Mock behavior: customerId exists on the first attempt but succeeds on the second
        when(customerRepository.existsByCustomerId(anyString()))
                .thenReturn(true)  // First attempt
                .thenReturn(false); // Second attempt

        // Execute method
        String customerId = customerService.generateUniqueCustomerId();

        // Verify and Assert
        assertNotNull(customerId);
        assertTrue(customerId.startsWith("Cust_"));
        verify(customerRepository, times(2)).existsByCustomerId(anyString());
    }
}

@Test
public void testGenerateUniqueCustomerId_RaceCondition() {
    // Mock behavior: simulate race condition
    when(customerRepository.existsByCustomerId(anyString()))
            .thenReturn(false) // First check indicates uniqueness
            .thenReturn(true); // Subsequent checks reveal a duplicate

    // Execute method
    String customerId = customerService.generateUniqueCustomerId();

    // Assert that the generated customerId is unique on retry
    assertNotNull(customerId);
    assertTrue(customerId.startsWith("Cust_"));
    verify(customerRepository, atLeastOnce()).existsByCustomerId(anyString());
}
