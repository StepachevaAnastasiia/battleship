package battleship;

import java.util.Scanner;
class Player {
    String name;
    char[][] field = new char[10][10];
    char[][] fogField = new char[10][10];
    String[][] ships = {
            {"O", "", "", "", "", ""},
            {"O", "", "", "", ""},
            {"O", "", "", ""},
            {"O", "", "", ""},
            {"O", "", ""}
    };
    public Player(String name) {
        this.name = name;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = '~';
                fogField[i][j] = '~';
            }
        }
    }
}
public class Game {
    public static final int COLUMNS = 10;
    public static final int ROWS = 10;

    public static void printField(char[][] field) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < ROWS; i++) {
            System.out.printf("%c ", 'A' + i);
            for (int j = 0; j < COLUMNS; j++) {
                System.out.printf("%c ", field[i][j]);
            }
            System.out.println();
        }
    }

    public static boolean checkAround(int x, int y, char[][] field) {
        if (x < 9 && y < 9 && field[x + 1][y + 1] != '~') {
            return false;
        }
        if (x > 0 && y > 0 && field[x - 1][y - 1] != '~') {
            return false;
        }
        if (x < 9 && y > 0 && field[x + 1][y - 1] != '~') {
            return false;
        }
        if (x > 0 && y < 9 && field[x - 1][y + 1] != '~') {
            return false;
        }
        return true;
    }

    public static boolean placeShip(String[] userInput, int size, char[][] field, String[][] ships, int shipIndex) {
        char startLetter = userInput[0].charAt(0);
        char endLetter = userInput[1].charAt(0);
        boolean differentLetter = startLetter != endLetter;
        if (startLetter > 'J' || startLetter < 'A'
                || endLetter > 'J' || endLetter < 'A') {
            System.out.println("Error!"); //out of bounds for rows
            return false;
        }

        int startNumber = 0;
        if (userInput[0].length() == 2) {
            startNumber = Character.getNumericValue(userInput[0].charAt(1));
            if (startNumber < 1) {
                System.out.println("Error!");
                return false;
            }
        } else if (userInput[0].length() == 3 && userInput[0].charAt(1) == '1'
                && userInput[0].charAt(2) == '0') {
            startNumber = 10;
        } else {
            System.out.println("Error!");
            return false;
        }

        int endNumber = 0;
        if (userInput[1].length() == 2) {
            endNumber = Character.getNumericValue(userInput[1].charAt(1));
            if (endNumber < 1) {
                System.out.println("Error!");
                return false;
            }
        } else if (userInput[1].length() == 3 && userInput[1].charAt(1) == '1'
                && userInput[1].charAt(2) == '0') {
            endNumber = 10;
        } else {
            System.out.println("Error!");
            return false;
        }

        int length = 0;
        if (differentLetter && startNumber != endNumber) {
            System.out.println("Error!");
            return false;
        } else if (differentLetter) {
            length = Math.abs(startLetter - endLetter) + 1;
        } else {
            length = Math.abs(startNumber - endNumber) + 1;
        }

        if (length != size) {
            System.out.println("Error! Wrong length of the ship! Try again:");
            return false;
        }

        int point = 1;
        if (differentLetter) {
            if (startLetter < endLetter) {
                for (char i = startLetter; i <= endLetter; i++) {
                     if (!checkAround(i - 'A', startNumber - 1, field)) {
                         System.out.println("Error! You placed it too close to another one. Try again:");
                         return false;
                     }
                }
                //place ship on the field
                for (char i = startLetter; i <= endLetter; i++) {
                    field[i - 'A'][startNumber - 1] = 'O';
                    ships[shipIndex][point] = String.format("%c%d", i, startNumber);
                    point++;
                }
            }
            if (startLetter > endLetter) {
                for (char i = startLetter; i >= endLetter; i--) {
                    if (!checkAround(i - 'A', startNumber - 1, field)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
                //place ship
                for (char i = startLetter; i >= endLetter; i--) {
                    field[i - 'A'][startNumber - 1] = 'O';
                    ships[shipIndex][point] = String.format("%c%d", i, startNumber);
                    point++;
                }
            }
        } else {
            if (startNumber < endNumber) {
                for (int i = startNumber; i <= endNumber; i++) {
                    if (!checkAround(startLetter - 'A', i - 1, field)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
                //place ship
                for (int i = startNumber; i <= endNumber; i++) {
                    field[startLetter - 'A'][i - 1] = 'O';
                    ships[shipIndex][point] = String.format("%c%d", startLetter, i);
                    point++;
                }
            }
            if (startNumber > endNumber) {
                for (int i = startNumber; i >= endNumber; i--) {
                    if (!checkAround(startLetter - 'A', i - 1, field)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
                //place ship
                for (int i = startNumber; i >= endNumber; i--) {
                    field[startLetter - 'A'][i - 1] = 'O';
                    ships[shipIndex][point] = String.format("%c%d", startLetter, i);
                    point++;
                }
            }
        }
        return true;
    }

    public static void place(Player player) {
        Scanner scanner = new Scanner(System.in);
        String[][] shipsArray = {
                {"Aircraft Carrier", "5"},
                {"Battleship", "4"},
                {"Submarine", "3"},
                {"Cruiser", "3"},
                {"Destroyer", "2"}
        };

        for (int i = 0; i < shipsArray.length; i++) {
            int size = Integer.parseInt(shipsArray[i][1]);
            String[] userInput;
            System.out.printf("Enter the coordinates of the %s (%d cells):", shipsArray[i][0], size);

            do {
                userInput = scanner.nextLine().split(" ");
            } while (!placeShip(userInput, size, player.field, player.ships, i));

            printField(player.field);
        }
    }

    public static boolean takeShot(String shot, char[][] field, char[][] fogField, String[][] ships) {
        int x = shot.charAt(0) - 'A';
        int y;
        if (shot.length() == 2) {
            y = Character.getNumericValue(shot.charAt(1)) - 1;
        } else if (shot.length() == 3 && shot.charAt(1) == '1' && shot.charAt(2) == '0') {
            y = 9;
        } else {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }

        if (x > 9) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }

        if (field[x][y] == '~' || field[x][y] == 'M') {
            field[x][y] = 'M';
            fogField[x][y] = 'M';
            System.out.println("You missed!");
        } else if (field[x][y] == 'O' || field[x][y] == 'X') {
            if (field[x][y] == 'X') {
                System.out.println("You hit a ship! Try again:");
            }
            field[x][y] = 'X';
            fogField[x][y] = 'X';
            boolean isSink = true;
            for (int k = 0; k < ships.length; k++) {
                for (int j = 1; j < ships[k].length; j++) {
                    if (shot.equals(ships[k][j])) {
                        ships[k][j] = "X";
                        for (int t = 1; t < ships[k].length; t++) {
                            if (!"X".equals(ships[k][t])) {
                                isSink = false;
                            }
                        }
                        if (isSink) {
                            ships[k][0] = "X";
                            if (!allShipsSank(ships)) {
                                System.out.println("You sank a ship! Specify a new target:");
                            }

                        } else {
                            System.out.println("You hit a ship! Try again:");
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean allShipsSank(String[][] ships) {
        for (int i = 0; i < ships.length; i++) {
            if ("O".equals(ships[i][0])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        System.out.println("Player 1, place your ships on the game field");
        printField(player1.field);
        place(player1);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        System.out.println("Player 2, place your ships to the game field");
        printField(player2.field);
        place(player2);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        Player currentPlayer = player1;
        Player oppositePlayer = player2;
        do {
            printField(currentPlayer.fogField);
            System.out.println("---------------------");
            printField(currentPlayer.field);
            System.out.printf("%s, it's your turn:", currentPlayer.name);
            String shot;
            do {
                shot = scanner.nextLine();
            } while (!takeShot(shot, oppositePlayer.field, currentPlayer.fogField, oppositePlayer.ships));
            if (!allShipsSank(oppositePlayer.ships)){
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();
                if (currentPlayer.equals(player1)) {
                    currentPlayer = player2;
                    oppositePlayer = player1;
                } else {
                    currentPlayer = player1;
                    oppositePlayer = player2;
                }
            }

        } while (!allShipsSank(oppositePlayer.ships));

        System.out.println("You sank the last ship. You won. Congratulations!");
    }
}
