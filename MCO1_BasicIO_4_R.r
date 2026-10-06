# ********************
# Last names: Hila, Katigbak, Mesa, Tumbocon
# Language: R
# Paradigm(s): Procedural, Imperative
# ********************

# input validator
read_number <- function(prompt) {
  value <- suppressWarnings(as.numeric(readline(prompt)))
  while (is.na(value)) {
    cat("Invalid input. Please enter a number.\n")
    value <- suppressWarnings(as.numeric(readline(prompt)))
  }
  return(value)
}

read_name <- function(prompt) {
  value <- trimws(readline(prompt))
  while (value == "") {
    cat("Invalid input. Name cannot be blank.\n")
    value <- trimws(readline(prompt))
  }
  return(value)
}

# Constants
DEFAULT_BALANCE  <- 1000
DEFAULT_CURRENCY <- "PHP"

currencies <- c(
  "Philippine Peso (PHP)",
  "United States Dollar (USD)",
  "Japanese Yen (JPY)",
  "British Pound Sterling (GBP)",
  "Euro (EUR)",
  "Chinese Yuan Renminni (CNY)"
)

# exchange values
exchange_values <- c(PHP = 1.00, USD = 62.00, JPY = 0.40,
                     GBP = 84.00, EUR = 72.00, CNY = 9.00)

print_currency_menu <- function() {
  for (i in 1:6) {
    cat(sprintf("[%d] %s\n", i, currencies[i]))
  }
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

  choice <- readline("Choice: ")
  while (!(choice %in% c("1", "2", "3", "4", "5", "6"))) {
    cat("Invalid choice. Please enter a number from 1 to 6.\n\n")
    choice <- readline("Choice: ")
  }

  cat("\n***\n")
  cat(sprintf("Choice = %s\n\n", choice))
  choice
}

# Register Account Name 
register_account_name <- function() {
  cat("Register Account Name\n")
  name <- read_name("Account Name: ")
 
  cat("\n***\n")
  cat(sprintf("Account Name = %s\n\n", name))
  name
}

# Deposit Amount
deposit_amount <- function() {
  cat("Deposit Amount\n")
  name <- read_name("Account Name: ")
  cat(sprintf("Current Balance: %.2f\n", DEFAULT_BALANCE))
  cat(sprintf("Currency: %s\n\n", DEFAULT_CURRENCY))

  amount <- read_number("Deposit Amount: ")

  cat("\n***\n")
  cat(sprintf("Account Name = %s\n", name))
  cat(sprintf("Deposit Amount = %.2f\n\n", amount))
}

# Withdraw Amount
withdraw_amount <- function() {
  cat("Withdraw Amount\n")
  name <- read_name("Account Name: ")
  cat(sprintf("Current Balance: %.2f\n", DEFAULT_BALANCE))
  cat(sprintf("Currency: %s\n\n", DEFAULT_CURRENCY))

  amount <- read_number("Withdraw Amount: ")

  cat("\n***\n")
  cat(sprintf("Account Name = %s\n", name))
  cat(sprintf("Withdraw Amount = %.2f\n\n", amount))
}

# Record Exchange Rate
record_exchange_rate <- function() {
  cat("Record Exchange Rate\n\n")
  print_currency_menu()
  cat("\n")

  selected <- readline("Select Foreign Currency: ")
  while (!(selected %in% c("1", "2", "3", "4", "5", "6"))) {
    cat("Invalid choice. Please enter a number from 1 to 6.\n\n")
    selected <- readline("Select Foreign Currency: ")
  }
  rate <- read_number("Exchange Rate: ")

  cat("\n***\n")
  cat(sprintf("Select Foreign Currency = [%s]\n", selected))
  cat(sprintf("Exchange Rate = %.2f\n\n", rate))
}

# Currency Exchange
currency_exchange <- function() {
  cat("Foreign Currency Exchange\n")
  amount <- read_number("Source Amount (PHP): ")

  cat("\nExchanged Currency\n")
  for (i in 1:6) {
    converted <- amount * exchange_values[i]
    cat(sprintf("[%d] %s = %.2f\n", i, currencies[i], converted))
  }

  cat("\n***\n")
  cat("Source Currency = Philippine Peso (PHP)\n")
  cat(sprintf("Source Amount (PHP) = %.2f\n", amount))
}

# main program call
main <- function() {
  main_menu()
  register_account_name()
  deposit_amount()
  withdraw_amount()
  record_exchange_rate()
  currency_exchange()
}

main()