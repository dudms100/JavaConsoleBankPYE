package banking;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class AutoSaver extends Thread {
    private Set<Account> accountSet;
    private boolean isRunning = true;

    public AutoSaver(Set<Account> accountSet) {
        this.accountSet = accountSet;
        setDaemon(true); // 데몬 쓰레드로 설정
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                saveAccountsToFile();
                Thread.sleep(5000); // 5초 대기
            } catch (InterruptedException e) {
                System.out.println("자동 저장이 중지되었습니다.");
                return;
            }
        }
    }

    public void stopSaving() {
        isRunning = false;
    }

    private void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AutoSaveAccount.txt"))) {
            for (Account account : accountSet) {
                writer.write(account.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("자동 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
