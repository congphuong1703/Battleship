package battleship;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String FOG = "~";
    private static final String CELL = "O";
    private static final String HIT = "X";
    private static final String MISS = "M";
    private static final String[] SHIPS = {"Destroyer", "Cruiser", "Submarine", "Battleship", "Aircraft Carrier"};

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String[][] shipPlayer1 = initMap();
        String[][] shipPlayer2 = initMap();

        String[][] fogPlayer1 = initMap();
        String[][] fogPlayer2 = initMap();

        String[] pointShipPlayer1 = new String[10];
        String[] pointShipPlayer2 = new String[10];

        System.out.println("Player 1, place your ships on the game field");
        show(fogPlayer1);
        initShips(shipPlayer1, pointShipPlayer1);

        System.out.println("Player 2, place your ships to the game field");
        show(fogPlayer2);
        initShips(shipPlayer2, pointShipPlayer2);
        startGame(shipPlayer1, shipPlayer2, fogPlayer1, fogPlayer2, pointShipPlayer1, pointShipPlayer2);
    }

    private static void startGame(String[][] shipPlayer1, String[][] shipPlayer2, String[][] fogPlayer1, String[][] fogPlayer2,
                                  String[] pointShipPlayer1, String[] pointShipPlayer2) {
        for (int i = 1; ; i++) {
            if (i % 2 == 1) {
                show(fogPlayer1);
                System.out.println("---------------------");
                show(shipPlayer1);
                takeShot(shipPlayer2, fogPlayer2, pointShipPlayer2, 2);
            } else {
                show(fogPlayer2);
                System.out.println("---------------------");
                show(shipPlayer2);
                takeShot(shipPlayer1, fogPlayer1, pointShipPlayer1, 1);
            }
        }
    }

    private static void takeShot(String[][] arr, String[][] fogs, String[] pointShips, int player) {
        String shot;
        int asciiX1;
        int asciiX2;
        System.out.printf("Player %d, it's your turn:%n", player);
        boolean isWin = true;
        boolean isSank = false;
        boolean isHit = false;
        do {
            shot = scanner.next();

            asciiX1 = shot.charAt(0) - 65;
            asciiX2 = Integer.parseInt(shot.substring(1)) - 1;
            if (asciiX1 > 9 || asciiX1 < 0 || asciiX2 > 9 || asciiX2 < 0) {
                System.out.println("Error! Wrong length of the Submarine! Try again:");
            } else {
                break;
            }
        } while (true);
        for (int i = 0; i < pointShips.length; i++) {
            if (pointShips[i].equalsIgnoreCase(shot)) {
                pointShips[i] = FOG;
                if ((i % 2 == 0 && FOG.equalsIgnoreCase(pointShips[i + 1])) || (i % 2 != 0 && FOG.equalsIgnoreCase(pointShips[i - 1]))) {
                    isSank = true;
                }
            }
            if (!FOG.equalsIgnoreCase(pointShips[i])) {
                isWin = false;
            }
        }
        if (!FOG.equalsIgnoreCase(arr[asciiX1][asciiX2])) {
            arr[asciiX1][asciiX2] = HIT;
            isHit = true;
        } else {
            arr[asciiX1][asciiX2] = MISS;
        }
        if (isWin) {
            System.out.println("You sank the last ship. You won. Congratulations!");
            return;
        } else if (isSank) {
            System.out.println("You sank a ship! Specify a new target:");
        } else if (isHit) {
            System.out.println("You hit a ship! Try again:");
        } else {
            System.out.println("You missed. Try again:");
        }
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        scanner.nextLine();
    }

    private static String[][] initMap() {
        String[][] arr = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arr[i][j] = FOG;
            }
        }
        return arr;
    }

    private static void initShips(String[][] arr, String[] pointShips) throws IOException {
        int cell = 5;
        for (int i = 5; i > 0; i--) {
            if (i == 2) {
                cell = 3;
            }
            System.out.println("Enter the coordinates of " + SHIPS[i - 1] + "(" + cell + " cells):");
            do {
                String pointX = scanner.next();
                String pointY = scanner.next();
                try {
                    int asciiX1 = pointX.charAt(0) - 65;
                    int asciiX2 = Integer.parseInt(pointX.substring(1)) - 1;

                    int asciiY1 = pointY.charAt(0) - 65;
                    int asciiY2 = Integer.parseInt(pointY.substring(1)) - 1;

                    if (asciiX1 > 9 || asciiY1 > 9 || asciiX2 > 9 || asciiY2 > 9
                            || asciiX1 < 0 || asciiY1 < 0 || asciiX2 < 0 || asciiY2 < 0
                            || (Math.abs(asciiX1 - asciiY1 + asciiX2 - asciiY2) + 1 != cell)) {
                        System.out.println("Error! Wrong length of the Submarine! Try again:");
                    } else if (asciiX1 != asciiY1 && asciiX2 != asciiY2) {
                        System.out.println("Error! Wrong ship location! Try again:");
                    } else {
                        boolean isClose = false;
                        for (int j = Math.min(asciiY1, asciiX1); j <= Math.max(asciiY1, asciiX1); j++) {
                            for (int k = Math.min(asciiY2, asciiX2); k <= Math.max(asciiY2, asciiX2); k++) {
                                if (!arr[j][k].equalsIgnoreCase(FOG)
                                        || (j > 0 && !arr[j - 1][k].equalsIgnoreCase(FOG))
                                        || (j < 9 && !arr[j + 1][k].equalsIgnoreCase(FOG))
                                        || (k < 9 && !arr[j][k + 1].equalsIgnoreCase(FOG))
                                        || (k > 0 && !arr[j][k - 1].equalsIgnoreCase(FOG))) {
                                    isClose = true;
                                    break;
                                }
                            }
                            if (isClose) {
                                break;
                            }
                        }
                        if (isClose) {
                            System.out.println("Error! You placed it too close to another one. Try again:");
                        } else {
                            pointShips[i * 2 - 1] = pointX;
                            pointShips[i * 2 - 2] = pointY;
                            for (int j = Math.min(asciiY1, asciiX1); j <= Math.max(asciiY1, asciiX1); j++) {
                                for (int k = Math.min(asciiY2, asciiX2); k <= Math.max(asciiY2, asciiX2); k++) {
                                    arr[j][k] = CELL;
                                }
                            }
                            cell--;
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error! Wrong length of the Submarine! Try again:");
                }
            } while (true);
            show(arr);
        }
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void show(String[][] arr) {
        int characterAscii = 65;
        System.out.print(" ");
        for (int i = 0; i < 10; i++) {
            System.out.print(" " + (i + 1));
        }
        for (int i = 0; i < 10; i++) {
            System.out.println();
            System.out.print(String.valueOf(Character.toChars(characterAscii + i)) + " ");
            for (int j = 0; j < 10; j++) {
                System.out.print(arr[i][j] + " ");
            }
        }
        System.out.println();
    }
}
