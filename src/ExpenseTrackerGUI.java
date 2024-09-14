import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ExpenseTrackerGUI extends JFrame implements ActionListener {
    private final JTextField categoryField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextArea displayArea = new JTextArea();
    private final JButton addButton = new JButton("Add");
    private final JButton viewButton = new JButton("View");
    private final JButton deleteButton = new JButton("Delete");
    private final JButton summaryButton = new JButton("Summary");

    private final Map<String, List<Double>> expenses = new HashMap<>();

    public ExpenseTrackerGUI() {
        setTitle("Expense Tracker");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Set background color to black
        getContentPane().setBackground(Color.BLACK);

        // Labels and text fields
        JLabel categoryLabel = new JLabel("Category:");
        JLabel amountLabel = new JLabel("Amount:");
        categoryLabel.setForeground(Color.WHITE);
        amountLabel.setForeground(Color.WHITE);

        categoryLabel.setBounds(20, 20, 80, 25);
        amountLabel.setBounds(20, 60, 80, 25);
        categoryField.setBounds(100, 20, 150, 25);
        amountField.setBounds(100, 60, 150, 25);

        add(categoryLabel);
        add(categoryField);
        add(amountLabel);
        add(amountField);

        // Customize buttons and set them to have a black theme
        addButton.setBounds(20, 100, 100, 25);
        viewButton.setBounds(130, 100, 100, 25);
        deleteButton.setBounds(240, 100, 100, 25);
        summaryButton.setBounds(20, 140, 320, 25);
        displayArea.setBounds(20, 180, 320, 350);
        displayArea.setEditable(false);

        // Set button colors for contrast
        addButton.setBackground(Color.GRAY);
        addButton.setForeground(Color.WHITE);
        viewButton.setBackground(Color.DARK_GRAY);
        viewButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        summaryButton.setBackground(Color.ORANGE);
        summaryButton.setForeground(Color.WHITE);

        // Set JTextArea colors (background black, text white)
        displayArea.setBackground(Color.BLACK);
        displayArea.setForeground(Color.WHITE);

        add(addButton);
        add(viewButton);
        add(deleteButton);
        add(summaryButton);
        add(displayArea);

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        deleteButton.addActionListener(this);
        summaryButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Add")) {
            addExpense();
        } else if (command.equals("View")) {
            viewExpenses();
        } else if (command.equals("Delete")) {
            deleteExpense();
        } else if (command.equals("Summary")) {
            viewSummary();
        }
    }

    private void addExpense() {
        String category = categoryField.getText();
        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category cannot be empty.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
            return;
        }

        expenses.computeIfAbsent(category, k -> new ArrayList<>()).add(amount);
        JOptionPane.showMessageDialog(this, "Expense added.");
        categoryField.setText("");
        amountField.setText("");
    }

    private void viewExpenses() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<Double>> entry : expenses.entrySet()) {
            sb.append("Category: ").append(entry.getKey()).append("\n");
            for (double amount : entry.getValue()) {
                sb.append(" - Amount: ").append(amount).append("\n");
            }
        }
        displayArea.setText(sb.toString());
    }

    private void deleteExpense() {
        String category = categoryField.getText();
        if (category.isEmpty() || !expenses.containsKey(category)) {
            JOptionPane.showMessageDialog(this, "Category not found.");
            return;
        }

        double amountToDelete;
        try {
            amountToDelete = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.");
            return;
        }

        List<Double> amounts = expenses.get(category);
        if (amounts.remove(amountToDelete)) {
            JOptionPane.showMessageDialog(this, "Expense removed.");
            if (amounts.isEmpty()) {
                expenses.remove(category);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Expense not found.");
        }
        categoryField.setText("");
        amountField.setText("");
    }

    private void viewSummary() {
        double total = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Map.Entry<String, List<Double>> entry : expenses.entrySet()) {
            String category = entry.getKey();
            double categoryTotal = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            categoryTotals.put(category, categoryTotal);
            total += categoryTotal;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Expense Summary:\n");
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            sb.append("Category: ").append(entry.getKey()).append(" | Total: ").append(entry.getValue()).append("\n");
        }
        sb.append("Total Spending: ").append(total);
        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTrackerGUI().setVisible(true));
    }
}
