package dk.via.bank;

import dk.via.bank.data.ExchangeRateData;
import dk.via.bank.model.ExchangeRate;
import dk.via.bank.model.Money;

import java.rmi.RemoteException;

public class BranchExchangeService {
    private final ExchangeRateData exchangeRateData;

    public BranchExchangeService(ExchangeRateData exchangeRateData) {
        this.exchangeRateData = exchangeRateData;
    }

    public Money exchange(Money amount, String targetCurrency) throws RemoteException {
        ExchangeRate exchangeRate = exchangeRateData.getExchangeRate(amount.getCurrency(), targetCurrency);
        return exchangeRate.exchange(amount);
    }
}
