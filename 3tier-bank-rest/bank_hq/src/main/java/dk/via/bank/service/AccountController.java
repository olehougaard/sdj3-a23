package dk.via.bank.service;

import dk.via.bank.data.AccountDataService;
import dk.via.bank.dto.AccountSpecification;
import dk.via.bank.model.Account;
import dk.via.bank.model.AccountNumber;
import dk.via.bank.service.exception.BadRequest;
import dk.via.bank.service.exception.Conflict;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	private final AccountDataService data;

	public AccountController(@Autowired AccountDataService data) {
		this.data = data;
	}

	@PostMapping
	public Account createAccount(@RequestBody AccountSpecification specification) throws SQLException {
		return data.create(specification.getRegNumber(), specification.getCpr(), specification.getCurrency());
	}

	@GetMapping
    public Collection<Account> readAccountsFor(@RequestParam("cpr") String cpr) throws SQLException {
		return data.readAccountsFor(cpr);
	}

	@GetMapping("/{accountNumber}")
    public Account readAccount(@PathVariable("accountNumber") String accountString) throws SQLException {
		Account read = data.read(AccountNumber.fromString(accountString));
		if (read == null) throw new NotFound();
		return read;
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
