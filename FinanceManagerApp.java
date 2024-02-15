package com.karthik.financeapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FinanceManagerApp extends Application {

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private PieChart pieChart = new PieChart(pieChartData);
    private Label totalExpensesLabel = new Label("Total Expenses: 0.0");
    private Label totalMoneyLeftLabel = new Label("Total Money Left: 0.0");

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Finance Manager App");

        VBox root = new VBox();
        root.setSpacing(10);

        ChoiceBox<String> typeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList("Income", "Expense"));
        typeChoiceBox.setTooltip(new Tooltip("Select Type"));

        TextField categoryField = new TextField();
        TextField amountField = new TextField();
        DatePicker datepicker = new DatePicker();
        datepicker.setConverter(new StringConverter<>() {
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });

        Button addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(event -> {
            String type = typeChoiceBox.getValue();
            String category = categoryField.getText();

            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid Amount", "Please enter a valid numeric amount.");
                return;
            }

            LocalDate date = datepicker.getValue();

            Transaction newTransaction = new Transaction(type, category, amount, date);

            transactions.add(newTransaction);

            updatePieChart();
            updateSummaryLabels();

            typeChoiceBox.getSelectionModel().clearSelection();
            categoryField.clear();
            amountField.clear();
            datepicker.setValue(null);
        });

        TableView<Transaction> tableView = new TableView<>();
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        TableColumn<Transaction, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.getColumns().addAll(typeColumn, categoryColumn, amountColumn, dateColumn);
        tableView.setItems(transactions);

        root.getChildren().addAll(
                new Label("Add New Transaction:"),
                new Label("Type:"),
                typeChoiceBox,
                new Label("Category:"),
                categoryField,
                new Label("Amount:"),
                amountField,
                new Label("Date:"),
                datepicker,
                addTransactionButton,
                new Label("Transaction History:"),
                tableView,
                new Label("Expense Summary:"),
                pieChart,
                totalExpensesLabel,
                totalMoneyLeftLabel
        );

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void updatePieChart() {
        pieChartData.clear();

        Map<String, Double> categoryExpenses = new HashMap<>();

        for (Transaction transaction : transactions) {
            if ("Expense".equals(transaction.getType())) {
                categoryExpenses.put(transaction.getCategory(), categoryExpenses.getOrDefault(transaction.getCategory(), 0.0) + transaction.getAmount());
            }
        }

        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    private void updateSummaryLabels() {
        double totalExpenses = 0;
        for (Transaction transaction : transactions) {
            if ("Expense".equals(transaction.getType())) {
                totalExpenses += transaction.getAmount();
            }
        }

        double totalMoneyLeft = getTotalIncome() - totalExpenses;

        totalExpensesLabel.setText("Total Expenses: " + totalExpenses);
        totalMoneyLeftLabel.setText("Total Money Left: " + totalMoneyLeft);
    }

    private double getTotalIncome() {
        double totalIncome = 0;
        for (Transaction transaction : transactions) {
            if ("Income".equals(transaction.getType())) {
                totalIncome += transaction.getAmount();
            }
        }
        return totalIncome;
    }

    public static class Transaction {
        private String type;
        private String category;
        private double amount;
        private LocalDate date;

        public Transaction(String type, String category, double amount, LocalDate date) {
            this.type = type;
            this.category = category;
            this.amount = amount;
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public String getCategory() {
            return category;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
