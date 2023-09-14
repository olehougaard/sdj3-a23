package dk.via.bank.data;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dk.via.bank.model.ExchangeRate;

public interface ExchangeRateData extends Remote {
	ExchangeRate getExchangeRate(String fromCurrency, String toCurrency) throws RemoteException;
}
