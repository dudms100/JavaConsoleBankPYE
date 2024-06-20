package banking;

import java.io.*;
import java.util.*;

// AccountManager 클래스는 계좌 관리를 위한 기능들을 제공합니다.
public class AccountManager {
    // 계좌 정보를 저장할 Set
    private static Set<Account> accountSet = new HashSet<>();
    
    // 사용자 입력을 받기 위한 Scanner 객체
    public static Scanner scanner = new Scanner(System.in);

    // 파일 경로 상수 정의
    private static final String FILE_NAME = "src/banking/AccountInfo.obj"; // 계좌 정보 파일
    private static final String AUTO_SAVE_FILE_NAME = "src/banking/AutoSaveAccount.txt"; // 자동 저장 파일

    // 자동 저장을 위한 쓰레드
    private AutoSaver autoSaverThread;

    // AccountManager 생성자
    public AccountManager() {
        loadAccounts(); // 기존 계좌 정보 불러오기
        loadAutoSaveAccounts(); // 자동 저장된 계좌 정보 불러오기
        registerShutdownHook(); // 프로그램 종료 시 자동 저장 쓰레드 등록
    }

    // 메뉴 실행 메서드
    public void runMenu() {
        while (true) {
            showMenu(); // 메뉴 출력
            int choice = getChoice(); // 선택지 입력 받기
            switch (choice) {
                case 1:
                    makeAccount(); // 계좌 개설
                    break;
                case 2:
                    depositMoney(); // 입금
                    break;
                case 3:
                    withdrawMoney(); // 출금
                    break;
                case 4:
                    showAccInfo(); // 계좌 정보 출력
                    break;
                case 5:
                    deleteAccount(); // 계좌 삭제
                    break;
                case 6:
                    runAutoSaveOption(); // 자동 저장 옵션 설정
                    break;
                case 7:
                    saveAccounts(); // 계좌 정보 저장
                    if (autoSaverThread != null && autoSaverThread.isAlive()) {
                        autoSaverThread.interrupt();
                    }
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }

    // 메뉴 출력 메서드
    private void showMenu() {
        System.out.println("-----Menu------");
        System.out.println("1.계좌개설");
        System.out.println("2.입    금");
        System.out.println("3.출    금");
        System.out.println("4.계좌정보출력");
        System.out.println("5.계좌정보삭제");
        System.out.println("6.저장옵션");
        System.out.println("7.프로그램종료");
        System.out.print("선택: ");
    }

    // 선택지 입력 받는 메서드
    private int getChoice() {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 비우기
            if (choice < 1 || choice > 7) {
                throw new IllegalArgumentException("잘못된 메뉴 선택입니다.");
            }
        } catch (InputMismatchException e) {
            System.out.println("숫자를 입력해야 합니다.");
            scanner.nextLine(); // 입력 버퍼 비우기
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return choice;
    }

    // 계좌 개설 메서드
    private void makeAccount() {
        System.out.println("***신규계좌개설***");
        System.out.println("-----계좌선택------");
        System.out.println("1.보통계좌");
        System.out.println("2.신용신뢰계좌");
        System.out.println("3.특판계좌");
        System.out.print("선택: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // 입력 버퍼 비우기
        if (type < 1 || type > 3) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

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
        scanner.nextLine(); // 입력 버퍼 비우기
        System.out.print("기본이자%(정수형태로입력): ");
        int interestRate = scanner.nextInt();
        scanner.nextLine(); // 입력 버퍼 비우기

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
        } else if (type == 3) {
            SpecialAccount account = new SpecialAccount(accountNumber, ownerName, balance, interestRate);
            accountSet.add(account);
            System.out.println("특판계좌 계설이 완료되었습니다.");
        }
    }

    // 입금 메서드
    private void depositMoney() {
        System.out.println("***입    금***");
        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

        try {
            System.out.print("입금액: ");
            int amount = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 비우기

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
            scanner.nextLine(); // 입력 버퍼 비우기
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 출금 메서드
    private void withdrawMoney() {
        System.out.println("***출    금***");
        System.out.print("계좌번호: ");
        String accountNumber = scanner.nextLine();

        try {
            System.out.print("출금액: ");
            int amount = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 비우기

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
            scanner.nextLine(); // 입력 버퍼 비우기
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 계좌 정보 출력 메서드
    private void showAccInfo() {
        System.out.println("***계좌정보출력***");
        for (Account account : accountSet) {
            account.showAccountInfo();
            System.out.println("-------------");
        }
        System.out.println("전체계좌정보 출력이 완료되었습니다.");
    }

    // 계좌 삭제 메서드
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

    // 계좌 찾기 메서드
    private Account findAccount(String accountNumber) {
        for (Account account : accountSet) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    // 파일로부터 계좌 정보 불러오기
    private void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accountSet = (HashSet<Account>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("저장된 파일을 찾을 수 없습니다. 새로운 파일을 생성합니다.");
            saveAccounts(); // 파일이 없으면 새 파일 생성
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일을 읽어오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 계좌 정보 저장하기
    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accountSet);
            System.out.println("계좌 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("파일에 계좌 정보를 저장하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 프로그램 종료 시 자동 저장 쓰레드 등록
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveAutoSaveAccounts();
        }));
    }

    // 자동 저장 옵션 실행 메서드
    private void runAutoSaveOption() {
        System.out.println("***저장옵션***");
        System.out.println("1. 자동저장 On");
        System.out.println("2. 자동저장 Off");
        System.out.print("선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 입력 버퍼 비우기

        switch (choice) {
            case 1:
                manageAutoSave(true); // 자동 저장 켜기
                break;
            case 2:
                manageAutoSave(false); // 자동 저장 끄기
                break;
            default:
                System.out.println("잘못된 입력입니다. 자동저장 옵션을 다시 설정하세요.");
                break;
        }
    }

    // 자동 저장 관리 메서드
    private void manageAutoSave(boolean enable) {
        if (enable) {
            if (autoSaverThread == null || !autoSaverThread.isAlive()) {
                autoSaverThread = new AutoSaver();
                autoSaverThread.start();
                System.out.println("자동저장이 시작되었습니다.");
            } else {
                System.out.println("이미 자동저장이 실행중입니다.");
            }
        } else {
            if (autoSaverThread != null && autoSaverThread.isAlive()) {
                autoSaverThread.interrupt();
                autoSaverThread = null;
                System.out.println("자동저장이 중지되었습니다.");
            } else {
                System.out.println("현재 자동저장이 실행중이지 않습니다.");
            }
        }
    }

    // 자동 저장된 계좌 정보 불러오기
    private void loadAutoSaveAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(AUTO_SAVE_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) {
                    System.out.println("잘못된 형식의 데이터입니다: " + line);
                    continue;
                }
                String accountNumber = parts[0];
                String ownerName = parts[1];
                int balance = Integer.parseInt(parts[2]);
                int interestRate = Integer.parseInt(parts[3]);
                String creditRating = parts[4];

                Account account = findAccount(accountNumber);
                if (account == null) {
                    if (creditRating.equals("null")) {
                        account = new NormalAccount(accountNumber, ownerName, balance, interestRate);
                    } else {
                        account = new HighCreditAccount(accountNumber, ownerName, balance, interestRate, creditRating);
                    }
                    accountSet.add(account);
                } else {
                    // 이미 존재하는 계좌를 찾았을 때, 정보를 덮어씌웁니다.
                    if (account instanceof NormalAccount) {
                        ((NormalAccount) account).setOwnerName(ownerName);
                        ((NormalAccount) account).setBalance(balance);
                        ((NormalAccount) account).setInterestRate(interestRate);
                    } else if (account instanceof HighCreditAccount) {
                        ((HighCreditAccount) account).setOwnerName(ownerName);
                        ((HighCreditAccount) account).setBalance(balance);
                        ((HighCreditAccount) account).setInterestRate(interestRate);
                        ((HighCreditAccount) account).setCreditRating(creditRating);
                    } else if (account instanceof SpecialAccount) {
                        ((SpecialAccount) account).setOwnerName(ownerName);
                        ((SpecialAccount) account).setBalance(balance);
                        ((SpecialAccount) account).setInterestRate(interestRate);
                    } else {
                        System.out.println("Unknown account type: " + account.getClass());
                    }
                    System.out.println("기존 계좌 정보를 덮어씌웁니다: " + accountNumber);
                }
            }
            System.out.println("자동저장된 계좌 정보를 불러왔습니다.");
        } catch (FileNotFoundException e) {
            System.out.println("저장된 파일을 찾을 수 없습니다. 새로운 파일을 생성합니다.");
        } catch (IOException e) {
            System.out.println("자동저장된 계좌 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 자동 저장된 계좌 정보 저장하기
    private void saveAutoSaveAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUTO_SAVE_FILE_NAME))) {
            for (Account account : accountSet) {
                writer.write(account.toStringForAutoSave());
                writer.newLine();
            }
            System.out.println("자동저장이 완료되었습니다.");
        } catch (IOException e) {
            System.out.println("자동저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 메인 메서드
    public static void main(String[] args) {
        AccountManager manager = new AccountManager();
        manager.runMenu();
    }

    // 자동 저장 쓰레드 클래스
    private class AutoSaver extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(5000); // 5초마다 자동 저장
                    saveAutoSaveAccounts();
                } catch (InterruptedException e) {
                    System.out.println("자동저장 쓰레드가 종료되었습니다.");
                    return;
                }
            }
        }
    }
}
