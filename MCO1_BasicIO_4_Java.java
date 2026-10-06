 /*********************
Last names: Hila, Katigbak, Mesa, Tumbocon
Language: Java
Paradigm(s): Procedural (using Java's class structure)
*********************/

import java.util.Locale;
import java.util.Scanner;

public class MCO1_BasicIO_4_Java {
    private static final Scanner INPUT = new Scanner(System.in);

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return INPUT.nextLine();
    }

  private static String readNonBlankName(String prompt) {
        while (true) {
            String name = readLine(prompt);
            if (!name.trim().isEmpty()) {
                return name.trim();
            }
            System.out.println("Account name cannot be blank. Please enter a name.");
        }
    }

    private static double readAmount(String prompt) {
        while (true) {
            String line = readLine(prompt).trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static int readMenuChoice(String prompt, int minimum, int maximum) {
        while (true) {
            String line = readLine(prompt).trim();
            try {
                int choice = Integer.parseInt(line);
                if (choice >= minimum && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                // Show the same range prompt for non-numeric choices.
            }
            System.out.printf("Please enter a number from %d to %d.%n", minimum, maximum);
        }
    }

    private static void displayMainMenu() {
        System.out.println("Main Menu");
        System.out.println("Select Transaction:");
        System.out.println("[1] Register Account Name");
        System.out.println("[2] Deposit Amount");
        System.out.println("[3] Withdraw Amount");
        System.out.println("[4] Currency Exchange");
        System.out.println("[5] Record Exchange Rates");
        System.out.println("[6] Show Interest Amount");

        int choice = readMenuChoice("Choice: ", 1, 6);
        System.out.println("***");
        System.out.printf("Choice = %d%n%n", choice);
    }

    private static void displayRegisterAccountName() {
        System.out.println("Register Account Name");
        String accountName = readLine("Account Name: ");
        System.out.println("***");
        System.out.printf("Account Name = %s%n%n", accountName);
    }

    private static void displayDepositAmount() {
        System.out.println("Deposit Amount");
        String accountName = readLine("Account Name: ");
        System.out.println("Current Balance: 1000.00");
        System.out.println("Currency: PHP");
        double depositAmount = readAmount("Deposit Amount: ");
        System.out.println("***");
        System.out.printf("Account Name = %s%n", accountName);
        System.out.printf(Locale.US, "Deposit Amount = %.2f%n%n", depositAmount);
    }

    private static void displayWithdrawAmount() {
        System.out.println("Withdraw Amount");
        String accountName = readLine("Account Name: ");
        System.out.println("Current Balance: 1000.00");
        System.out.println("Currency: PHP");
        double withdrawAmount = readAmount("Withdraw Amount: ");
        System.out.println("***");
        System.out.printf("Account Name = %s%n", accountName);
        System.out.printf(Locale.US, "Withdraw Amount = %.2f%n%n", withdrawAmount);
    }

    private static void displayRecordExchangeRate() {
        String[] currencyNames = {
            "Philippine Peso (PHP)",
            "United States Dollar (USD)",
            "Japanese Yen (JPY)",
            "British Pound Sterling (GBP)",
            "Euro (EUR)",
            "Chinese Yuan Renminni (CNY)"
        };

        System.out.println("Record Exchange Rate");
        for (int index = 0; index < currencyNames.length; index++) {
            System.out.printf("[%d] %s%n", index + 1, currencyNames[index]);
        }

        int currencyChoice = readMenuChoice("Select Foreign Currency: ", 1, 6);
        double exchangeRate = readAmount("Exchange Rate: ");
        System.out.println("***");
        System.out.printf("Select Foreign Currency = [%d] %s%n",
                currencyChoice, currencyNames[currencyChoice - 1]);
        System.out.printf(Locale.US, "Exchange Rate = %.2f%n%n", exchangeRate);
    }

    private static void displayCurrencyExchange() {
        String[] currencyNames = {
            "Philippine Peso (PHP)",
            "United States Dollar (USD)",
            "Japanese Yen (JPY)",
            "British Pound Sterling (GBP)",
            "Euro (EUR)",
            "Chinese Yuan Renminni (CNY)"
        };
        double[] exchangeValues = {1.00, 62.00, 0.40, 84.00, 72.00, 9.00};

        System.out.println("Foreign Currency Exchange");
        double sourceAmount = readAmount("Source Amount (PHP): ");
        System.out.println("Exchanged Currency");

        for (int index = 0; index < currencyNames.length; index++) {
            System.out.printf(Locale.US, "[%d] %s = %.2f%n",
                    index + 1, currencyNames[index], sourceAmount * exchangeValues[index]);
        }

        System.out.println("***");
        System.out.println("Source Currency = Philippine Peso (PHP)");
        System.out.printf(Locale.US, "Source Amount (PHP) = %.2f%n", sourceAmount);
    }

    private static void displayInterestAmount() {
        System.out.println("Show Interest Amount");
        System.out.println("Interest Amount = 0.00");
    }

    public static void main(String[] args) {
        displayMainMenu();
        displayRegisterAccountName();
        displayDepositAmount();
        displayWithdrawAmount();
        displayRecordExchangeRate();
        displayCurrencyExchange();
        displayInterestAmount();
    }
}
