package banking;

import java.io.Serializable;

// 계좌 정보를 저장하는 추상 클래스
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;  // 계좌번호
    private String ownerName;      // 고객명
    private int balance;           // 잔고
    private int interestRate;      // 기본 이자율

    // Account 클래스 생성자
    public Account(String accountNumber, String ownerName, int balance, int interestRate) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    // 계좌번호 getter 및 setter
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // 고객명 getter 및 setter
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    // 잔고 getter 및 setter
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    // 기본 이자율 getter 및 setter
    public int getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(int interestRate) {
        this.interestRate = interestRate;
    }

    // 입금 기능을 정의하는 추상 메서드
    public abstract void deposit(int amount);

    // 출금 기능을 정의하는 추상 메서드
    public abstract void withdraw(int amount);

    // 계좌 정보를 출력하는 추상 메서드
    public abstract void showAccountInfo();

    // 자동 저장을 위해 계좌 정보를 문자열로 변환하는 메서드
    public String toStringForAutoSave() {
        return String.format("%s,%s,%d,%d,null", accountNumber, ownerName, balance, interestRate);
    }
}
