
#include <ctype.h>
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NUM_CURRENCIES 6
#define BASE_INDEX     0          
#define NAME_LEN       100
#define LINE_LEN       256
#define ANNUAL_RATE    0.05
#define DAYS_IN_YEAR   365.0
#define MAX_DAYS       3650
#define MAX_AMOUNT     1e12

typedef struct {
    const char *code;
    const char *name;
} Currency;

typedef struct {
    char   name[NAME_LEN];
    int    registered;
    double balances[NUM_CURRENCIES];
} Account;

static const Currency CURRENCIES[NUM_CURRENCIES] = {
    {"PHP", "Philippine Peso"},
    {"USD", "United States Dollar"},
    {"JPY", "Japanese Yen"},
    {"GBP", "British Pound Sterling"},
    {"EUR", "Euro"},
    {"CNY", "Chinese Yuan Renminni"}
};

/* PHP per 1 unit. 0.0 means "not recorded yet". */
static double rates[NUM_CURRENCIES] = {1.0, 0.0, 0.0, 0.0, 0.0, 0.0};

static Account account;

/* ---------- input helpers ---------- */

static void trimNewline(char *s)
{
    size_t n = strlen(s);
    while (n > 0 && (s[n - 1] == '\n' || s[n - 1] == '\r')) {
        s[--n] = '\0';
    }
}

/* Reads one line; returns 0 on EOF. Discards the rest of an overlong line. */
static int readLine(char *buf, size_t size)
{
    if (fgets(buf, (int)size, stdin) == NULL) {
        return 0;
    }
    if (strchr(buf, '\n') == NULL) {
        int c;
        while ((c = getchar()) != '\n' && c != EOF) { /* discard */ }
    }
    trimNewline(buf);
    return 1;
}

static int isBlank(const char *s)
{
    while (*s) {
        if (!isspace((unsigned char)*s)) return 0;
        s++;
    }
    return 1;
}

static int readInt(const char *prompt, int min, int max)
{
    char buf[LINE_LEN];
    for (;;) {
        printf("%s", prompt);
        if (!readLine(buf, sizeof buf)) exit(0);
        char *end;
        long v = strtol(buf, &end, 10);
        if (end != buf && isBlank(end) && v >= min && v <= max) {
            return (int)v;
        }
        printf("Invalid input. Enter a number from %d to %d.\n", min, max);
    }
}

static double readPositiveAmount(const char *prompt)
{
    char buf[LINE_LEN];
    for (;;) {
        printf("%s", prompt);
        if (!readLine(buf, sizeof buf)) exit(0);
        char *end;
        double v = strtod(buf, &end);
        if (end != buf && isBlank(end) && isfinite(v) && v > 0.0 && v <= MAX_AMOUNT) {
            return v;
        }
        printf("Invalid amount. Enter a positive number.\n");
    }
}

static int readYesNo(const char *prompt)
{
    char buf[LINE_LEN];
    for (;;) {
        printf("%s", prompt);
        if (!readLine(buf, sizeof buf)) exit(0);
        char c = (char)toupper((unsigned char)buf[0]);
        if ((c == 'Y' || c == 'N') && isBlank(buf + 1)) {
            return c == 'Y';
        }
        printf("Please enter Y or N.\n");
    }
}

/* ---------- money helpers ---------- */

static double roundCents(double x)
{
    return floor(x * 100.0 + 0.5) / 100.0;
}

static void printCurrencyMenu(void)
{
    for (int i = 0; i < NUM_CURRENCIES; i++) {
        printf("[%d] %s (%s)\n", i + 1, CURRENCIES[i].name, CURRENCIES[i].code);
    }
}

static int chooseCurrency(const char *prompt)
{
    printCurrencyMenu();
    printf("\n");
    return readInt(prompt, 1, NUM_CURRENCIES) - 1;
}

static int requireAccount(void)
{
    if (!account.registered) {
        printf("No account registered yet. Use [1] Register Account Name first.\n");
        return 0;
    }
    return 1;
}

/* ---------- transactions ---------- */

