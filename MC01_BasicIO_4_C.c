#include <stdio.h>
#include <string.h>

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
    printf("Choice: ");

    if (scanf("%d", &choice) != 1) {
        printf("Invalid input. Please enter a number from 1-6.\n");
        return 0;
    }

    printf("\nChoice = %d\n\n", choice);

    switch (choice) {

        /* REGISTER ACCOUNT NAME */
        case 1:
            printf("***\n");
            getchar();

            printf("Register Account Name\n");

            do {
                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);

                accountName[strcspn(accountName, "\n")] = '\0';

                if (strlen(accountName) == 0) {
                    printf("Account name cannot be empty. Please try again.\n");
                }

            } while (strlen(accountName) == 0);

            printf("\nAccount Name = %s\n", accountName);

            break;


        /* DEPOSIT AMOUNT */
        case 2:
            printf("***\n");
            getchar();

            printf("Deposit Amount\n");

            do {
                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);

                accountName[strcspn(accountName, "\n")] = '\0';

                if (strlen(accountName) == 0) {
                    printf("Account name cannot be empty. Please try again.\n");
                }

            } while (strlen(accountName) == 0);

            printf("Current Balance: %.2f\n", balance);
            printf("Currency: PHP\n");

            do {
                printf("Deposit Amount: ");

                if (scanf("%lf", &amount) != 1) {
                    printf("Invalid input. Please enter a valid amount.\n");

                    while (getchar() != '\n');
                    amount = -1;
                }

                if (amount < 0) {
                    printf("Deposit amount cannot be negative.\n");
                }

            } while (amount < 0);

            balance += amount;

            printf("\nAccount Name = %s\n", accountName);
            printf("Deposit Amount = %.2f\n", amount);

            break;


        /* WITHDRAW AMOUNT */
        case 3:
            printf("***\n");
            getchar();

            printf("Withdraw Amount\n");

            do {
                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);

                accountName[strcspn(accountName, "\n")] = '\0';

                if (strlen(accountName) == 0) {
                    printf("Account name cannot be empty. Please try again.\n");
                }

            } while (strlen(accountName) == 0);

            printf("Current Balance: %.2f\n", balance);
            printf("Currency: PHP\n");

            do {
                printf("Withdraw Amount: ");

                if (scanf("%lf", &amount) != 1) {
                    printf("Invalid input. Please enter a valid amount.\n");

                    while (getchar() != '\n');
                    amount = -1;
                }

                if (amount < 0) {
                    printf("Withdrawal amount cannot be negative.\n");
                }

            } while (amount < 0);

            if (amount <= balance) {

                balance -= amount;

                printf("\nAccount Name = %s\n", accountName);
                printf("Withdraw Amount = %.2f\n", amount);

            } else {

                printf("\nInsufficient balance.\n");

            }

            break;


        /* CURRENCY EXCHANGE */
        case 4:
            printf("***\n");

            printf("Foreign Currency Exchange\n");

            do {
                printf("Source Amount (PHP): ");

                if (scanf("%lf", &amount) != 1) {
                    printf("Invalid input. Please enter a valid amount.\n");

                    while (getchar() != '\n');
                    amount = -1;
                }

                if (amount < 0) {
                    printf("Amount cannot be negative.\n");
                }

            } while (amount < 0);

            printf("\nExchanged Currency\n");

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

            printf("[6] Chinese Yuan Renminbi (CNY) = %.2f\n",
                   amount * 9.00);

            printf("\nSource Currency = Philippine Peso (PHP)\n");
            printf("Source Amount (PHP) = %.2f\n", amount);

            break;


        /* RECORD EXCHANGE RATE */
        case 5:
            printf("***\n");

            printf("Record Exchange Rate\n");

            printf("[1] Philippine Peso (PHP)\n");
            printf("[2] United States Dollar (USD)\n");
            printf("[3] Japanese Yen (JPY)\n");
            printf("[4] British Pound Sterling (GBP)\n");
            printf("[5] Euro (EUR)\n");
            printf("[6] Chinese Yuan Renminbi (CNY)\n");

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
                    exchangeRate = -1;
                }

                if (exchangeRate < 0) {
                    printf("Exchange rate cannot be negative.\n");
                }

            } while (exchangeRate < 0);

            printf("\nSelect Foreign Currency = [%d]\n",
                   currencyChoice);

            printf("Exchange Rate = %.2f\n",
                   exchangeRate);

            break;


        /* SHOW INTEREST AMOUNT */
        case 6:
            printf("***\n");

            printf("Show Interest Amount\n");
            printf("Interest Amount = 0.00\n");

            break;


        default:
            printf("***\n");

            printf("Invalid choice. Please select 1-6.\n");

            break;
    }

    return 0;
}
