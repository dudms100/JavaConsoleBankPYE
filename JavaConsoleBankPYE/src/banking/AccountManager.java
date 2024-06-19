package banking;

import java.io.*;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class AccountManager {
    private static Set<Account> accountSet = new HashSet<>();
    public static Scanner scanner = new Scanner(System.in); // Changed to public static

    // File name for serialization
    private static final String FILE_NAME = "src/banking/AccountInfo.obj";

    // Menu constants
    private static final int MAKE = 1;
    private static final int DEPOSIT = 2;
    private static final int WITHDRAW = 3;
    private static final int INQUIRE = 4;
    private static final int DELETE = 5;
    private static final int EXIT = 6;

    public AccountManager() {
        loadAccounts();
        registerShutdownHook();
    }

    public void runMenu() {
        while (true) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case MAKE:
                    makeAccount();
                    break;
                case DEPOSIT:
                    depositMoney();
                    break;
                case WITHDRAW:
                    withdrawMoney();
                    break;
                case INQUIRE:
                    showAccInfo();
                    break;
                case DELETE:
                    deleteAccount();
                    break;
                case EXIT:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }

    private void showMenu() {
        System.out.println("-----Menu------");
        System.out.println("1.계좌개설");
        System.out.println("2.입    금");
        System.out.println("3.출    금");
        System.out.println("4.계좌정보출력");
        System.out.println("5.계좌정보삭제");
        System.out.println("6.프로그램종료");
        System.out.print("선택: ");
    }

    private int getChoice() {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
            if (choice < 1 || choice > 6) {
                throw new IllegalArgumentException("잘못된 메뉴 선택입니다.");
            }
        } catch (InputMismatchException e) {
            System.out.println("숫자를 입력해야 합니다.");
            scanner.nextLine(); // Clear the buffer
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return choice;
    }

    private void makeAccount() {
        System.out.println("***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.print("선택: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        if (type != 1 && type != 2) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

        // Check if the account number already exists
        Account existingAccount = findAccount(accountNumber);
        if (existingAccount != null) {
            System.out.print("중복계좌발견됨. 덮어쓸까요?(y or n): ");
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("n")) {
                System.out.println("기존의 정보를 유지합니다.");
                return;
            } else {
                accountSet.remove(existingAccount);
            }
        }

        System.out.print("고객이름: ");
        String ownerName = scanner.nextLine();
        System.out.print("잔고: ");
        int balance = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        System.out.print("기본이자%(정수형태로입력): ");
        int interestRate = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        if (type == 1) {
            NormalAccount account = new NormalAccount(accountNumber, ownerName, balance, interestRate);
            accountSet.add(account);
            System.out.println("계좌계설이 완료되었습니다.");
        } else if (type == 2) {
            System.out.print("신용등급(A,B,C등급): ");
            String creditRating = scanner.nextLine();
            HighCreditAccount account = new HighCreditAccount(accountNumber, ownerName, balance, interestRate, creditRating);
            accountSet.add(account);
            System.out.println("계좌계설이 완료되었습니다.");
        }
    }

    private void depositMoney() {
        System.out.println("***입    금***");
        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

        try {
            System.out.print("입금액: ");
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            if (amount <= 0) {
                throw new IllegalArgumentException("입금액은 양수만 가능합니다.");
            }

            if (amount % 500 != 0) {
                throw new IllegalArgumentException("입금액은 500원 단위로 가능합니다.");
            }

            Account account = findAccount(accountNumber);
            if (account == null) {
                System.out.println("해당 계좌를 찾을 수 없습니다.");
                return;
            }

            account.deposit(amount);
            System.out.println("입금이 완료되었습니다.");
        } catch (InputMismatchException e) {
            System.out.println("숫자를 입력해야 합니다.");
            scanner.nextLine(); // Clear the buffer
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void withdrawMoney() {
        System.out.println("***출    금***");
        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

        try {
            System.out.print("출금액: ");
            int amount = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            if (amount <= 0) {
                throw new IllegalArgumentException("출금액은 양수만 가능합니다.");
            }

            if (amount % 1000 != 0) {
                throw new IllegalArgumentException("출금액은 1000원 단위로 가능합니다.");
            }

            Account account = findAccount(accountNumber);
            if (account == null) {
                System.out.println("해당 계좌를 찾을 수 없습니다.");
                return;
            }

            if (account.getBalance() < amount) {
                System.out.print("잔고보다 많은 금액을 출금하려고 합니다. 전액 출금하시겠습니까? (y/n): ");
                String answer = scanner.nextLine();
                if (answer.equalsIgnoreCase("y")) {
                    account.withdraw(account.getBalance());
                } else {
                    System.out.println("출금이 취소되었습니다.");
                }
            } else {
                account.withdraw(amount);
                System.out.println("출금이 완료되었습니다.");
            }
        } catch (InputMismatchException e) {
            System.out.println("숫자를 입력해야 합니다.");
            scanner.nextLine(); // Clear the buffer
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAccInfo() {
        System.out.println("***계좌정보출력***");
        for (Account account : accountSet) {
            account.showAccountInfo();
            System.out.println("-------------");
        }
        System.out.println("전체계좌정보 출력이 완료되었습니다.");
    }

    private void deleteAccount() {
        System.out.println("***계좌정보삭제***");
        System.out.print("삭제할 계좌번호: ");
        String accountNumber = scanner.nextLine();

        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("해당 계좌를 찾을 수 없습니다.");
            return;
        }

        System.out.print("정말로 삭제하시겠습니까? (y/n): ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("y")) {
            accountSet.remove(account);
            System.out.println("계좌정보가 삭제되었습니다.");
        } else {
            System.out.println("계좌정보 삭제가 취소되었습니다.");
        }
    }

    private Account findAccount(String accountNumber) {
        for (Account account : accountSet) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accountSet = (HashSet<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("저장된 파일을 찾을 수 없습니다. 새로운 파일을 생성합니다.");
            saveAccounts(); // Create a new file if it doesn't exist
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일을 읽어오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accountSet);
            System.out.println("계좌 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("파일에 계좌 정보를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveAccounts();
        }));
    }

    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        manager.runMenu();
    }
}
