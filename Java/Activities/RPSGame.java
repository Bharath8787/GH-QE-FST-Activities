import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class RPSGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String[] choices = {"Rock", "Paper", "Scissors"};

        try {
            System.out.println("Enter your move (0 = Rock, 1 = Paper, 2 = Scissors): ");
            int playerChoice = scanner.nextInt();

            if (playerChoice < 0 || playerChoice > 2) {
                throw new IllegalArgumentException("Invalid input! You must enter 0, 1, or 2.");
            }

            int computerChoice = random.nextInt(3);

            System.out.println("You chose: " + choices[playerChoice]);
            System.out.println("Computer chose: " + choices[computerChoice]);

            if (playerChoice == computerChoice) {
                System.out.println("It's a draw!");
            } 
            else if (
                (playerChoice == 0 && computerChoice == 2) ||
                (playerChoice == 1 && computerChoice == 0) ||
                (playerChoice == 2 && computerChoice == 1)
            ) {
                System.out.println("You win!");
            } 
            else {
                System.out.println("Computer wins!");
            }

        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a number only!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }
}