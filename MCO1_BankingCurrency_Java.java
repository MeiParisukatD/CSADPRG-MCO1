/*
 * Major Course Output #1: Banking and Currency Exchange Application
 * Team: Hila, Katigbak, Mesa, Tumbocon
 * Language: Java
 */


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public final class MCO1_BankingCurrency_Java {
    private static final BigDecimal OPENING_BALANCE = new BigDecimal("1000.00");
    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.05");
    private static final BigDecimal DAYS_PER_YEAR = new BigDecimal("365");
    private static final int MAX_INTEREST_DAYS = 3650;

    private final Scanner input = new Scanner(System.in);
    private final Map<String, BankAccount> accountsByName = new LinkedHashMap<>();
    private final EnumMap<Currency, BigDecimal> phpPerCurrency =
            new EnumMap<>(Currency.class);
    private int nextAccountNumber = 1;

    private enum Currency {
        PHP("Philippine Peso", "PHP"),
        USD("United States Dollar", "USD"),
        JPY("Japanese Yen", "JPY"),
        GBP("British Pound Sterling", "GBP"),
        EUR("Euro", "EUR"),
        CNY("Chinese Yuan Renminni", "CNY");

        private final String name;
        private final String code;

        Currency(String name, String code) {
            this.name = name;
            this.code = code;
        }

        private String label() {
            return name + " (" + code + ")";
        }
    }

    private static final class BankAccount {
        private final String accountNumber;
        private final String accountName;
        private BigDecimal balancePHP;

        private BankAccount(String accountNumber, String accountName) {
            this.accountNumber = accountNumber;
            this.accountName = accountName;
            this.balancePHP = OPENING_BALANCE;
        }
    }

    private MCO1_BankingCurrency_Java() {
        // PHP is the base currency, so its conversion rate is always one.
        phpPerCurrency.put(Currency.PHP, BigDecimal.ONE);
    }

    public static void main(String[] args) {
        new MCO1_BankingCurrency_Java().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = readInteger("Choice: ", 1, 6);

            switch (choice) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    depositAmount();
                    break;
                case 3:
                    withdrawAmount();
                    break;
                case 4:
                    currencyExchange();
                    break;
                case 5:
                    recordExchangeRate();
                    break;
                case 6:
                    showInterestAmount();
                    break;
                default:
                    throw new IllegalStateException("Validated menu choice was out of range.");
            }

            running = readYesNo("Back to the Main Menu (Y/N): ");
        }

        System.out.println("Thank you for using the Banking and Currency Exchange Application.");
    }

    private void displayMainMenu() {
        System.out.println();
        System.out.println("Main Menu");
        System.out.println("Select Transaction:");
        System.out.println("[1] Register Account Name");
        System.out.println("[2] Deposit Amount");
        System.out.println("[3] Withdraw Amount");
        System.out.println("[4] Currency Exchange");
        System.out.println("[5] Record Exchange Rates");
        System.out.println("[6] Show Interest Amount");
    }

    private void registerAccount() {
        System.out.println();
        System.out.println("Register Account Name");
        String accountName = readNonBlank("Account Name: ");
        String key = accountKey(accountName);

        if (accountsByName.containsKey(key)) {
            BankAccount existing = accountsByName.get(key);
            System.out.println("An account with that name is already registered.");
            printAccountSummary(existing);
            return;
        }

        String accountNumber = String.format(Locale.ROOT, "ACC-%04d", nextAccountNumber++);
        BankAccount account = new BankAccount(accountNumber, accountName);
        accountsByName.put(key, account);

        System.out.println("Account registered successfully.");
        printAccountSummary(account);
        System.out.println("Opening Balance: " + money(account.balancePHP) + " PHP");
    }

    private void depositAmount() {
        System.out.println();
        System.out.println("Deposit Amount");
        BankAccount account = promptAccount();
        if (account == null) {
            return;
        }

        printAccountSummary(account);
        System.out.println("Current Balance: " + money(account.balancePHP));
        System.out.println("Currency: PHP (account balance)");
        Currency currency = promptCurrency("Select Deposit Currency:");
        BigDecimal rate = rateToPHP(currency);
        if (rate == null) {
            return;
        }

        BigDecimal amount = readPositiveDecimal("Deposit Amount (" + currency.code + "): ");
        BigDecimal amountPHP = toPHP(amount, rate);
        if (amountPHP.signum() == 0) {
            System.out.println("Deposit value rounds to less than one cent PHP; no deposit was made.");
            return;
        }
        account.balancePHP = account.balancePHP.add(amountPHP).setScale(2, RoundingMode.HALF_UP);

        System.out.println("Deposit Amount = " + money(amount) + " " + currency.code);
        System.out.println("Deposit Value = " + money(amountPHP) + " PHP");
        System.out.println("Updated Balance: " + money(account.balancePHP) + " PHP");
    }

    private void withdrawAmount() {
        System.out.println();
        System.out.println("Withdraw Amount");
        BankAccount account = promptAccount();
        if (account == null) {
            return;
        }

        printAccountSummary(account);
        System.out.println("Current Balance: " + money(account.balancePHP));
        System.out.println("Currency: PHP (account balance)");
        Currency currency = promptCurrency("Select Withdrawal Currency:");
        BigDecimal rate = rateToPHP(currency);
        if (rate == null) {
            return;
        }

        BigDecimal amount = readPositiveDecimal("Withdraw Amount (" + currency.code + "): ");
        BigDecimal amountPHP = toPHP(amount, rate);
        if (amountPHP.signum() == 0) {
            System.out.println("Withdrawal value rounds to less than one cent PHP; no withdrawal was made.");
            return;
        }
        if (amountPHP.compareTo(account.balancePHP) > 0) {
            System.out.println("Withdrawal declined: the account has insufficient funds.");
            System.out.println("Available Balance: " + money(account.balancePHP) + " PHP");
            return;
        }

        account.balancePHP = account.balancePHP.subtract(amountPHP)
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("Withdraw Amount = " + money(amount) + " " + currency.code);
        System.out.println("Withdrawal Value = " + money(amountPHP) + " PHP");
        System.out.println("Updated Balance: " + money(account.balancePHP) + " PHP");
    }

    private void recordExchangeRate() {
        System.out.println();
        System.out.println("Record Exchange Rate");
        Currency currency = promptCurrency("Select Currency:");

        if (currency == Currency.PHP) {
            System.out.println("PHP is the base currency. Its rate is fixed at 1.00.");
            return;
        }

        BigDecimal rate;
        while (true) {
            rate = readPositiveDecimal("Exchange Rate (PHP per 1 " + currency.code + "): ")
                    .setScale(6, RoundingMode.HALF_UP);
            if (rate.signum() > 0) {
                break;
            }
            System.out.println("That rate rounds to zero. Enter a rate of at least 0.0000005.");
        }
        phpPerCurrency.put(currency, rate);
        System.out.println("Exchange Rate = " + rateText(rate) + " PHP per 1 " + currency.code);
    }

    private void currencyExchange() {
        System.out.println();
        System.out.println("Currency Exchange");
        boolean convertAnother;
        do {
            convertOneCurrency();
            convertAnother = readYesNo("Convert another currency (Y/N)? ");
        } while (convertAnother);
    }

    private void convertOneCurrency() {
        System.out.println();
        System.out.println("Foreign Currency Exchange");
        Currency sourceCurrency = promptCurrency("Source Currency Options:");
        BigDecimal sourceRate = rateToPHP(sourceCurrency);
        if (sourceRate == null) {
            return;
        }

        BigDecimal sourceAmount = readPositiveDecimal(
                "Source Amount (" + sourceCurrency.code + "): ");
        Currency targetCurrency = promptCurrency("Exchanged Currency Options:");
        BigDecimal targetRate = rateToPHP(targetCurrency);
        if (targetRate == null) {
            return;
        }

        // Rates are stored as PHP per unit, so convert through PHP as the base.
        BigDecimal targetAmount = convertAmount(sourceAmount, sourceRate, targetRate);
        System.out.println("Source Currency = " + sourceCurrency.label());
        System.out.println("Source Amount = " + money(sourceAmount) + " " + sourceCurrency.code);
        System.out.println("Exchange Currency = " + targetCurrency.label());
        System.out.println("Exchange Amount = " + money(targetAmount) + " " + targetCurrency.code);
    }

    private void showInterestAmount() {
        System.out.println();
        System.out.println("Show Interest Amount");
        BankAccount account = promptAccount();
        if (account == null) {
            return;
        }

        printAccountSummary(account);
        System.out.println("Current Balance: " + money(account.balancePHP));
        System.out.println("Currency: PHP");
        System.out.println("Interest Rate: 5% per annum");
        int days = readInteger("Total Number of Days: ", 1, MAX_INTEREST_DAYS);

        BigDecimal projectedBalance = account.balancePHP;
        BigDecimal totalInterest = BigDecimal.ZERO.setScale(2);
        System.out.println();
        System.out.println("Day | Interest | Balance |");
        for (int day = 1; day <= days; day++) {
            // Apply the required daily formula to the prior day's closing balance.
            BigDecimal dailyInterest = calculateDailyInterest(projectedBalance);
            projectedBalance = projectedBalance.add(dailyInterest)
                    .setScale(2, RoundingMode.HALF_UP);
            totalInterest = totalInterest.add(dailyInterest).setScale(2, RoundingMode.HALF_UP);
            System.out.println(day + " | " + money(dailyInterest) + " | "
                    + money(projectedBalance) + " |");
        }

        System.out.println("Expected Interest = " + money(totalInterest) + " PHP");
        System.out.println("Projected Balance = " + money(projectedBalance) + " PHP");
        System.out.println("The projection does not change the actual account balance.");
    }

    private BankAccount promptAccount() {
        if (accountsByName.isEmpty()) {
            System.out.println("No account is registered. Register an account first.");
            return null;
        }

        String name = readNonBlank("Account Name: ");
        BankAccount account = accountsByName.get(accountKey(name));
        if (account == null) {
            System.out.println("Account not found. Check the name or register the account first.");
        }
        return account;
    }

    private Currency promptCurrency(String heading) {
        System.out.println(heading);
        Currency[] currencies = Currency.values();
        for (int index = 0; index < currencies.length; index++) {
            System.out.println("[" + (index + 1) + "] " + currencies[index].label());
        }
        int choice = readInteger("Currency Choice: ", 1, currencies.length);
        return currencies[choice - 1];
    }

    private BigDecimal rateToPHP(Currency currency) {
        BigDecimal rate = phpPerCurrency.get(currency);
        if (rate == null) {
            System.out.println("No exchange rate is recorded for " + currency.code + ".");
            System.out.println("Use Record Exchange Rates from the Main Menu first.");
        }
        return rate;
    }

    static BigDecimal toPHP(BigDecimal amount, BigDecimal phpPerUnit) {
        return amount.multiply(phpPerUnit).setScale(2, RoundingMode.HALF_UP);
    }

    static BigDecimal convertAmount(BigDecimal amount, BigDecimal sourcePhpRate,
            BigDecimal targetPhpRate) {
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Conversion amount cannot be negative.");
        }
        if (sourcePhpRate.signum() <= 0 || targetPhpRate.signum() <= 0) {
            throw new IllegalArgumentException("Currency rates must be greater than zero.");
        }
        return amount.multiply(sourcePhpRate)
                .divide(targetPhpRate, 2, RoundingMode.HALF_UP);
    }

    static BigDecimal calculateDailyInterest(BigDecimal endOfDayBalance) {
        if (endOfDayBalance.signum() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        return endOfDayBalance.multiply(ANNUAL_INTEREST_RATE)
                .divide(DAYS_PER_YEAR, 2, RoundingMode.HALF_UP);
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    private String readNonBlank(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    private BigDecimal readPositiveDecimal(String prompt) {
        while (true) {
            String value = readNonBlank(prompt);
            try {
                BigDecimal number = new BigDecimal(value);
                if (number.compareTo(BigDecimal.ZERO) > 0) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                // The message below handles both non-numbers and non-positive amounts.
            }
            System.out.println("Please enter a valid number greater than zero.");
        }
    }

    private int readInteger(String prompt, int minimum, int maximum) {
        while (true) {
            String value = readNonBlank(prompt);
            try {
                int number = Integer.parseInt(value);
                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException exception) {
                // The range message below handles non-integer input.
            }
            System.out.println("Please enter a whole number from " + minimum + " to " + maximum + ".");
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String answer = readNonBlank(prompt).toUpperCase(Locale.ROOT);
            if (answer.equals("Y")) {
                return true;
            }
            if (answer.equals("N")) {
                return false;
            }
            System.out.println("Please enter Y or N.");
        }
    }

    private static String accountKey(String accountName) {
        return accountName.trim().toLowerCase(Locale.ROOT);
    }

    private static void printAccountSummary(BankAccount account) {
        System.out.println("Account Number: " + account.accountNumber);
        System.out.println("Account Name: " + account.accountName);
    }

    private static String money(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String rateText(BigDecimal rate) {
        BigDecimal normalized = rate.stripTrailingZeros();
        if (normalized.scale() < 2) {
            normalized = normalized.setScale(2, RoundingMode.HALF_UP);
        }
        return normalized.toPlainString();
    }
}
