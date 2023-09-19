package dk.via.bank;

import dk.via.bank.model.Account;
import dk.via.bank.model.Customer;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.DepositTransaction;
import dk.via.bank.model.transaction.Transaction;
import dk.via.bank.model.transaction.TransferTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransferIntegrationTest {
	private Branch branch;
	private Account primaryAccount;
	private Account secondaryAccount;

	@Before
	public void setUp() throws Exception {
		branch = new BranchClient("localhost", 9090);
		Customer customer = branch.getCustomer("1234567890");
		primaryAccount = branch.createAccount(customer, "DKK");
		assertNotNull(primaryAccount);
		Customer other = branch.getCustomer("1122334455");
		secondaryAccount = branch.createAccount(other, "EUR");
	}
	
	@After
	public void tearDown() throws Exception {
		branch.cancelAccount(primaryAccount);
		branch.cancelAccount(secondaryAccount);
	}
	
	@Test
	public void test() throws RemoteException {
		Money startingAmount = new Money(new BigDecimal(10000), "DKK");
		Money transferAmount = new Money(new BigDecimal(1000), "DKK");
		Money remainingAmount = new Money(new BigDecimal(9000), "DKK");
		List<Transaction> primaryTransactionsBefore = branch.getTransactionsFor(primaryAccount);
		List<Transaction> secondaryTransactionsBefore = branch.getTransactionsFor(secondaryAccount);
		branch.execute(new DepositTransaction(-1, startingAmount, primaryAccount));
		primaryAccount = branch.getAccount(primaryAccount.getAccountNumber());
		assertEquals(startingAmount, primaryAccount.getBalance());
		branch.execute(new TransferTransaction(-1, transferAmount, primaryAccount, secondaryAccount));
		primaryAccount = branch.getAccount(primaryAccount.getAccountNumber());
		secondaryAccount = branch.getAccount(secondaryAccount.getAccountNumber());
		assertEquals(remainingAmount, primaryAccount.getBalance());
		assertEquals(branch.exchange(transferAmount, secondaryAccount.getSettledCurrency()), secondaryAccount.getBalance());
		assertEquals(primaryTransactionsBefore.size() + 2, branch.getTransactionsFor(primaryAccount).size());
		assertEquals(secondaryTransactionsBefore.size() + 1, branch.getTransactionsFor(secondaryAccount).size());
	}
}
