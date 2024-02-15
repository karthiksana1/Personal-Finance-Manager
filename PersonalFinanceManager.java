import java.util.InputMismatchException;
import java.util.Scanner;

public class PersonalFinanceManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FinanceManager financeManager = new FinanceManager();

        try {
            while (true) {
                System.out.println("\nPersonal Finance Manager");
                System.out.println("1. Add Transaction");
                System.out.println("2. Show Summary");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (choice) {
                        case 1:
                            // Adding a new transaction.
                            System.out.print("Enter Transaction Type (Income/Expense): ");
                            String typeInput = scanner.nextLine().trim();
                            FinanceManager.TransactionType type = getTransactionType(typeInput);

                            if (type == null) {
                                System.out.println("Invalid transaction type. Please enter 'Income' or 'Expense'.");
                                continue;
                            }

                            System.out.print("Enter Category: ");
                            String category = scanner.nextLine();
                            System.out.print("Enter Amount: ");
                            double amount = scanner.nextDouble();
                            scanner.nextLine(); // Consume the newline character
                            System.out.print("Enter Date: ");
                            String date = scanner.nextLine();

                            financeManager.addTransaction(type, category, amount, date);
                            break;

                        case 2:
                            // Displaying a summary of financial transactions.
                            financeManager.showSummary();
                            break;

                        case 3:
                            // Exiting the program.
                            System.out.println("Exiting the program. Thank you!");
                            System.exit(0);

                        default:
                            // Handling invalid user choices.
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    // Handling invalid numeric input.
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static FinanceManager.TransactionType getTransactionType(String input) {
        for (FinanceManager.TransactionType type : FinanceManager.TransactionType.values()) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }
        return null;
    }
}
