package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.TransactionInitiationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.CustomerResponse;
import africa.semicolon.regcrow.dtos.response.TransactionResponse;
import africa.semicolon.regcrow.exceptions.TransactionNotFoundException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;
import africa.semicolon.regcrow.models.Payment;
import africa.semicolon.regcrow.models.Transaction;
import africa.semicolon.regcrow.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static africa.semicolon.regcrow.models.PaymentMethod.CARD;
import static africa.semicolon.regcrow.utils.AppUtils.buildPageRequest;
import static africa.semicolon.regcrow.utils.ResponseUtils.TRANSACTION_DELETED_SUCCESSFULLY;
import static africa.semicolon.regcrow.utils.ResponseUtils.TRANSACTION_ID_NOT_FOUND;

@Service
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RegcrowTransactionService implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final RegcrowCustomerService regcrowCustomerService;

    @Override
    public ApiResponse<?> initiateTransaction(TransactionInitiationRequest transactionInitiationRequest) throws UserNotFoundException {
        CustomerResponse foundBuyer = regcrowCustomerService.getCustomerById(transactionInitiationRequest.getBuyerId());
        CustomerResponse foundSeller = regcrowCustomerService.getCustomerById(transactionInitiationRequest.getSellerId());
        Payment payment = new Payment();
        payment.setAmount(transactionInitiationRequest.getAmount());
        if(transactionInitiationRequest.getPaymentMethod().equals("CARD")){
            payment.setPaymentMethod(CARD); }

        Transaction transaction = new Transaction();
        transaction.setSellerId(foundBuyer.getId());
        transaction.setBuyerId(foundSeller.getId());
        transaction.setPayment(payment);
        transaction.setDescription(transactionInitiationRequest.getDescription());
        transactionRepository.save(transaction);
        return ApiResponse.builder().message("Transaction Initiated Successfully").build();

    }

    @Override
    public TransactionResponse findTransactionById(long id) throws TransactionNotFoundException {
        Optional<Transaction> foundtransaction = transactionRepository.findById(id);
        Transaction transaction = foundtransaction.orElseThrow(()-> new TransactionNotFoundException(
                String.format(TRANSACTION_ID_NOT_FOUND, id)));
        return buildTransactionResponse(transaction);


    }

    @Override
    public List<TransactionResponse> findAllTransactionByCustomerId(long customerId) {
        List <Transaction> foundTransactions = transactionRepository.findAllById(Collections.singleton(customerId));
        return foundTransactions.stream().map(RegcrowTransactionService::buildTransactionResponse).collect(Collectors.toList());

    }

    @Override
    public List<TransactionResponse> findAllTransactions(int page, int items) {
        Pageable pageable = buildPageRequest(page, items);
        Page <Transaction> transactionPage = transactionRepository.findAll(pageable);
        List <Transaction> allTransactions = transactionPage.getContent();
        return allTransactions.stream().map(RegcrowTransactionService::buildTransactionResponse).toList();
    }

    @Override
    public ApiResponse<?> deleteTransactionById(long id) {
        transactionRepository.deleteById(id);
        return  ApiResponse.builder().message(String.format(TRANSACTION_DELETED_SUCCESSFULLY, id)).build();
    }

    @Override
    public void deleteAllTransactions() {
        transactionRepository.deleteAll();

    }

    private static TransactionResponse buildTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder().Id(transaction.getId()).description(transaction.getDescription())
                .paymentMethod(String.valueOf(transaction.getPayment().getPaymentMethod())).sellerId(transaction.getSellerId())
                .buyerId(transaction.getBuyerId()).amount(transaction.getPayment().getAmount()).build();
    }
}
