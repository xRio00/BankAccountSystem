import java.io.*;
import java.util.*;

/**
 * Bank Account System
 * Allows users to register, log in, reset passwords, manage accounts, and transfer money.
 */
public class BankAccountSystem {

    // File name for storing account data
    private static final String FILE_NAME = "./accounts.txt";

    // HashMap to store accounts, with username as the key
    private static HashMap<String, Account> accounts = new HashMap<>();

    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // Account Class: Represents a bank account
    static class Account implements Serializable {
        private String username; // Unique username
        private String email;    // Email address
        private String phone;    // Phone number
        private String password; // Password for authentication
        private String accountNumber; // 13-digit unique account number
        private double balance;  // Account balance

        // Constructor: Initialize a new account
        Account(String username, String email, String phone, String password, double balance) {
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.balance = balance;
            this.accountNumber = generateAccountNumber();
        }

        // Generate a unique 13-digit account number starting with 1208
        private String generateAccountNumber() {
            Random random = new Random();
            StringBuilder sb = new StringBuilder("1208");
            for (int i = 0; i < 9; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }

        // Getters and Setters
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getPassword() { return password; }
        public String getAccountNumber() { return accountNumber; }
        public double getBalance() { return balance; }

        public void setPassword(String password) { this.password = password; }
        public void setEmail(String email) { this.email = email; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setBalance(double balance) { this.balance = balance; }

        // Display account details
        @Override
        public String toString() {
            return "Account Number: " + accountNumber + ", Username: " + username + 
                   ", Email: " + email + ", Phone: " + phone + ", Balance: " + balance;
        }
    }

    /**
     * Load accounts from a file into memory.
     */
    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<String, Account>) ois.readObject();
            System.out.println("Accounts loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("No saved accounts found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    /**
     * Save accounts from memory into a file.
     */
    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
            System.out.println("Accounts saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * Register a new user.
     */
    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        // Check for duplicate username, email, or phone
        for (Account account : accounts.values()) {
            if (account.getUsername().equals(username)) {
                System.out.println("Username already exists! Please try again.");
                return;
            }
            if (account.getEmail().equals(email)) {
                System.out.println("Email already exists! Please try again.");
                return;
            }
            if (account.getPhone().equals(phone)) {
                System.out.println("Phone number already exists! Please try again.");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match! Please try again.");
            return;
        }

        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        // Create the account and add it to the HashMap
        Account newAccount = new Account(username, email, phone, password, balance);
        accounts.put(username, newAccount);
        saveAccounts();
        System.out.println("Registration successful! Your account number is: " + newAccount.getAccountNumber());
    }

    /**
     * Log in with username, email, or phone.
     */
    private static void login() {
        System.out.print("Enter username, email, or phone number: ");
        String loginCredential = scanner.nextLine();

        // Find the account using any login credential
        Account account = null;
        for (Account acc : accounts.values()) {
            if (acc.getUsername().equals(loginCredential) || acc.getEmail().equals(loginCredential) || acc.getPhone().equals(loginCredential)) {
                account = acc;
                break;
            }
        }

        if (account == null) {
            System.out.println("Account not found! Please register.");
            return;
        }

        // Authenticate with password
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (account.getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + account.getUsername());
            loggedInMenu(account);
        } else {
            System.out.println("Incorrect password! Please try again.");
        }
    }

    /**
     * Reset the password with OTP.
     */
    private static void resetPassword() {
        System.out.print("Enter username, email, or phone number: ");
        String loginCredential = scanner.nextLine();

        // Find the account
        Account account = null;
        for (Account acc : accounts.values()) {
            if (acc.getUsername().equals(loginCredential) || acc.getEmail().equals(loginCredential) || acc.getPhone().equals(loginCredential)) {
                account = acc;
                break;
            }
        }

        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        // Generate OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        System.out.println("OTP: " + otp); // Display OTP for demo purposes

        // Verify OTP
        System.out.print("Enter OTP: ");
        int enteredOtp = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (enteredOtp == otp) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            account.setPassword(newPassword);
            saveAccounts();
            System.out.println("Password reset successfully!");
        } else {
            System.out.println("Invalid OTP! Please try again.");
        }
    }

    /**
     * Logged-in user menu.
     */
    private static void loggedInMenu(Account account) {
        while (true) {
            System.out.println("\nHello, " + account.getUsername());
            System.out.println("1. View Account");
            System.out.println("2. Update Account");
            System.out.println("3. Transfer Money");
            System.out.println("4. Delete Account");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    System.out.println(account);
                    break;
                case 2:
                    updateAccount(account);
                    break;
                case 3:
                    transferMoney(account);
                    break;
                case 4:
                    deleteAccount(account);
                    return;
                case 5:
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    /**
     * Update account details.
     */
    private static void updateAccount(Account account) {
        System.out.println("\nWhat do you want to update?");
        System.out.println("1. Password");
        System.out.println("2. Email");
        System.out.println("3. Phone");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1:
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                account.setPassword(newPassword);
                break;
            case 2:
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                account.setEmail(newEmail);
                break;
            case 3:
                System.out.print("Enter new phone number: ");
                String newPhone = scanner.nextLine();
                account.setPhone(newPhone);
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        saveAccounts();
        System.out.println("Account updated successfully!");
    }

    /**
     * Transfer money to another account.
     */
    private static void transferMoney(Account senderAccount) {
        System.out.print("Enter recipient's username: ");
        String recipientUsername = scanner.nextLine();
        if (!accounts.containsKey(recipientUsername)) {
            System.out.println("Recipient account not found!");
            return;
        }

        Account recipientAccount = accounts.get(recipientUsername);
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (amount <= 0 || amount > senderAccount.getBalance()) {
            System.out.println("Invalid amount! Transfer failed.");
            return;
        }

        // Perform the transfer
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        recipientAccount.setBalance(recipientAccount.getBalance() + amount);
        saveAccounts();
        System.out.println("Transfer successful! New balance: " + senderAccount.getBalance());
    }

    /**
     * Delete the logged-in account.
     */
    private static void deleteAccount(Account account) {
        System.out.print("Are you sure you want to delete your account? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            accounts.remove(account.getUsername());
            saveAccounts();
            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Account deletion canceled.");
        }
    }

    /**
     * Start menu for the application.
     */
    private static void startMenu() {
        loadAccounts(); // Load accounts from file
        while (true) {
            System.out.println("\nWelcome to the Bank Account System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Reset Password");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        startMenu();
    }
}
