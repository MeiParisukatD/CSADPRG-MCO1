/*********************
Last names: Hila, Katigbak, Mesa, Tumbocon
Language: Kotlin
Paradigm(s): Procedural
*********************/
import java.util.Locale

/* --- Constants --- */
const val ANNUAL_INT_RATE = 0.05
const val BASE_CURR_CODE = "PHP"
const val BASE_CURR_NAME = "Philippine Peso"
const val BASE_CURR_RATE = 1.00 


/* --- Data Classes --- */
data class Currency(val currCode: String, val currName: String, var currRate: Double)
data class User(var name: String, var balance: Double, var registered: Boolean)


/* --- Helper Functions for Input Checking  --- */
fun getString(prompt: String): String{
    print(prompt)
    var input = readln().trim()
    while(input.isBlank()){
        println("Invalid input. Please try again!")
        print(prompt)
        input = readln().trim()
    }
    return input
}

fun getDouble(prompt: String): Double{
    print(prompt)
    var input = readln()
    while(input.toDoubleOrNull() == null || input.toDouble() <= 0.00 || !(input.toDouble().isFinite())){
        println("Invalid input. Please try again!")
        print(prompt)
        input = readln()
    }
    return input.toDouble()
}

fun getInt(prompt: String, min: Int, max: Int): Int{
    print(prompt)
    var input = readln()
    while(input.toIntOrNull() == null || input.toIntOrNull() !in min..max){
        println("Invalid input. Please try again!")
        print(prompt)
        input = readln()
    }
    return input.toInt()
}

fun getType(choice: Int): String{ // For deposit/withdraw menu 
    if (choice == 2) return "Deposit" 
    else return "Withdraw"
}

fun getCurrency(prompt: String, currencyList: List<Currency>): Int{
    var foreignCurr = getString(prompt).trim('[', ']')
    while(foreignCurr.toIntOrNull() == null || foreignCurr.toIntOrNull() !in 1..currencyList.size){
        println("Invalid input. Please try again!")
        foreignCurr = getString(prompt).trim('[', ']')
    }
    return foreignCurr.toInt()
}

fun getForeignCurrency(currencyList: List<Currency>): Int{
    var foreignCurr = getCurrency("\nSelect Foreign Currency: ",currencyList)
    while(currencyList[foreignCurr-1].currCode == BASE_CURR_CODE){
        println("${BASE_CURR_CODE} is the base currency, thus its rate cannot be changed.")
        foreignCurr = getCurrency("Select Foreign Currency: ",currencyList)
    }
    return foreignCurr
}

fun verifyAccount(user: User): Boolean{
    if(!user.registered) println("No account registered yet. Please register an account name and try again.")
    else println("Account Name: ${user.name}")
    return user.registered
}

fun getYN(prompt: String): Boolean{
    var choice = getString(prompt)
    while(!(choice == "Y" ||choice == "y" ||choice == "N" ||choice == "n")){
        choice = getString(prompt)
    }
    if(choice == "Y" ||choice == "y") return true
    else return false
}


/* --- Transactions --- */
fun recordRate(input: Double, index: Int, currencyList: List<Currency>){ // records rates of selected currency
    var c = currencyList[index-1]
    if (!(c.currCode == BASE_CURR_CODE))
        c.currRate = input
}

fun recordTransaction(choice: Int, input: Double, user: User){
    if(choice == 2) user.balance += input
    else user.balance -= input
    user.balance = "%.2f".format(user.balance).toDouble()
}

fun recordName(input: String, user: User){
    user.name = input
    user.registered = true
}

fun recordExchangedAmt(srcCurr: Int, excCurr: Int, input: Double, currencyList: List<Currency>): Double{
    var source: Double = currencyList[srcCurr-1].currRate * input
    var exchanged: Double = source/currencyList[excCurr-1].currRate
    return exchanged
}

fun amtToPHP(amount: Double, currIndex: Int, currencyList: List<Currency>): Double{
    return (amount * currencyList[currIndex-1].currRate)
}


/* --- Displaying Outputs --- */
fun displayTransaction(user: User){
    println("Updated Balance: %.2f".format(user.balance))
}

fun displayInterestAmt(days: Int, balance: Double){
    var updatedBal = balance
    var interest: Double 

    println("\nTotal Number of Days: ${days} days\n")
    println("Day".padEnd(6) + " | " + "Interest".padEnd(6) + " | " + "Balance".padEnd(6) + " |")
    for(i in 1..days){
        interest = "%.2f".format(updatedBal * (ANNUAL_INT_RATE / 365)).toDouble()
        updatedBal = "%.2f".format(updatedBal + interest).toDouble()
        println("${i.toString().padEnd(4)}   | %.2f     | %.2f |".format(interest,updatedBal))
    }
}

