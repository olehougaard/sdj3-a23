package dk.via.bank.service;

import dk.via.bank.data.ExchangeRateDataService;
import dk.via.bank.model.ExchangeRate;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/exchange_rate")
public class ExchangeRateController {
	private final ExchangeRateDataService data;

	public ExchangeRateController(@Autowired ExchangeRateDataService data) {
		this.data = data;
	}

	@GetMapping
	public List<ExchangeRate> getRates(
			@RequestParam(value = "fromCurrency", required = false) String fromCurrency,
			@RequestParam(value = "toCurrency", required = false) String toCurrency) throws SQLException {
		return getExchangeRateList(fromCurrency, toCurrency);
	}

	private List<ExchangeRate> getExchangeRateList(String fromCurrency, String toCurrency) throws SQLException {
		if (fromCurrency != null && toCurrency != null) {
			ExchangeRate exchangeRate = data.map(fromCurrency, toCurrency);
			if (exchangeRate == null) throw new NotFound();
			return Collections.singletonList(exchangeRate);
		} else if (fromCurrency != null)
			return data.getExchangeRatesFrom(fromCurrency);
		else if (toCurrency != null)
			return data.getExchangeRatesTo(toCurrency);
		else
			return data.getExchangeRates();
	}
}
