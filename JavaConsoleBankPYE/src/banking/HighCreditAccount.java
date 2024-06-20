package banking;

// HighCreditAccount 클래스는 Account 클래스를 상속받아 신용 등급에 따른 추가 이자를 적용하는 클래스입니다.
public class HighCreditAccount extends Account {
    private static final long serialVersionUID = 3L;

    private String creditRating; // 신용 등급

    // HighCreditAccount 생성자
    public HighCreditAccount(String accountNumber, String ownerName, int balance, int interestRate, String creditRating) {
        super(accountNumber, ownerName, balance, interestRate);
        this.creditRating = creditRating;
    }

    // 신용 등급 getter 및 setter
    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    // 입금 기능을 정의하는 메서드
    @Override
    public void deposit(int amount) {
        // 기본 이자 계산
        int basicInterest = (int) (getBalance() * (getInterestRate() / 100.0));
        int additionalInterest = 0;

        // 초기 계좌 개설 시에는 이자를 계산하지 않음
        if (getBalance() != 0) {
            switch (creditRating.toUpperCase()) {
                case "A":
                    additionalInterest = (int) (getBalance() * (7.0 / 100.0));
                    break;
                case "B":
                    additionalInterest = (int) (getBalance() * (4.0 / 100.0));
                    break;
                case "C":
                    additionalInterest = (int) (getBalance() * (2.0 / 100.0));
                    break;
                default:
                    break;
            }
        }

        // 총 잔액 계산
        int totalBalance = getBalance() + basicInterest + additionalInterest + amount;
        setBalance(totalBalance);
    }

    // 출금 기능을 정의하는 메서드
    @Override
    public void withdraw(int amount) {
        setBalance(getBalance() - amount);
    }

    // 계좌 정보 출력 메서드
    @Override
    public void showAccountInfo() {
        System.out.println("계좌번호: " + getAccountNumber());
        System.out.println("예금주: " + getOwnerName());
        System.out.println("잔액: " + getBalance());
        System.out.println("기본이자: " + getInterestRate() + "%");
        System.out.println("신용등급: " + getCreditRating());
    }

    // 자동 저장을 위해 계좌 정보를 문자열로 반환하는 메서드
    @Override
    public String toStringForAutoSave() {
        return String.format("%s,%s,%d,%d,%s", getAccountNumber(), getOwnerName(), getBalance(), getInterestRate(), getCreditRating());
    }
}
