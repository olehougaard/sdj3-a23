package dk.via.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExchangeRate implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fromCurrency;
	private String toCurrency;
	private BigDecimal exchangeRate;
	
	public ExchangeRate(String fromCurrency, String toCurrency, BigDecimal exchangeRate) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.exchangeRate = exchangeRate;
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	
	public Money exchange(Money money) {
		if (!fromCurrency.equals(money.getCurrency())) {
			throw new IllegalArgumentException("Wrong currency");
		}
		return new Money(money.getAmount().multiply(exchangeRate), toCurrency);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result + ((fromCurrency == null) ? 0 : fromCurrency.hashCode());
		result = prime * result + ((toCurrency == null) ? 0 : toCurrency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExchangeRate other = (ExchangeRate) obj;
		if (exchangeRate == null) {
			if (other.exchangeRate != null)
				return false;
		} else if (!exchangeRate.equals(other.exchangeRate))
			return false;
		if (fromCurrency == null) {
			if (other.fromCurrency != null)
				return false;
		} else if (!fromCurrency.equals(other.fromCurrency))
			return false;
		if (toCurrency == null) {
			if (other.toCurrency != null)
				return false;
		} else if (!toCurrency.equals(other.toCurrency))
			return false;
		return true;
	}

	// Marshalling

	public ExchangeRate() {
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
