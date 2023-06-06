package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.CustomerRegistrationRequest;
import africa.semicolon.regcrow.dtos.request.TransactionInitiationRequest;
import africa.semicolon.regcrow.dtos.request.UserRegistrationRequest;
import africa.semicolon.regcrow.dtos.response.TransactionResponse;
import africa.semicolon.regcrow.exceptions.CustomerRegistrationFailedException;
import africa.semicolon.regcrow.exceptions.TransactionNotFoundException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static java.math.BigInteger.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RegcrowTransactionServiceTest {

    @Autowired
    RegcrowTransactionService regcrowTransactionService;

    @Autowired
    private RegcrowCustomerService regcrowCustomerService;
    private TransactionInitiationRequest transactionInitiationRequest;
    private CustomerRegistrationRequest customerRegistrationRequest;


    @BeforeEach
    void setUp() throws CustomerRegistrationFailedException {
        regcrowTransactionService.deleteAllTransactions();
        customerRegistrationRequest = new CustomerRegistrationRequest();
        customerRegistrationRequest.setEmail("Felix@gmail.com");
        customerRegistrationRequest.setPassword("12345");
        regcrowCustomerService.register(customerRegistrationRequest);
        regcrowCustomerService.register(customerRegistrationRequest);
        transactionInitiationRequest = TransactionInitiationRequest.builder()
                .sellerId(1L).buyerId(2L).paymentMethod("CARD")
                .amount(BigDecimal.valueOf(5000)).description("I am sending my details")
                .build();


    }

    @Test
    void createTransactionTest() throws UserNotFoundException {
        var transactionResponse = regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        assertThat(transactionResponse).isNotNull();
        String expected = "Transaction Initiated Successfully";
        assertThat(transactionResponse.getMessage()).isEqualTo(expected);

    }

    @Test
    void findTransactionWithTransactionIdTest() throws UserNotFoundException, TransactionNotFoundException {
         regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        var transactionResponse = regcrowTransactionService.findTransactionById(2L);
        assertThat(transactionResponse).isNotNull();
    }

    @Test
    void findAllTransactionsWithCustomerIdTest() throws UserNotFoundException, TransactionNotFoundException {
        regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        List<TransactionResponse> transactionResponse = regcrowTransactionService.findAllTransactionByCustomerId(2L);
        assertThat(BigInteger.ONE).isEqualTo(transactionResponse.size());
    }

    @Test
    void findAllTransactionsTest() throws UserNotFoundException, TransactionNotFoundException {
        regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        List<TransactionResponse> transactionResponse = regcrowTransactionService.findAllTransactions(1, 10);
        assertThat(BigInteger.ONE).isEqualTo(transactionResponse.size());
    }

    @Test
    public void deleteTransactionWithIdTest() throws UserNotFoundException {
        regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        var transactionResponse = regcrowTransactionService.deleteTransactionById(2L);
        String expected = "Transaction with id number 2 is Deleted Successfully";
        assertThat(transactionResponse.getMessage()).isEqualTo(expected);
    }

    @Test
    public void deleteAllAdminsTest() throws UserNotFoundException {
        regcrowTransactionService.initiateTransaction(transactionInitiationRequest);
        regcrowTransactionService.deleteAllTransactions();
        List <TransactionResponse> adminResponses = regcrowTransactionService.findAllTransactions(1, 10);
        assertThat(ZERO).isEqualTo(adminResponses.size());
    }
}