package dk.via.bank.service;

import dk.via.bank.data.CustomerDataService;
import dk.via.bank.model.Customer;
import dk.via.bank.service.exception.Conflict;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	private final CustomerDataService data;

	public CustomerController(@Autowired CustomerDataService data) {
		this.data = data;
	}

	private EntityModel<Customer> createEntity(Customer customer) throws SQLException {
		String cpr = customer.getCpr();
		return EntityModel.of(customer,
				linkTo(methodOn(getClass()).readCustomer(cpr)).withSelfRel(),
				linkTo(methodOn(AccountController.class).readAccountsFor(cpr)).withRel("accounts"));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EntityModel<Customer> createCustomer(@RequestBody Customer customer) throws SQLException {
		return createEntity(data.create(customer.getCpr(), customer.getName(), customer.getAddress()));
	}

	@GetMapping("/{cpr}")
	public EntityModel<Customer> readCustomer(@PathVariable("cpr") String cpr) throws SQLException {
		Customer customer = data.read(cpr);
		if (customer == null) throw new NotFound();
		return createEntity(customer);
	}

	@PutMapping("/{cpr}")
	public EntityModel<Customer> updateCustomer(@PathVariable("cpr") String cpr, @RequestBody Customer customer) throws SQLException {
		if (data.read(cpr) == null) {
			return createCustomer(customer);
		} else {
			if (customer.getCpr() != null && !customer.getCpr().equals(cpr)) {
				throw new Conflict();
			}
			data.update(customer);
			return createEntity(customer);
		}
	}

	@DeleteMapping("/{cpr}")
	public void deleteCustomer(@PathVariable("cpr") String cpr) throws SQLException {
		data.delete(cpr);
	}
}
