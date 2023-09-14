package dk.via.bank.service;

import dk.via.bank.data.CustomerDataService;
import dk.via.bank.model.Customer;
import dk.via.bank.service.exception.Conflict;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerDataService data;

	public CustomerController(@Autowired CustomerDataService data) {
		this.data = data;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer createCustomer(@RequestBody Customer customer) throws SQLException {
		return data.create(customer.getCpr(), customer.getName(), customer.getAddress());
	}

	@GetMapping("/{cpr}")
	public Customer readCustomer(@PathVariable("cpr") String cpr) throws SQLException {
		Customer customer = data.read(cpr);
		if (customer == null) throw new NotFound();
		return customer;
	}

	@PutMapping("/{cpr}")
	public Customer updateCustomer(@PathVariable("cpr") String cpr, @RequestBody Customer customer) throws SQLException {
		if (data.read(cpr) == null) {
			return createCustomer(customer);
		} else {
			if (customer.getCpr() != null && !customer.getCpr().equals(cpr)) {
				throw new Conflict();
			}
			data.update(customer);
			return customer;
		}
	}

	@DeleteMapping("/{cpr}")
	public void deleteCustomer(@PathVariable("cpr") String cpr) throws SQLException {
		data.delete(cpr);
	}
}
