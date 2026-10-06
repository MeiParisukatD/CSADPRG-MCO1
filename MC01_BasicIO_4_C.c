#include <stdio.h>
#include <string.h>

int main() {

    int choice;
    char accountName[100];
    double amount;
    double balance = 1000.00;
    int currencyChoice;
    double exchangeRate;

    while (1) {

        printf("\n");
        printf("Select Transaction:\n");
        printf("[1] Register Account Name\n");
        printf("[2] Deposit Amount\n");
        printf("[3] Withdraw Amount\n");
        printf("[4] Currency Exchange\n");
        printf("[5] Record Exchange Rates\n");
        printf("[6] Show Interest Amount\n");
        printf("Choice: ");
        scanf("%d", &choice);

        printf("\nChoice = %d\n\n", choice);

        switch (choice) {

            /* REGISTER ACCOUNT NAME */
            case 1:
                getchar();

                printf("Register Account Name\n");
                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);

                accountName[strcspn(accountName, "\n")] = '\0';

                printf("\nAccount Name = %s\n", accountName);
                break;


            /* DEPOSIT AMOUNT */
            case 2:
                getchar();

                printf("Deposit Amount\n");

                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);
                accountName[strcspn(accountName, "\n")] = '\0';

                printf("Current Balance: %.2f\n", balance);
                printf("Currency: PHP\n");

                printf("Deposit Amount: ");
                scanf("%lf", &amount);

                balance += amount;

                printf("\nAccount Name = %s\n", accountName);
                printf("Deposit Amount = %.2f\n", amount);
                printf("New Balance = %.2f\n", balance);

                break;


            /* WITHDRAW AMOUNT */
            case 3:
                getchar();

                printf("Withdraw Amount\n");

                printf("Account Name: ");
                fgets(accountName, sizeof(accountName), stdin);
                accountName[strcspn(accountName, "\n")] = '\0';

                printf("Current Balance: %.2f\n", balance);
                printf("Currency: PHP\n");

                printf("Withdraw Amount: ");
                scanf("%lf", &amount);

                if (amount <= balance) {

                    balance -= amount;

                    printf("\nAccount Name = %s\n", accountName);
                    printf("Withdraw Amount = %.2f\n", amount);
                    printf("New Balance = %.2f\n", balance);

                } else {

                    printf("\nInsufficient balance.\n");
                    printf("Current Balance = %.2f\n", balance);

                }

                break;


            /* CURRENCY EXCHANGE */
            case 4:

                printf("Foreign Currency Exchange\n");

                printf("Source Amount (PHP): ");
                scanf("%lf", &amount);

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

                printf("[6] Chinese Yuan Renminni (CNY) = %.2f\n",
                       amount * 9.00);

                printf("\nSource Currency = Philippine Peso (PHP)\n");
                printf("Source Amount (PHP) = %.2f\n", amount);

                break;


            /* RECORD EXCHANGE RATE */
            case 5:

                printf("Record Exchange Rate\n");

                printf("[1] Philippine Peso (PHP)\n");
                printf("[2] United States Dollar (USD)\n");
                printf("[3] Japanese Yen (JPY)\n");
                printf("[4] British Pound Sterling (GBP)\n");
                printf("[5] Euro (EUR)\n");
                printf("[6] Chinese Yuan Renminni (CNY)\n");

                printf("Select Foreign Currency: ");
                scanf("%d", &currencyChoice);

                printf("Exchange Rate: ");
                scanf("%lf", &exchangeRate);

                printf("\nSelect Foreign Currency = [%d]\n",
                       currencyChoice);

                printf("Exchange Rate = %.2f\n",
                       exchangeRate);

                break;


            /* SHOW INTEREST AMOUNT */
            case 6:

                printf("Show Interest Amount\n");

                printf("Interest Amount = 0.00\n");

                break;


            /* INVALID CHOICE */
            default:

                printf("Invalid choice. Please select 1-6.\n");

                break;
        }

        printf("\n----------------------------------------\n");
    }

    return 0;
}
