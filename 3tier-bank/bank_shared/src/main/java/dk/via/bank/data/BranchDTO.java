package dk.via.bank.data;

import java.io.Serializable;

public class BranchDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int regNumber;
	private String address;

	public BranchDTO(int regNumber, String address) {
		this.regNumber = regNumber;
		this.address = address;
	}

	public int getRegNumber() {
		return regNumber;
	}

	public String getAddress() {
		return address;
	}
}
