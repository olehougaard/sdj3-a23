package dk.via.bank.service;

import dk.via.bank.data.AccountDataService;
import dk.via.bank.data.TransactionDataService;
import dk.via.bank.dto.TransactionSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.model.transaction.Transaction;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/accounts/{accountNumber}/transactions")
public class TransactionController {
	private final TransactionDataService transactionData;
	private final AccountDataService accountData;

	public TransactionController(@Autowired TransactionDataService transactionData, AccountDataService accountData) {
		this.transactionData = transactionData;
		this.accountData = accountData;
	}

	private EntityModel<TransactionSpecification> createEntity(String accountString, TransactionSpecification transaction) throws SQLException {
		return EntityModel.of(transaction,
				linkTo(methodOn(getClass()).readTransaction(accountString, transaction.getId())).withSelfRel());
	}

	private CollectionModel<EntityModel<TransactionSpecification>> createCollection(String accountString, List<TransactionSpecification> transactions) throws SQLException {
		List<EntityModel<TransactionSpecification>> accountModels = new ArrayList<>();
		for(TransactionSpecification t: transactions) {
            accountModels.add(createEntity(accountString, t));
        }
		return CollectionModel.of(accountModels, linkTo(methodOn(getClass()).readTransactionsFor(accountString)).withSelfRel());
	}

	@PostMapping
	public EntityModel<TransactionSpecification> createTransaction(
			@PathVariable("accountNumber") String accountString,
			@RequestBody TransactionSpecification transactionSpec)
			throws SQLException {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		Account account = accountData.read(accountNumber);
		if (account == null) throw new NotFound();
		Transaction transaction = transactionSpec.toTransaction(account);
		return createEntity(accountString, TransactionSpecification.from(transactionData.create(transaction)));
	}

	@GetMapping("/{id}")
	public EntityModel<TransactionSpecification> readTransaction(
			@PathVariable("accountNumber") String accountString,
			@PathVariable("id") int transactionId)
			throws SQLException {
		Transaction read = transactionData.read(transactionId);
		if (read == null || !read.includes(AccountNumber.fromString(accountString))) throw new NotFound();
		return createEntity(accountString, TransactionSpecification.from(read));
	}

	@GetMapping
	public CollectionModel<EntityModel<TransactionSpecification>> readTransactionsFor(
			@PathVariable("accountNumber") String accountString)
			throws SQLException {
		List<TransactionSpecification> specifications = transactionData
				.readAllFor(AccountNumber.fromString(accountString))
				.stream()
				.map(TransactionSpecification::from)
				.toList();
		return createCollection(accountString, specifications);
	}
}
