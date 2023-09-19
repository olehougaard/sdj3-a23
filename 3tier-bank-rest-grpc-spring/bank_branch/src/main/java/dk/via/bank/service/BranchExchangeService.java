package dk.via.bank.service;

import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.model.ExchangeRate;
import dk.via.bank.model.Money;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Component
public class BranchExchangeService {
    private final ExchangeRateData exchangeRateData;

    public BranchExchangeService(ExchangeRateData exchangeRateData) {
        this.exchangeRateData = exchangeRateData;
    }

    public Money exchange(Money amount, String targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateData.getExchangeRate(amount.getCurrency(), targetCurrency);
        return exchangeRate.exchange(amount);
    }
}
