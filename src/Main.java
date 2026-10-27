
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String castle = "\uD83C\uDFF0";
        String tile = "⬜";

        System.out.println("Привет! Ты готов начать играть в игру? (Напиши: да или нет)");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();

        System.out.println("Ваш ответ:\t" + answer);

        switch (answer) {
            case "ДА", "да":
                System.out.println("Выбери сложность игры (от 1 до 5). От этого зависит количество монстров и жизней вашего героя");
                int difficultGame = scanner.nextInt();
                if (difficultGame < 6 && difficultGame > 0) {
                    scanner.nextLine();

                    int sizeBoard = 6;
                    int step = 0;

                    Person person = new Person(sizeBoard);
                    person.setLive(6 - difficultGame);

                    Random random = new Random();
                    String[][] board = new String[sizeBoard][sizeBoard];

                    int castleX = 1 + random.nextInt(sizeBoard);
                    int castleY = 1;

                    for (int y = 1; y <= sizeBoard; y++) {
                        for (int x = 1; x <= sizeBoard; x++) {
                            board[y - 1][x - 1] = "  ";
                        }
                    }
                    board[castleY - 1][castleX - 1] = castle;
                    int count = 0;
                    int countMonster = sizeBoard * sizeBoard - sizeBoard - 1 + (difficultGame - 6) * 2;
                    Monster[] monsters = new Monster[countMonster];

                    int bigMCount = 0;
                    int maxBigMCount = difficultGame;

                    while (bigMCount < maxBigMCount) {
                        Monster monster = new BigMonster(sizeBoard);
                        int row = monster.getY() - 1;
                        int column = monster.getX() - 1;

                        if (board[row][column].equals("  ")) {
                            board[row][column] = monster.getImage();
                            monsters[bigMCount] = monster;
                            bigMCount++;
                            count++;
                        }
                    }

                    while (count < countMonster + difficultGame - 5) {
                        Monster monster = new Monster(sizeBoard);
                        int row = monster.getY() - 1;
                        int column = monster.getX() - 1;

                        if (board[row][column].equals("  ")) {
                            board[row][column] = monster.getImage();
                            monsters[count] = monster;
                            count++;
                        }
                    }
                    while (count < countMonster) {
                        Monster monster = new Liar(sizeBoard);
                        int row = monster.getY() - 1;
                        int column = monster.getX() - 1;

                        if (board[row][column].equals("  ")) {
                            board[row][column] = monster.getImage();
                            monsters[count] = monster;
                            count++;
                        }
                    }
                    for (int y = 1; y <= sizeBoard; y++) {
                        for (int x = 1; x <= sizeBoard; x++) {
                            if (board[y - 1][x - 1].equals("  ")) {
                                board[y - 1][x - 1] = tile;
                            }
                        }
                    }

                    System.out.println("Выбранная сложность:\t" + difficultGame);
                    System.out.println("Никому не доверяй...");
                    while (true) {
                        board[person.getY() - 1][person.getX() - 1] = person.getImage();
                        outputBoard(board, person.getLive());

                        System.out.println("Чтобы управлять персонажем введите (w a s d/ц ф ы в) или напишите новую координату"
                            + " (ход возможен только по вертикали или горизонтали на одну клетку)"
                            + "\nКоординаты персонажа — (x: " + person.getX()
                            + ", y: " + person.getY() + ")");
                        String hod = scanner.nextLine();

                        int newX = person.getX();
                        int newY = person.getY();

                        if ((hod.equals("w")) || (hod.equals("ц"))) {
                            newY = person.getY() - 1;
                        } else if ((hod.equals("a")) || (hod.equals("ф"))) {
                            newX = person.getX() - 1;
                        } else if ((hod.equals("s")) || (hod.equals("ы"))) {
                            newY = person.getY() + 1;
                        } else if ((hod.equals("d")) || (hod.equals("в"))) {
                            newX = person.getX() + 1;
                        } else if (hod.length() == 2) {
                            String[] otvet = hod.split("\\s+");
                            newX = Integer.parseInt(otvet[0]);
                            newY = Integer.parseInt(otvet[1]);
                        }

                        if (person.isMoveCorrect(newX, newY)) {
                            if (board[newY - 1][newX - 1].equals(tile)) {
                                board[person.getY() - 1][person.getX() - 1] = tile;
                                person.move(newX, newY);
                                step++;

                                System.out.println("Ход корректный; новые координаты: "
                                    + person.getX() + ", " + person.getY()
                                    + "\nХод номер: " + step);
                            } else if (board[newY - 1][newX - 1].equals(castle)) {
                                System.out.println("Вы прошли игру!");
                                break;
                            } else {
                                for (Monster monster : monsters) {
                                    if (monster.conflictPerson(newX, newY)) {
                                        if (monster.taskMonster(difficultGame)) {
                                            board[person.getY() - 1][person.getX() - 1] = tile;
                                            person.move(newX, newY);
                                            step++;
                                            monster.reward(person);
                                        } else {
                                            person.downLive();
                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            System.out.println("Некорректный ход");
                        }
                        if (person.getLive() <= 0) {
                            break;
                        }
                    }

                    if (person.getLive() <= 0) {
                        System.out.println("Закончились жизни. Итог: ...");
                    }
                    break;
                }
                else{
                    System.out.println("Некорректное число");
                }
            case "НЕТ", "нет":
                System.out.println("Жаль, приходи ещё!");
                break;

            default:
                System.out.println("Данные введены некорректно");
                break;
        }
    }

    static void outputBoard(String[][] board, int live) {
        String leftBlock = "|" + " ";
        String rightBlock = "|";
        String wall = "+ ➖ + ➖ + ➖ + ➖ + ➖ + ➖ +";

        for (String[] row : board) {
            System.out.println(wall);
            for (String col : row) {
                System.out.print(leftBlock + col + " ");
            }
            System.out.println(rightBlock);
        }
        System.out.println(wall);

        System.out.println("Количество жизней:\t" + live + "\n");
    }
}
