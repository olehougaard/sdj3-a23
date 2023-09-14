package dk.via.bank.service;

import dk.via.bank.data.AccountDataService;
import dk.via.bank.data.TransactionDataService;
import dk.via.bank.dto.TransactionSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.transaction.Transaction;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/accounts/{accountNumber}/transactions")
public class TransactionController {
	private final TransactionDataService transactionData;
	private final AccountDataService accountData;

	public TransactionController(@Autowired TransactionDataService transactionData, AccountDataService accountData) {
		this.transactionData = transactionData;
		this.accountData = accountData;
	}

	@PostMapping
	public TransactionSpecification createTransaction(@PathVariable("accountNumber") String accountString, @RequestBody TransactionSpecification transactionSpec) throws SQLException {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		Account account = accountData.read(accountNumber);
		if (account == null) throw new NotFound();
		Transaction transaction = transactionSpec.toTransaction(account);
		return TransactionSpecification.from(transactionData.create(transaction));
	}

	@GetMapping("/{id}")
	public TransactionSpecification readTransaction(@PathVariable("accountNumber") String accountString, @PathVariable("id") int transactionId) throws SQLException {
		Transaction read = transactionData.read(transactionId);
		if (read == null || !read.includes(AccountNumber.fromString(accountString))) throw new NotFound();
		return TransactionSpecification.from(read);
	}

	@GetMapping
	public List<TransactionSpecification> readTransactionsFor(@PathVariable("accountNumber") String accountString) throws SQLException {
		return transactionData
				.readAllFor(AccountNumber.fromString(accountString))
				.stream()
				.map(TransactionSpecification::from)
				.toList();
	}
}
