package dk.via.cars;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Money implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal amount;
	private String currency;

	public Money(BigDecimal amount, String currency) {
		assert amount != null && currency != null;
		this.amount = amount;
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	@Override
	public int hashCode() {
		return amount.hashCode() ^ currency.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Money)) return false;
		Money that = (Money) obj;
		return this.amount.equals(that.amount) && this.currency.equals(that.currency);
	}
	
	@Override
	public String toString() {
		return String.format("%s %s", amount.toPlainString(), currency);
	}
}
