# ********************
# Last names: Hila, Katigbak, Mesa, Tumbocon
# Language: R
# Paradigm(s): Procedural, Imperative
# ********************

# Constants
ANNUAL_INTEREST_RATE <- 0.05
DAYS_IN_YEAR         <- 365

currencies <- c(
  "Philippine Peso (PHP)",
  "United States Dollar (USD)",
  "Japanese Yen (JPY)",
  "British Pound Sterling (GBP)",
  "Euro (EUR)",
  "Chinese Yuan Renminni (CNY)"
)

currency_codes <- c("PHP", "USD", "JPY", "GBP", "EUR", "CNY")

# exchange values (PHP per 1 unit)
exchange_values <- c(PHP = 1.00, USD = 52.00, JPY = 0.35,
                     GBP = 70.00, EUR = 60.00, CNY = 7.20)

# account details (balance is always in PHP)
account <- new.env()
account$name    <- NULL
account$balance <- 0

# input validators
read_positive_number <- function(prompt) {
  value <- suppressWarnings(as.numeric(trimws(readline(prompt))))
  while (is.na(value) || !is.finite(value) || value <= 0) {
    cat("Invalid input. Please enter a number greater than 0.\n")
    value <- suppressWarnings(as.numeric(trimws(readline(prompt))))
  }
  return(value)
}

read_days <- function(prompt) {
  value <- suppressWarnings(as.numeric(trimws(readline(prompt))))
  while (is.na(value) || !is.finite(value) || value < 1 || value > 3650 || value != floor(value)) {
    cat("Invalid input. Please enter a whole number of days (1 to 3650).\n")
    value <- suppressWarnings(as.numeric(trimws(readline(prompt))))
  }
  return(as.integer(value))
}

read_name <- function(prompt) {
  value <- trimws(readline(prompt))
  while (value == "") {
    cat("Invalid input. Name cannot be blank.\n")
    value <- trimws(readline(prompt))
  }
  return(value)
}

read_choice <- function(prompt, valid, error_msg) {
  choice <- trimws(readline(prompt))
  while (!(choice %in% valid)) {
    cat(error_msg, "\n\n", sep = "")
    choice <- trimws(readline(prompt))
  }
  return(choice)
}

read_yes_no <- function(prompt) {
  answer <- read_choice(prompt, c("Y", "y", "N", "n"),
                        "Invalid input. Please enter Y or N.")
  return(toupper(answer) == "Y")
}

read_currency <- function(prompt) {
  choice <- read_choice(prompt, as.character(1:6),
                        "Invalid choice. Please enter a number from 1 to 6.")
  return(as.integer(choice))
}

# helper functions
round2 <- function(x) {
  return(floor(x * 100 + 0.5) / 100)
}

print_currency_menu <- function() {
  for (i in 1:6) {
    cat(sprintf("[%d] %s\n", i, currencies[i]))
  }
}

convert_currency <- function(amount, from, to) {
  return(amount * exchange_values[[from]] / exchange_values[[to]])
}

has_account <- function() {
  if (is.null(account$name)) {
    cat("No account registered yet. Please choose [1] Register Account Name first.\n\n")
    return(FALSE)
  }
  return(TRUE)
}

print_account_details <- function() {
  cat(sprintf("Account Name: %s\n", account$name))
  cat(sprintf("Current Balance: %.2f\n", account$balance))
  cat("Currency: PHP\n\n")
}

# Main Menu
main_menu <- function() {
  cat("Select Transaction:\n",
      "[1] Register Account Name\n",
      "[2] Deposit Amount\n",
      "[3] Withdraw Amount\n",
      "[4] Currency Exchange\n",
      "[5] Record Exchange Rates\n",
      "[6] Show Interest Amount\n\n",
      sep = "")

  choice <- read_choice("Choice: ", as.character(1:6),
                        "Invalid choice. Please enter a number from 1 to 6.")
  cat("\n")
  return(choice)
}

# Register Account Name
register_account_name <- function() {
  cat("Register Account Name\n")
  account$name <- read_name("Account Name: ")
  cat("\n")
  return(TRUE)
}

