package com.openclassrooms.p2dfsbea.repository;

import com.openclassrooms.p2dfsbea.model.Expense;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExpenseRepository {
    private final Path emmaPath = Path.of("src/main/resources/emma_expenses.csv");
    private final Path personnalPath = Path.of("src/main/resources/personnal_expenses.csv");

    public List<Expense> readAll() {
        List<Expense> result = new ArrayList<>();
        readFileIntoList(emmaPath, result);
        readFileIntoList(personnalPath, result);
        return result;
    }

    private void readFileIntoList(Path p, List<Expense> list) {
        if (!Files.exists(p)) return;
        try (Stream<String> lines = Files.lines(p, StandardCharsets.UTF_8)) {
            lines.map(Expense::fromCsvLine).filter(e -> e != null).forEach(list::add);
        } catch (IOException e) {
            System.err.println("Unable to read " + p + ": " + e.getMessage());
        }
    }

    public void append(Expense expense) throws IOException {
        // Ensure file exists
        if (!Files.exists(personnalPath)) {
            Files.createDirectories(personnalPath.getParent());
            Files.createFile(personnalPath);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(personnalPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(expense.toCsvLine());
            writer.newLine();
        }
    }
}
