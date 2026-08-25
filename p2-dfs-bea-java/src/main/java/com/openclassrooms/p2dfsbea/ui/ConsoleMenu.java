package com.openclassrooms.p2dfsbea.ui;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;
import com.openclassrooms.p2dfsbea.service.ExpenseService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final ExpenseRepository repository = new ExpenseRepository();
    private final ExpenseService service = new ExpenseService(repository);

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showHistory();
                case "2" -> addExpense();
                case "3" -> showStatistics();
                case "4" -> planCarPurchase();
                case "5" -> calculateBorrowingCapacity();
                case "6" -> {
                    System.out.println("Goodbye");
                    running = false;
                }
                default -> System.out.println("Unknown option, please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Personal Finance Tracker ---");
        System.out.println("1) Show history");
        System.out.println("2) Add expense");
        System.out.println("3) Show statistics");
        System.out.println("4) Plan car purchase");
        System.out.println("5) Calculate borrowing capacity");
        System.out.println("6) Quit");
        System.out.print("Choose an option: ");
    }

    private void showHistory() {
        List<Expense> list = service.loadAllExpenses();
        System.out.println("\nHistory (most recent first):");
        list.stream().sorted((a, b) -> b.getDate().compareTo(a.getDate())).forEach(e -> System.out.println(e.toCsvLine()));
    }

    private void addExpense() {
        try {
            System.out.print("Date (dd/MM/yyyy): ");
            String dateS = scanner.nextLine().trim();
            LocalDate date = LocalDate.parse(dateS, Expense.FORMATTER);

            System.out.print("Category: ");
            String category = scanner.nextLine().trim();

            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Amount (e.g. 12.50): ");
            String amountS = scanner.nextLine().trim();
            BigDecimal amount = new BigDecimal(amountS);

            Expense e = new Expense(date, category, description, amount);
            service.addExpense(e);
            System.out.println("Expense added.");
        } catch (DateTimeParseException dtpe) {
            System.out.println("Invalid date format. Use dd/MM/yyyy.");
        } catch (IOException io) {
            System.out.println("Unable to save expense: " + io.getMessage());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void showStatistics() {
        List<Expense> list = service.loadAllExpenses();
        Map<String, BigDecimal> totals = service.totalPerCategory(list);
        Set<String> months = service.distinctMonths(list);
        System.out.println("\nTotals by category:");
        totals.forEach((k, v) -> System.out.println(k + ": " + v.setScale(2, RoundingMode.HALF_UP) + " EUR"));
        System.out.println("Distinct months: " + months.size());
    }

    private void planCarPurchase() {
        try {
            System.out.print("Car price (EUR): ");
            BigDecimal price = new BigDecimal(scanner.nextLine().trim());
            System.out.print("Monthly saving (EUR): ");
            BigDecimal monthly = new BigDecimal(scanner.nextLine().trim());
            long months = service.monthsToBuyCar(price, monthly);
            if (months == Long.MAX_VALUE) System.out.println("Monthly saving must be > 0");
            else System.out.println("You need " + months + " months to buy the car.");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private void calculateBorrowingCapacity() {
        try {
            System.out.print("Monthly payment you can afford (EUR): ");
            BigDecimal monthly = new BigDecimal(scanner.nextLine().trim());
            System.out.print("Annual interest rate (percent, e.g. 2.5): ");
            BigDecimal annual = new BigDecimal(scanner.nextLine().trim());
            System.out.print("Years: ");
            int years = Integer.parseInt(scanner.nextLine().trim());
            BigDecimal capacity = service.borrowingCapacity(monthly, annual, years);
            System.out.println("Estimated borrowing capacity: " + capacity.setScale(2, RoundingMode.HALF_UP) + " EUR");
        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
