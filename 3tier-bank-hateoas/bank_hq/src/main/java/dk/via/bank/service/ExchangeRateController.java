package dk.via.bank.service;

import dk.via.bank.data.ExchangeRateDataService;
import dk.via.bank.model.ExchangeRate;
import dk.via.bank.service.exception.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/exchange_rate")
public class ExchangeRateController {
	private final ExchangeRateDataService data;

	public ExchangeRateController(@Autowired ExchangeRateDataService data) {
		this.data = data;
	}

	private EntityModel<ExchangeRate> createEntity(ExchangeRate rate) throws SQLException {
		return EntityModel.of(rate,
				linkTo(methodOn(getClass()).getRates(rate.getFromCurrency(), rate.getToCurrency())).withSelfRel());
	}

	private CollectionModel<EntityModel<ExchangeRate>> createCollection(List<ExchangeRate> rates) throws SQLException {
		List<EntityModel<ExchangeRate>> rateModels = new ArrayList<>();
		for(ExchangeRate rate: rates) {
            rateModels.add(createEntity(rate));
        }
		return CollectionModel.of(rateModels, linkTo(methodOn(getClass()).getRates(null, null)).withSelfRel());
	}

	@GetMapping
	public CollectionModel<EntityModel<ExchangeRate>> getRates(
			@RequestParam(value = "fromCurrency", required = false) String fromCurrency,
			@RequestParam(value = "toCurrency", required = false) String toCurrency) throws SQLException {
		return createCollection(getExchangeRateList(fromCurrency, toCurrency));
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
