package banking;

public class HighCreditAccount extends Account {
    private static final long serialVersionUID = 1L;

    private String creditRating;

    public HighCreditAccount(String accountNumber, String ownerName, int balance, int interestRate, String creditRating) {
        super(accountNumber, ownerName, balance, interestRate);
        this.creditRating = creditRating;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    @Override
    public void withdraw(int amount) {
        setBalance(getBalance() - amount);
    }

    @Override
    public void deposit(int amount) {
        int basicInterest = (int) (getBalance() * (getInterestRate() / 100.0));
        int additionalInterest = 0;

        if (creditRating.equals("A")) {
            additionalInterest = (int) (getBalance() * (0.07));
        } else if (creditRating.equals("B")) {
            additionalInterest = (int) (getBalance() * (0.04));
        } else if (creditRating.equals("C")) {
            additionalInterest = (int) (getBalance() * (0.02));
        }

        setBalance(getBalance() + amount + basicInterest + additionalInterest);
    }

    @Override
    public void showAccountInfo() {
        super.showAccountInfo();
        System.out.println("신용등급: " + creditRating);
    }
}
