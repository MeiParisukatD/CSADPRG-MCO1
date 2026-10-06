/*
Last Names: [LAST NAME 1], [LAST NAME 2], [LAST NAME 3]
Language: C
Paradigm: Imperative / Procedural
Filename: MCO1_BasicIO_4_C.c
*/

#include <stdio.h>
#include <string.h>
#include <ctype.h>

int main() {

    int choice;
    char accountName[100];
    char tempName[100];
    double amount;
    double balance = 1000.00;
    int currencyChoice;
    double exchangeRate;

    /* MAIN MENU */
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
            while (getchar() != '\n');
            choice = 0;
        }

        if (choice < 1 || choice > 6) {
            printf("Invalid choice. Please select 1-6.\n");
        }

    } while (choice < 1 || choice > 6);

    printf("\n***\n");
    printf("Choice = %d\n", choice);


    /* REGISTER ACCOUNT NAME */
    printf("\n\nRegister Account Name\n");

    getchar();

    do {
        printf("Account Name: ");
        fgets(accountName, sizeof(accountName), stdin);

        accountName[strcspn(accountName, "\n")] = '\0';

        /* Remove leading spaces */
        strcpy(tempName, accountName);

        char *start = tempName;
        while (isspace((unsigned char)*start)) {
            start++;
        }

        /* Remove trailing spaces */
        char *end = start + strlen(start) - 1;

        while (end >= start && isspace((unsigned char)*end)) {
            *end = '\0';
            end--;
        }

        strcpy(accountName, start);

        if (strlen(accountName) == 0) {
            printf("Account name cannot be empty. Please try again.\n");
        }

    } while (strlen(accountName) == 0);

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);


    /* DEPOSIT AMOUNT */
    printf("\n\nDeposit Amount\n");

    printf("Account Name: ");
    printf("%s\n", accountName);

    printf("Current Balance: %.2f\n", balance);
    printf("Currency: PHP\n\n");

    do {
        printf("Deposit Amount: ");

        if (scanf("%lf", &amount) != 1) {
            printf("Invalid input. Please enter a valid amount.\n");
            while (getchar() != '\n');
            amount = 0;
        } else {
            break;
        }

    } while (1);

    balance += amount;

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);
    printf("Deposit Amount = %.2f\n", amount);


    /* WITHDRAW AMOUNT */
    printf("\n\nWithdraw Amount\n");

    printf("Account Name: ");
    printf("%s\n", accountName);

    printf("Current Balance: %.2f\n", balance);
    printf("Currency: PHP\n\n");

    do {
        printf("Withdraw Amount: ");

        if (scanf("%lf", &amount) != 1) {
            printf("Invalid input. Please enter a valid amount.\n");
            while (getchar() != '\n');
            amount = 0;
        } else {
            break;
        }

    } while (1);

    if (amount <= balance) {
        balance -= amount;
    }

    printf("\n***\n");
    printf("Account Name = %s\n", accountName);
    printf("Withdraw Amount = %.2f\n", amount);

    if (amount > balance + amount) {
        printf("Insufficient balance.\n");
    }


    /* CURRENCY EXCHANGE */
    printf("\n\nForeign Currency Exchange\n");

    printf("Source Amount (PHP): ");

    do {
        if (scanf("%lf", &amount) != 1) {
            printf("Invalid input. Please enter a valid amount.\n");
            while (getchar() != '\n');
            amount = 0;
        } else {
            break;
        }

    } while (1);

    printf("\nExchanged Currency\n\n");

    printf("[1] Philippine Peso (PHP) = %.2f\n",
           amount);

    printf("[2] United States Dollar (USD) = %.2f\n",
           amount * 62.00);

    printf("[3] Japanese Yen (JPY) = %.2f\n",
           amount * 0.40);

    printf("[4] British Pound Sterling (GBP) = %.2f\n",
           amount * 84.00);

    printf("[5] Euro (EUR) = %.2f\n",
           amount * 72.00);

    printf("[6] Chinese Yuan Renminni (CNY) = %.2f\n",
           amount * 9.00);

    printf("\n***\n");
    printf("Source Currency = Philippine Peso (PHP)\n");
    printf("Source Amount (PHP) = %.2f\n", amount);


    /* RECORD EXCHANGE RATE */
    printf("\n\nRecord Exchange Rate\n");

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
            while (getchar() != '\n');
            currencyChoice = 0;
        }

        if (currencyChoice < 1 || currencyChoice > 6) {
            printf("Please select a currency from 1-6.\n");
        }

    } while (currencyChoice < 1 || currencyChoice > 6);

    do {
        printf("Exchange Rate: ");

        if (scanf("%lf", &exchangeRate) != 1) {
            printf("Invalid input. Please enter a valid exchange rate.\n");
            while (getchar() != '\n');
            exchangeRate = 0;
        } else {
            break;
        }

    } while (1);

    printf("\n***\n");
    printf("Select Foreign Currency = [%d]\n",
           currencyChoice);

    printf("Exchange Rate = %.2f\n",
           exchangeRate);


    /* SHOW INTEREST AMOUNT */
    printf("\n\nShow Interest Amount\n");

    printf("\n***\n");
    printf("Interest Amount = 0.00\n");

    return 0;
}
