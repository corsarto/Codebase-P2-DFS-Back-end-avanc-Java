package com.openclassrooms.p2dfsbea.ui;

import com.openclassrooms.p2dfsbea.model.Expense;
import com.openclassrooms.p2dfsbea.repository.ExpenseRepository;
import com.openclassrooms.p2dfsbea.service.ExpenseService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
                case "3" -> statistics();
                case "4" -> simulationForCar();
                case "5" -> {
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
        System.out.println("3) Statistics");
        System.out.println("4) Car purchase simulation");
        System.out.println("5) Quit");
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
            String amountS = scanner.nextLine().trim().replace(',', '.');
            BigDecimal amount = new BigDecimal(amountS);

            Expense e = new Expense(date, category, description, amount);
            service.addExpense(e);
            System.out.println("Expense added.");
        } catch (DateTimeParseException dtpe) {
            System.out.println("Invalid date format. Use dd/MM/yyyy.");
        } catch (IOException io) {
            System.out.println("Unable to save expense: " + io.getMessage());
        } catch(NumberFormatException ex) {
        	System.out.println("Invalid amount format. Decimal is expected.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void statistics() {
        List<Expense> expenses = service.removeBudget(service.loadAllExpenses());
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet. Add one.");
            return;
        }

        Map<String, BigDecimal> amountForCategories = service.calculateAmountForCategories(expenses);
        System.out.println(" Totals by category :");
        for (Map.Entry<String, BigDecimal> amountEntry : amountForCategories.entrySet()) {
            System.out.println(amountEntry.getKey() + " : " + amountEntry.getValue());
        }

        BigDecimal totalAmount = service.calculateTotalAmount(amountForCategories);
        System.out.println(" Total expenses: " + totalAmount);

        int months = service.countMonths(expenses);
        System.out.println(" Months covered: " + months);

        Map<String, BigDecimal> monthlyAverageByCategory = service.calculateMonthlyAverageByCategories(amountForCategories, months);
        System.out.println(" Monthly average by category :");
        for (Map.Entry<String, BigDecimal> averageEntry : monthlyAverageByCategory.entrySet()) {
            System.out.println(averageEntry.getKey() + " : " + averageEntry.getValue());
        }

    }

    private void simulationForCar(){
        List<Expense> expenses = service.loadAllExpenses();

        BigDecimal monthlySaving = service.calculateAvailableMonthlySaving(expenses);
        System.out.println("Available monthly savings :" + monthlySaving);

        try {
            System.out.print("Price : ");
            String prices = scanner.nextLine().trim().replace(',', '.');
            BigDecimal price = new BigDecimal(prices);
        
            int monthsNeeded = service.calculateMonthsNeeded(price, monthlySaving);
            if (monthsNeeded == -1) {
                System.out.println("No savings available, purchase impossible.");
            } else {
                System.out.println("Months estimated to buy this car: " + monthsNeeded);
            }
        } catch(NumberFormatException ex) {
            System.out.println("Invalid price format. Decimal is expected.");
        }
    }
}
