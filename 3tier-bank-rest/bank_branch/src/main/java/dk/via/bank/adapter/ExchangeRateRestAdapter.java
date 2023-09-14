package dk.via.bank.adapter;

import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.model.ExchangeRate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.rmi.RemoteException;

public class ExchangeRateRestAdapter implements ExchangeRateData {
    private final String endpoint;
    private final RestTemplate restTemplate;

    public ExchangeRateRestAdapter(String endpoint) {
        this.endpoint = endpoint + "exchange_rate";
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            builder.queryParam("fromCurrency", fromCurrency);
            builder.queryParam("toCurrency", toCurrency);
            ExchangeRate[] rates = restTemplate.getForObject(builder.toUriString(), ExchangeRate[].class);
            if (rates == null || rates.length == 0) {
                throw new RemoteException(String.format("Exchange rate from %s to %s not found", fromCurrency, toCurrency));
            }
            return rates[0];
        } catch (RestClientException e) {
            throw new RemoteException(e.getMessage());
        }
    }
}
