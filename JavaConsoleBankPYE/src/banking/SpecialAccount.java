package banking;

// SpecialAccount 클래스는 NormalAccount 클래스를 상속받아 특수 입금 규칙을 적용하는 계좌 클래스입니다.
public class SpecialAccount extends NormalAccount {
    private static final long serialVersionUID = 3L;

    private int depositCount; // 입금 횟수를 저장할 변수

    // SpecialAccount 생성자
    public SpecialAccount(String accountNumber, String ownerName, int balance, int interestRate) {
        super(accountNumber, ownerName, balance, interestRate);
        this.depositCount = 0;
    }

    // 입금 메서드 오버라이드
    @Override
    public void deposit(int amount) {
        depositCount++;

        int basicInterest = (int) (getBalance() * (getInterestRate() / 100.0)); // 기본 이자 계산

        if (depositCount % 2 == 0) { // 짝수 번째 입금
            setBalance(getBalance() + basicInterest + amount + 500);
        } else { // 홀수 번째 입금
            setBalance(getBalance() + basicInterest + amount);
        }
    }

    // 계좌 정보 출력 메서드 오버라이드
    @Override
    public void showAccountInfo() {
        System.out.println("*** 특판계좌 정보 ***");
        System.out.println("계좌번호: " + getAccountNumber());
        System.out.println("예금주: " + getOwnerName());
        System.out.println("잔액: " + getBalance());
        System.out.println("기본이자: " + getInterestRate() + "%");
        System.out.println("입금 횟수: " + depositCount);
    }

    // 자동 저장을 위해 계좌 정보를 문자열로 반환하는 메서드 오버라이드
    @Override
    public String toStringForAutoSave() {
        return String.format("%s,%s,%d,%d,%d", getAccountNumber(), getOwnerName(), getBalance(), getInterestRate(), depositCount);
    }
}
