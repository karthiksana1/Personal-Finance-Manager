// Class representing a financial transaction.
public class Transaction {
    String type; // Type of transaction ("Income" or "Expense").
    String category; // Category of the transaction.
    double amount; // Amount involved in the transaction.
    String date; // Date when the transaction occurred.

    // Constructor to initialize transaction details.
    public Transaction(String type, String category, double amount, String date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}
