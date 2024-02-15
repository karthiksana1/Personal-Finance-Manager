import java.util.ArrayList;

public class FinanceManager {

    ArrayList<Transaction> transactions = new ArrayList<>();

    public void addTransaction(TransactionType type, String category, double amount, String date) {
        if (amount < 0) {
            System.out.println("Amount cannot be negative. Transaction not added.");
            return;
        }

        // Validate date format or use a dedicated method for date validation.

        Transaction transaction = new Transaction(type, category, amount, date);
        transactions.add(transaction);
        System.out.println("Transaction added successfully!");
    }

    public void showSummary() {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction transaction : transactions) {
            if (TransactionType.INCOME.equals(transaction.getType())) {
                totalIncome += transaction.getAmount();
            } else if (TransactionType.EXPENSE.equals(transaction.getType())) {
                totalExpenses += transaction.getAmount();
            }
        }

        double netIncome = totalIncome - totalExpenses;

        System.out.println("\nFinancial Summary:");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenses);
        System.out.println("Net Income: " + netIncome);
    }

    public static class Transaction {
        private TransactionType type;
        private String category;
        private double amount;
        private String date;

        public Transaction(TransactionType type, String category, double amount, String date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        public TransactionType getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }
    }

    public enum TransactionType {
        INCOME, EXPENSE
    }
}
