import java.util.Random;
import java.util.Scanner;

public class Liar extends Monster {
    Liar(int sizeBoard) {
        super(sizeBoard);
        setImage("\uD83D\uDC74");
    }

    @Override
    public boolean taskMonster(int difficultGame) {
        System.out.println("Помогите старику решить его задачу: ");

        Random random = new Random();
        int x = random.nextInt((difficultGame - 1), 10 * difficultGame);
        int y = random.nextInt((difficultGame - 1), 10 * difficultGame);
        int z = random.nextInt((difficultGame - 1), 10 * difficultGame);

        int trueAnswer = x * y - z;

        System.out.println("Реши пример: " + x + " * " + y + " - " + z + " = ?");

        Scanner scanner = new Scanner(System.in);
        int answer = scanner.nextInt();

        if (trueAnswer == answer) {
            System.out.println("Верно! Но ты проиграл монстру. Чтобы его победить надо ответить неправильно. Твое здоровье уменьшилось на 1 единицу");
            return false;
        } else {
            System.out.println("Ты его раскусил и выиграл эту битву!");
            return true;
        }
    }
}