# Deposit Amount
deposit_amount <- function() {
  cat("Deposit Amount\n")
  if (!has_account()) return(TRUE)
  print_account_details()

  cat("Deposit Currency Options:\n")
  print_currency_menu()
  currency <- read_currency("Deposit Currency: ")
  amount <- read_positive_number("Deposit Amount: ")

  amount_php <- round2(convert_currency(amount, currency, 1))
  if (currency != 1) {
    cat(sprintf("Amount in PHP: %.2f (%.2f %s x %.2f)\n", amount_php, amount,
                currency_codes[currency], exchange_values[[currency]]))
  }

  account$balance <- round2(account$balance + amount_php)
  cat(sprintf("Updated Balance: %.2f\n\n", account$balance))
  return(TRUE)
}

# Withdraw Amount
withdraw_amount <- function() {
  cat("Withdraw Amount\n")
  if (!has_account()) return(TRUE)
  print_account_details()

  cat("Withdraw Currency Options:\n")
  print_currency_menu()
  currency <- read_currency("Withdraw Currency: ")
  amount <- read_positive_number("Withdraw Amount: ")

  amount_php <- round2(convert_currency(amount, currency, 1))
  if (currency != 1) {
    cat(sprintf("Amount in PHP: %.2f (%.2f %s x %.2f)\n", amount_php, amount,
                currency_codes[currency], exchange_values[[currency]]))
  }

  if (amount_php > account$balance) {
    cat(sprintf("Insufficient balance. Withdrawal of %.2f PHP exceeds %.2f PHP.\n",
                amount_php, account$balance))
  } else {
    account$balance <- round2(account$balance - amount_php)
  }
  cat(sprintf("Updated Balance: %.2f\n\n", account$balance))
  return(TRUE)
}

# Currency Exchange
currency_exchange <- function() {
  another <- TRUE
  while (another) {
    cat("Foreign Currency Exchange\n")
    cat("Source Currency Option:\n")
    print_currency_menu()
    cat("\n")
    from_currency <- read_currency("Source Currency: ")
    amount <- read_positive_number("Source Amount: ")

    cat("\nExchanged Currency Options:\n")
    print_currency_menu()
    cat("\n")
    to_currency <- read_currency("Exchange Currency: ")

    cat(sprintf("Exchange Amount: %.2f\n\n",
                convert_currency(amount, from_currency, to_currency)))

    another <- read_yes_no("Convert another currency (Y/N)? ")
    cat("\n")
  }
  return(TRUE)
}

# Record Exchange Rate
record_exchange_rate <- function() {
  cat("Record Exchange Rate\n\n")
  print_currency_menu()
  cat("\n")
  selected <- read_currency("Select Foreign Currency: ")

  if (selected == 1) {
    cat("PHP is the base currency; its rate is fixed at 1.00.\n\n")
    return(TRUE)
  }

  exchange_values[selected] <<- read_positive_number("Exchange Rate: ")
  cat(sprintf("Recorded: 1 %s = %.2f PHP\n\n", currency_codes[selected],
              exchange_values[[selected]]))
  return(TRUE)
}

# Show Interest Amount
show_interest_amount <- function() {
  cat("Show Interest Amount\n")
  if (!has_account()) return(TRUE)
  cat(sprintf("Account Name: %s\n", account$name))
  cat(sprintf("Current Balance: %.2f\n", account$balance))
  cat("Currency: PHP\n")
  cat(sprintf("Interest Rate: %g%%\n\n", ANNUAL_INTEREST_RATE * 100))

  days <- read_days("Enter Number of Days: ")
  cat(sprintf("\nTotal Number of Days: %d days\n\n", days))

  cat("Day | Interest | Balance |\n")
  balance <- account$balance
  for (day in seq_len(days)) {
    interest <- round2(balance * (ANNUAL_INTEREST_RATE / DAYS_IN_YEAR))
    balance  <- round2(balance + interest)
    cat(sprintf("%-3d | %-8.2f | %.2f |\n", day, interest, balance))
  }
  cat("\n")
  return(TRUE)
}

# main program call
main <- function() {
  repeat {
    choice <- main_menu()

    show_back <- switch(choice,
                        "1" = register_account_name(),
                        "2" = deposit_amount(),
                        "3" = withdraw_amount(),
                        "4" = currency_exchange(),
                        "5" = record_exchange_rate(),
                        "6" = show_interest_amount())

    if (show_back && !read_yes_no("Back to the Main Menu (Y/N): ")) {
      break
    }
    cat("\n")
  }
  cat("\nThank you for using the Banking and Currency Exchange Application.\n")
}

main()