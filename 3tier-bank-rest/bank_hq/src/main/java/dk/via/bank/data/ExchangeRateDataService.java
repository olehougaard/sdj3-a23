package dk.via.bank.data;

import dk.via.bank.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Scope("singleton")
public class ExchangeRateDataService {
	private final DatabaseHelper<ExchangeRate> helper;

	public ExchangeRateDataService(@Autowired DatabaseHelper<ExchangeRate> helper) {
		this.helper = helper;
	}

	private static ExchangeRate map(ResultSet rs) throws SQLException {
		return new ExchangeRate(rs.getString("from_currency"), rs.getString("to_currency"), rs.getBigDecimal("rate"));
	}

	public List<ExchangeRate> getExchangeRates() throws SQLException {
		return helper.map(ExchangeRateDataService::map, "SELECT * FROM Exchange_rates");
	}

	public List<ExchangeRate> getExchangeRatesTo(String toCurrency) throws SQLException {
		return helper.map(
				ExchangeRateDataService::map,
				"SELECT * FROM Exchange_rates WHERE to_currency = ?",
				toCurrency);
	}

	public List<ExchangeRate> getExchangeRatesFrom(String fromCurrency) throws SQLException {
		return helper.map(
				ExchangeRateDataService::map,
				"SELECT * FROM Exchange_rates WHERE from_currency = ?",
				fromCurrency);
	}

	public ExchangeRate map(String fromCurrency, String toCurrency) throws SQLException {
		return helper.mapSingle(
				ExchangeRateDataService::map,
				"SELECT * FROM Exchange_rates WHERE from_currency = ? AND to_currency = ?",
				fromCurrency,
				toCurrency);
	}
}
