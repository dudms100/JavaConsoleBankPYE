package banking;

import java.util.Random;
import java.util.Scanner;

public class threeby3 {
    private char[][] board;
    private char[][] initialBoard;
    private int emptyRow;
    private int emptyCol;
    private Scanner scanner;
    private Random random;

    // 초기 퍼즐 설정
    public threeby3() {
        initialBoard = new char[][]{
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', 'X'}
        };
        resetBoard();
        scanner = new Scanner(System.in);
        random = new Random();
    }

    // 보드 리셋
    private void resetBoard() {
        board = new char[][]{
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', 'X'}
        };
        emptyRow = 2;
        emptyCol = 2;
    }

    // 퍼즐 게임 실행 메서드
    public void play() {
        randomizeBoard();
        while (true) {
            printBoard();
            if (isInitialState()) {
                System.out.print(" 클리어!. 게임을 다시 시작할까요? (y/n): ");
                char restart = scanner.next().charAt(0);
                if (restart == 'y' || restart == 'Y') {
                    resetBoard();
                    randomizeBoard();
                    continue;
                } else {
                    break;
                }
            }

            System.out.print("키를 입력해주세요: ");
            char key = scanner.next().charAt(0);

            if (key == 'x') {
                System.out.println("게임을 종료합니다.");
                break;
            }

            switch (key) {
                case 'a': moveLeft(); break;
                case 'd': moveRight(); break;
                case 'w': moveUp(); break;
                case 's': moveDown(); break;
                default: System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

    // 퍼즐 보드 출력 메서드
    private void printBoard() {
        System.out.println("3X3퍼즐게임");
        System.out.println("=====");
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
        System.out.println("=====");
        System.out.println("[이동] a:Left d:Right w:Up s:Down");
        System.out.println("[종료] x:Exit");
    }

    // 초기 상태와 비교 메서드
    private boolean isInitialState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != initialBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 퍼즐 무작위 이동 메서드
    private void randomizeBoard() {
        for (int i = 0; i < 100; i++) {
            int move = random.nextInt(4);
            switch (move) {
                case 0: moveLeft(false); break;
                case 1: moveRight(false); break;
                case 2: moveUp(false); break;
                case 3: moveDown(false); break;
            }
        }
    }

    // 퍼즐 조각 이동 메서드들
    private void moveLeft() {
        moveLeft(true);
    }

    private void moveRight() {
        moveRight(true);
    }

    private void moveUp() {
        moveUp(true);
    }

    private void moveDown() {
        moveDown(true);
    }

    private void moveLeft(boolean showMessage) {
        if (emptyCol < 2) {
            board[emptyRow][emptyCol] = board[emptyRow][emptyCol + 1];
            board[emptyRow][emptyCol + 1] = 'X';
            emptyCol++;
        } else if (showMessage) {
            System.out.println("--이동불가--");
        }
    }

    private void moveRight(boolean showMessage) {
        if (emptyCol > 0) {
            board[emptyRow][emptyCol] = board[emptyRow][emptyCol - 1];
            board[emptyRow][emptyCol - 1] = 'X';
            emptyCol--;
        } else if (showMessage) {
            System.out.println("--이동불가--");
        }
    }

    private void moveUp(boolean showMessage) {
        if (emptyRow < 2) {
            board[emptyRow][emptyCol] = board[emptyRow + 1][emptyCol];
            board[emptyRow + 1][emptyCol] = 'X';
            emptyRow++;
        } else if (showMessage) {
            System.out.println("--이동불가--");
        }
    }

    private void moveDown(boolean showMessage) {
        if (emptyRow > 0) {
            board[emptyRow][emptyCol] = board[emptyRow - 1][emptyCol];
            board[emptyRow - 1][emptyCol] = 'X';
            emptyRow--;
        } else if (showMessage) {
            System.out.println("--이동불가--");
        }
    }
}
