package banking;

// NormalAccount 클래스는 Account 클래스를 상속받아 보통 계좌의 기능을 구현한 계좌 클래스입니다.
public class NormalAccount extends Account {
    private static final long serialVersionUID = 2L;

    // NormalAccount 생성자
    public NormalAccount(String accountNumber, String ownerName, int balance, int interestRate) {
        super(accountNumber, ownerName, balance, interestRate);
    }

    // 입금 메서드 오버라이드
    @Override
    public void deposit(int amount) {
        int basicInterest = (int) (getBalance() * (getInterestRate() / 100.0)); // 기본 이자 계산

        int totalBalance = getBalance() + basicInterest + amount;
        setBalance(totalBalance);
    }

    // 출금 메서드 오버라이드
    @Override
    public void withdraw(int amount) {
        setBalance(getBalance() - amount);
    }

    // 계좌 정보 출력 메서드 오버라이드
    @Override
    public void showAccountInfo() {
        System.out.println("계좌번호: " + getAccountNumber());
        System.out.println("예금주: " + getOwnerName());
        System.out.println("잔액: " + getBalance());
        System.out.println("기본이자: " + getInterestRate() + "%");
    }

    // 자동 저장을 위해 계좌 정보를 문자열로 반환하는 메서드 오버라이드
    @Override
    public String toStringForAutoSave() {
        return String.format("%s,%s,%d,%d,null", getAccountNumber(), getOwnerName(), getBalance(), getInterestRate());
    }
}
