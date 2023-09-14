package dk.via.bank.adapter;

import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.model.ExchangeRate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Objects;

public class ExchangeRateRestAdapter implements ExchangeRateData {
    private final String endpoint;

    public ExchangeRateRestAdapter(String endpoint) {
        this.endpoint = endpoint + "exchange_rate";
    }

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
        builder.queryParam("fromCurrency", fromCurrency);
        builder.queryParam("toCurrency", toCurrency);
        Traverson traverson = new Traverson(builder.build(0), MediaTypes.HAL_JSON);
        CollectionModel<ExchangeRate> exchangeRates = traverson.follow()
                .toObject(new TypeReferences.CollectionModelType<ExchangeRate>() {});
        if (exchangeRates == null || exchangeRates.getContent().size() != 1) {
            throw new RestClientException(String.format("Exchange rate from %s to %s not found", fromCurrency, toCurrency));
        }
        Collection<ExchangeRate> rates = exchangeRates.getContent();
        return rates.iterator().next();
    }
}
