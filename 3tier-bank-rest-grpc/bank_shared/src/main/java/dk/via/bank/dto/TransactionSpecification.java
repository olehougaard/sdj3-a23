package dk.via.bank.dto;

import dk.via.bank.model.Account;
import dk.via.bank.model.Money;
import dk.via.bank.model.transaction.*;

@SuppressWarnings("unused")
public class TransactionSpecification {
    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAW = "withdraw";
    public static final String TRANSFER = "transfer";

    private Integer id;
    private String type;
    private Money amount;
    private String text;
    private Account recipient;

    public TransactionSpecification() {
    }

    private TransactionSpecification(Integer id, String type, Money amount, String text, Account recipient) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.text = text;
        this.recipient = recipient;
    }

    public static TransactionSpecification deposit(Integer id, Money amount, String text) {
        return new TransactionSpecification(id, DEPOSIT, amount, text, null);
    }

    public static TransactionSpecification withdraw(Integer id, Money amount, String text) {
        return new TransactionSpecification(id, WITHDRAW, amount, text, null);
    }

    public static TransactionSpecification transfer(Integer id, Money amount, String text, Account recipient) {
        return new TransactionSpecification(id, TRANSFER, amount, text, recipient);
    }

    public static TransactionSpecification from(Transaction transaction) {
        if (transaction instanceof DepositTransaction)
            return deposit(transaction.getId(), transaction.getAmount(), transaction.getText());
        else if (transaction instanceof WithdrawTransaction)
            return withdraw(transaction.getId(), transaction.getAmount(), transaction.getText());
        else if (transaction instanceof TransferTransaction)
            return transfer(transaction.getId(), transaction.getAmount(), transaction.getText(), ((TransferTransaction) transaction).getRecipient());
        else
            throw new IllegalArgumentException("Unknown transaction type");
    }

    public Transaction toTransaction(Account account) {
        return switch (type) {
            case DEPOSIT -> new DepositTransaction(id, amount, account, text);
            case WITHDRAW -> new WithdrawTransaction(id, amount, account, text);
            case TRANSFER -> new TransferTransaction(id, amount, account, recipient, text);
            default -> throw new IllegalStateException("Not a valid transaction: " + type);
        };
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getRecipient() {
        return recipient;
    }

    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }
}
