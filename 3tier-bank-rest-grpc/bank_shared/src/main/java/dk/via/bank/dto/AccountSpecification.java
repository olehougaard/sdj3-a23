package dk.via.bank.dto;

public class AccountSpecification {
    private int regNumber;
    private String currency;
    private String cpr;

    public AccountSpecification() {
    }

    public AccountSpecification(int regNumber, String currency, String cpr) {
        this.regNumber = regNumber;
        this.currency = currency;
        this.cpr = cpr;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public int getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(int regNumber) {
        this.regNumber = regNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
