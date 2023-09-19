package dk.via.bank.model;

import java.io.Serializable;

public final class AccountNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int regNumber;
	private long accountNumber;

	public AccountNumber(int regNumber, long accountNumber) {
		this.regNumber = regNumber;
		this.accountNumber = accountNumber;
	}

	public int getRegNumber() {
		return regNumber;
	}

	public long getAccountNumber() {
		return accountNumber;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(regNumber) ^ Long.hashCode(accountNumber);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AccountNumber)) {
			return false;
		}
		AccountNumber that = (AccountNumber) obj;
		return this.regNumber == that.regNumber && this.accountNumber == that.accountNumber;
	}
	public String toString() {
		return String.format("%04d%06d", regNumber, accountNumber);
	}
	public static AccountNumber fromString(String accountString) {
		int regNumber = Integer.parseInt(accountString.substring(0, 4));
		long accountNumber = Long.parseLong(accountString.substring(4));
		return new AccountNumber(regNumber, accountNumber);
	}

	// Marshalling

	public AccountNumber() {
	}

	public void setRegNumber(int regNumber) {
		this.regNumber = regNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
}
