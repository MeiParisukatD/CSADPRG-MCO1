/*
Last Names: Hila, Katigbak, Mesa, Tumbocon
Language: C
Paradigm(s): Imperative / Procedural
*/

#include <stdio.h>
#include <string.h>
#include <ctype.h>

void flushLine(void) {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}


void readName(char name[], int size) {
    char temp[100];
    char *start;
    size_t len;

    do {
        printf("Account Name: ");
        if (fgets(temp, size, stdin) == NULL) {
            temp[0] = '\0';
        }
        temp[strcspn(temp, "\n")] = '\0';

        start = temp;
        while (isspace((unsigned char)*start)) {
            start++;
        }

        len = strlen(start);
        while (len > 0 && isspace((unsigned char)start[len - 1])) {
            start[--len] = '\0';
        }

        strcpy(name, start);

        if (strlen(name) == 0) {
            printf("Account name cannot be empty. Please try again.\n");
        }

    } while (strlen(name) == 0);
}

double readAmount(const char *prompt, const char *errorMsg) {
    double value;

    while (1) {
        printf("%s", prompt);

        if (scanf("%lf", &value) == 1) {
            flushLine();
            return value;
        }

        printf("%s\n", errorMsg);
        flushLine();
    }
}

int main() {

    int choice;
    char accountName[100];
    double amount;
    double balance = 1000.00;
    int currencyChoice;
    double exchangeRate;

    printf("Select Transaction:\n");
    printf("[1] Register Account Name\n");
    printf("[2] Deposit Amount\n");
    printf("[3] Withdraw Amount\n");
    printf("[4] Currency Exchange\n");
    printf("[5] Record Exchange Rates\n");
    printf("[6] Show Interest Amount\n");

    do {
        printf("\nChoice: ");

        if (scanf("%d", &choice) != 1) {
            choice = 0;
        }
        flushLine();

        if (choice < 1 || choice > 6) {
            printf("Invalid choice. Please select 1-6.\n");
        }

    } while (choice < 1 || choice > 6);

    printf("\n***\n");
    printf("Choice = %d\n", choice);

    printf("\nRegister Account Name\n");

    readName(accountName, sizeof(accountName));

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);

    printf("\nDeposit Amount\n");

    readName(accountName, sizeof(accountName));

    printf("Current Balance: %.2f\n", balance);
    printf("Currency: PHP\n\n");

    amount = readAmount("Deposit Amount: ",
                        "Invalid input. Please enter a valid amount.");

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);
    printf("Deposit Amount = %.2f\n", amount);

    printf("\nWithdraw Amount\n");

    readName(accountName, sizeof(accountName));

    printf("Current Balance: %.2f\n", balance);
    printf("Currency: PHP\n\n");

    amount = readAmount("Withdraw Amount: ",
                        "Invalid input. Please enter a valid amount.");

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);
    printf("Withdraw Amount = %.2f\n", amount);


    printf("\nRecord Exchange Rate\n\n");

    printf("[1] Philippine Peso (PHP)\n");
    printf("[2] United States Dollar (USD)\n");
    printf("[3] Japanese Yen (JPY)\n");
    printf("[4] British Pound Sterling (GBP)\n");
    printf("[5] Euro (EUR)\n");
    printf("[6] Chinese Yuan Renminni (CNY)\n\n");

    do {
        printf("Select Foreign Currency: ");

        if (scanf("%d", &currencyChoice) != 1) {
            printf("Invalid input. Please enter a number from 1-6.\n");
            currencyChoice = 0;
        } else if (currencyChoice < 1 || currencyChoice > 6) {
            printf("Please select a currency from 1-6.\n");
        }
        flushLine();

    } while (currencyChoice < 1 || currencyChoice > 6);

    exchangeRate = readAmount("Exchange Rate: ",
                              "Invalid input. Please enter a valid exchange rate.");

    printf("\n***\n");
    printf("Select Foreign Currency = [%d]\n", currencyChoice);
    printf("Exchange Rate = %.2f\n", exchangeRate);

    printf("\nForeign Currency Exchange\n");

    amount = readAmount("Source Amount (PHP): ",
                        "Invalid input. Please enter a valid amount.");

    printf("\nExchanged Currency\n");

    printf("[1] Philippine Peso (PHP) = %.2f\n", amount);
    printf("[2] United States Dollar (USD) = %.2f\n", amount * 62.00);
    printf("[3] Japanese Yen (JPY) = %.2f\n", amount * 0.40);
    printf("[4] British Pound Sterling (GBP) = %.2f\n", amount * 84.00);
    printf("[5] Euro (EUR) = %.2f\n", amount * 72.00);
    printf("[6] Chinese Yuan Renminni (CNY) = %.2f\n", amount * 9.00);

    printf("\n***\n");
    printf("Source Currency = Philippine Peso (PHP)\n");
    printf("Source Amount (PHP) = %.2f\n", amount);


    return 0;
}
