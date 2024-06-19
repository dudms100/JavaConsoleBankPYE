package banking;

import java.io.Serializable;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private String ownerName;
    private int balance;
    private int interestRate;

    public Account(String accountNumber, String ownerName, int balance, int interestRate) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    public abstract void withdraw(int amount);

    public void deposit(int amount) {
        int interest = (int) (balance * (interestRate / 100.0));
        balance += (amount + interest);
    }

    public void showAccountInfo() {
        System.out.println("계좌번호: " + accountNumber);
        System.out.println("고객이름: " + ownerName);
        System.out.println("잔고: " + balance);
        System.out.println("기본이자%: " + interestRate);
    }
}
