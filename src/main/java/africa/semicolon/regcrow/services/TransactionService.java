package africa.semicolon.regcrow.services;

import africa.semicolon.regcrow.dtos.request.TransactionInitiationRequest;
import africa.semicolon.regcrow.dtos.response.ApiResponse;
import africa.semicolon.regcrow.dtos.response.TransactionResponse;
import africa.semicolon.regcrow.exceptions.TransactionNotFoundException;
import africa.semicolon.regcrow.exceptions.UserNotFoundException;

import java.util.List;

public interface TransactionService {
    ApiResponse <?> initiateTransaction(TransactionInitiationRequest transactionInitiationRequest) throws UserNotFoundException;

    TransactionResponse findTransactionById(long id) throws TransactionNotFoundException;

    List<TransactionResponse> findAllTransactionByCustomerId(long customeId);

    List<TransactionResponse> findAllTransactions(int page, int items);

    ApiResponse deleteTransactionById(long l);

    void deleteAllTransactions();
}