fun displayExchangedAmt(amount: Double){
    println("Exchange Amount: %.2f".format(amount))
}

fun displayCurrencies(currencyList: List<Currency>){
    for ((index, c) in currencyList.withIndex()){
            println("[${index+1}] ${c.currName} (${c.currCode})")
        }
}


/* --- Menu Types --- */
fun mainMenu(): Int{
    println()
    println("Select Transaction:")
    println("[1] Register Account Name")
    println("[2] Deposit Amount")
    println("[3] Withdraw Amount")
    println("[4] Currency Exchange")
    println("[5] Record Exchange Rates")
    println("[6] Show Interest Amount")
    println("[7] Exit")
        var choice = getInt("\nChoice: ", 1, 7)
    return choice
}

fun registerMenu(user: User){
    println()
    println("Register Account Name")
    recordName(getString("Account Name: "), user)
}

fun depositWithdrawMenu(choice: Int, user: User, currencyList: List<Currency>){
    println()
    var typeName = getType(choice) // For determining which menu will be used
    println("${typeName} Amount")
    if(verifyAccount(user)){
        println("Current Balance: %.2f".format(user.balance))
        println()
        displayCurrencies(currencyList)
            var currChoice = getCurrency("\nCurrency: ", currencyList)
        println("Currency: ${currencyList[currChoice-1].currCode}")
            var amount = getDouble("\n${typeName} Amount: ")
            var amtPHP = amtToPHP(amount, currChoice, currencyList)
        if(choice == 3 && amtPHP > user.balance)
            println("Insufficient balance. Transaction cancelled.")
        else{
            recordTransaction(choice, amtPHP, user)
            displayTransaction(user)
        }
    }
}

fun recordExchangeMenu(currencyList: List<Currency>){
    println("\nRecord Exchange Rate\n")
    displayCurrencies(currencyList)
        var foreignCurr = getForeignCurrency(currencyList)
        var excRate = getDouble("Exchange Rate: ")
    recordRate(excRate,foreignCurr, currencyList)
}

fun currencyExchangeMenu(currencyList: List<Currency>){
    var exit: Boolean
    do{
        println("\nForeign Currency Exchange")
        println("Source Currency Option:")
        displayCurrencies(currencyList)
            var srcCurr = getCurrency("\nSource Currency: ", currencyList)
            var srcAmt = getDouble("Source Amount: ")
        println("\nExchanged Currency Options:")
        displayCurrencies(currencyList)
            var excCurr = getCurrency("\nExchange Currency: ", currencyList)
            var excAmt = recordExchangedAmt(srcCurr, excCurr, srcAmt, currencyList)
        displayExchangedAmt(excAmt)
        exit = !getYN("\nConvert another currency (Y/N)? ... ")
    }while(!exit)
}

fun showInterestMenu(user: User){
    println()
    println("Show Interest Amount")
    if(verifyAccount(user)){
        println("Current Balance: %.2f".format(user.balance))
        println("Currency: ${BASE_CURR_CODE}")
        println("Interest Rate: %d%%".format((ANNUAL_INT_RATE * 100).toInt()))
            var daysNum = getInt("\nTotal Number of Days: ", 1, 3650)
        displayInterestAmt(daysNum, user.balance)
    }
}


/* --- Main Function --- */
fun main(){
    Locale.setDefault(Locale.US) // ensures consistent formatting for double formats
    var user = User("", 00.00, false)
    var exit = false
    var currencyList = listOf( 
        Currency(BASE_CURR_CODE, BASE_CURR_NAME, BASE_CURR_RATE), 
        Currency("USD", "United States Dollar", 52.00), 
        Currency("JPY", "Japanese Yen", 0.35),
        Currency("GBP", "British Pound Sterling", 70.00),
        Currency("EUR", "Euro", 60.00),
        Currency("CNY", "Chinese Yuan Renminni", 7.20)
    )
    while(!exit){
        var choice = mainMenu()
        when(choice){
            1 -> registerMenu(user)
            2 -> depositWithdrawMenu(2, user, currencyList)
            3 -> depositWithdrawMenu(3, user, currencyList)
            4 -> currencyExchangeMenu(currencyList)
            5 -> recordExchangeMenu(currencyList)
            6 -> showInterestMenu(user)
            7 -> exit = true
        }
        if(choice in 1..6)
            exit = !getYN("\nBack to the Main Menu (Y/N): ")
    }
    println("\nThank you for using the Banking and Currency Exchange Application.\n")
}
