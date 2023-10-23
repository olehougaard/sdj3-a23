package dk.via.bank.service;

import dk.via.bank.data.AccountDataService;
import dk.via.bank.dto.AccountSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.service.exception.BadRequest;
import dk.via.bank.service.exception.Conflict;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	private final AccountDataService data;

	public AccountController(@Autowired AccountDataService data) {
		this.data = data;
	}

	private EntityModel<Account> createEntity(Account account) throws SQLException {
		String accountString = account.getAccountNumber().toString();
		return EntityModel.of(account,
				linkTo(methodOn(getClass()).readAccount(accountString)).withSelfRel(),
				linkTo(methodOn(TransactionController.class).readTransactionsFor(accountString)).withRel("transactions"));
	}

	private CollectionModel<EntityModel<Account>> createCollection(String cpr, Collection<Account> accounts) throws SQLException {
		List<EntityModel<Account>> accountModels = new ArrayList<>();
		for(Account account: accounts) {
            accountModels.add(createEntity(account));
        }
		return CollectionModel.of(accountModels, linkTo(methodOn(getClass()).readAccountsFor(cpr)).withSelfRel());
	}

	@PostMapping
	public EntityModel<Account> createAccount(@RequestBody AccountSpecification specification) throws SQLException {
		Account account = data.create(specification.getRegNumber(), specification.getCpr(), specification.getCurrency());
		return createEntity(account);
	}

	@GetMapping
    public CollectionModel<EntityModel<Account>> readAccountsFor(@RequestParam("cpr") String cpr) throws SQLException {
		return createCollection(cpr, data.readAccountsFor(cpr));
	}

	@GetMapping("/{accountNumber}")
    public EntityModel<Account> readAccount(@PathVariable("accountNumber") String accountString) throws SQLException {
		Account read = data.read(AccountNumber.fromString(accountString));
		if (read == null) throw new NotFound();
		return createEntity(read);
	}

	@PutMapping("/{accountNumber}")
    public void updateAccount(@PathVariable("accountNumber") String accountString, @RequestBody Account account) throws SQLException {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
		if (account.getAccountNumber() != null && !account.getAccountNumber().equals(accountNumber)) {
			throw new Conflict();
		}
		if (!account.getSettledCurrency().equals(account.getBalance().getCurrency())) {
			throw new BadRequest();
		}
		int updated = data.update(account);
		if (updated == 0) {
			throw new NotFound();
		}
	}

	@DeleteMapping("/{accountNumber}")
    public void deleteAccount(@PathVariable("accountNumber") String accountString) throws SQLException {
		AccountNumber accountNumber = AccountNumber.fromString(accountString);
    	data.delete(accountNumber);
	}
}