static void registerAccount(void)
{
    char buf[NAME_LEN];
    printf("Register Account Name\n");
    for (;;) {
        printf("Account Name: ");
        if (!readLine(buf, sizeof buf)) exit(0);
        if (!isBlank(buf)) break;
        printf("Name cannot be empty.\n");
    }
    if (account.registered && strcmp(account.name, buf) != 0) {
        memset(account.balances, 0, sizeof account.balances);
    }
    strcpy(account.name, buf);
    account.registered = 1;
}

static void deposit(void)
{
    printf("Deposit Amount\n");
    if (!requireAccount()) return;

    int c = BASE_INDEX;
    printf("Account Name: %s\n", account.name);
    printf("Current Balance: %.2f\n", account.balances[c]);
    printf("Currency: %s\n\n", CURRENCIES[c].code);

    double amt = roundCents(readPositiveAmount("Deposit Amount: "));
    account.balances[c] += amt;
    printf("Updated Balance: %.2f\n", account.balances[c]);
}

static void withdraw(void)
{
    printf("Withdraw Amount\n");
    if (!requireAccount()) return;

    int c = BASE_INDEX;
    printf("Account Name: %s\n", account.name);
    printf("Current Balance: %.2f\n", account.balances[c]);
    printf("Currency: %s\n\n", CURRENCIES[c].code);

    double amt = roundCents(readPositiveAmount("Withdraw Amount: "));
    if (amt > account.balances[c]) {
        printf("Insufficient funds. Withdrawal cancelled.\n");
        printf("Updated Balance: %.2f\n", account.balances[c]);
        return;
    }
    account.balances[c] = roundCents(account.balances[c] - amt);
    printf("Updated Balance: %.2f\n", account.balances[c]);
}

static void recordExchangeRate(void)
{
    printf("Record Exchange Rate\n\n");
    int c = chooseCurrency("Select Foreign Currency: ");
    if (c == BASE_INDEX) {
        printf("PHP is the base currency; its rate is fixed at 1.00.\n");
        return;
    }
    rates[c] = readPositiveAmount("Exchange Rate: ");
}

static void currencyExchange(void)
{
    printf("Foreign Currency Exchange\n");
    do {
        printf("Source Currency Option:\n");
        int src = chooseCurrency("Source Currency: ");
        double amt = readPositiveAmount("Source Amount: ");

        printf("\nExchanged Currency Options:\n");
        int dst = chooseCurrency("Exchange Currency: ");

        if (rates[src] <= 0.0 || rates[dst] <= 0.0) {
            printf("Exchange rate not recorded for %s. Use [5] Record Exchange Rates first.\n",
                   rates[src] <= 0.0 ? CURRENCIES[src].code : CURRENCIES[dst].code);
        } else {
            double result = amt * rates[src] / rates[dst];
            printf("Exchange Amount: %.2f\n", result);
        }
        printf("\n");
    } while (readYesNo("Convert another currency (Y/N)? "));
}

static void showInterest(void)
{
    printf("Show Interest Amount\n");
    if (!requireAccount()) return;

    int c = BASE_INDEX;
    printf("Account Name: %s\n", account.name);
    printf("Current Balance: %.2f\n", account.balances[c]);
    printf("Currency: %s\n", CURRENCIES[c].code);
    printf("Interest Rate: %.0f%%\n\n", ANNUAL_RATE * 100.0);

    int days = readInt("Total Number of Days: ", 1, MAX_DAYS);

    printf("\nDay | Interest | Balance |\n");
    double balance = account.balances[c];
    for (int d = 1; d <= days; d++) {
        double interest = roundCents(balance * (ANNUAL_RATE / DAYS_IN_YEAR));
        balance = roundCents(balance + interest);
        printf("%-3d | %-8.2f | %-.2f |\n", d, interest, balance);
    }
}


static void printMainMenu(void)
{
    printf("\nSelect Transaction:\n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n");
}

int main(void)
{
    memset(&account, 0, sizeof account);

    for (;;) {
        printMainMenu();
        int choice = readInt("Choice: ", 1, 6);
        printf("\n");

        switch (choice) {
            case 1: registerAccount();   break;
            case 2: deposit();           break;
            case 3: withdraw();          break;
            case 4: currencyExchange();  break;
            case 5: recordExchangeRate(); break;
            case 6: showInterest();      break;
        }

        printf("\n");
        if (!readYesNo("Back to the Main Menu (Y/N): ")) {
            break;
        }
    }
    printf("Goodbye.\n");
    return 0;
}
