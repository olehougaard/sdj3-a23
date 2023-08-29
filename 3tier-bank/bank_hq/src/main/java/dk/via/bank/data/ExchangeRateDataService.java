package dk.via.bank.data;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dk.via.bank.model.ExchangeRate;

public class ExchangeRateDataService extends UnicastRemoteObject implements ExchangeRateData {
	private static final long serialVersionUID = 1L;
	private final DatabaseHelper<BigDecimal> helper;

	public ExchangeRateDataService(String jdbcURL, String username, String password) throws RemoteException {
		this.helper = new DatabaseHelper<>(jdbcURL, username, password);
	}

	@Override
	public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException {
		BigDecimal rate = helper.mapSingle(rs->rs.getBigDecimal(1),
				"SELECT rate FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?",
				fromCurrency,
				toCurrency);
		return new ExchangeRate(fromCurrency, toCurrency, rate);
	}
}
