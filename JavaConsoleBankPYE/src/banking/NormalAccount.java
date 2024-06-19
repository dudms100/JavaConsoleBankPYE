package banking;

public class NormalAccount extends Account {
    private static final long serialVersionUID = 1L;

    public NormalAccount(String accountNumber, String ownerName, int balance, int interestRate) {
        super(accountNumber, ownerName, balance, interestRate);
    }

    @Override
    public void withdraw(int amount) {
        setBalance(getBalance() - amount);
    }
}
