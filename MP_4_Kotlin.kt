/*********************
Last names: Hila, Katigbak, Mesa, Tumbocon
Language: Kotlin
Paradigm(s): Procedural
*********************/

/* --- Constants --- */
const val ANNUAL_INT_RATE = 0.05
const val BASE_CURR_CODE = "PHP"
const val BASE_CURR_NAME = "Philippine Peso"
const val BASE_CURR_RATE = 1.00   // base is always 1.00 against itself
const val DEFAULT_NAME = "New User"

/* --- Data Classes --- */
data class Currency(val currCode: String, val currName: String, var currRate: Double)
data class User(var name: String, var balance: Double)


/* --- Helper Functions for Input Checking  --- */
fun getString(prompt: String): String{
    print(prompt)
    var input = readln()
    while(input.isBlank()){
        print(prompt)
        input = readln()
    }
    return input
}

fun getDouble(prompt: String): Double{
    print(prompt)
    var input = readln()
    while(input.toDoubleOrNull() == null || input.toDouble() <= 0.00 || !(input.toDouble().isFinite())){
        print(prompt)
        input = readln()
    }
    return input.toDouble()
}

fun getInt(prompt: String, min: Int, max: Int): Int{
    print(prompt)
    var input = readln()
    while(input.toIntOrNull() == null || input.toIntOrNull() !in min..max){
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
        foreignCurr = getString(prompt).trim('[', ']')
    }
    return foreignCurr.toInt()
}

fun getForeignCurrency(currencyList: List<Currency>): Int{
    var foreignCurr = getCurrency("\nSelect Foreign Currency: ",currencyList)
    while(currencyList[foreignCurr-1].currCode == BASE_CURR_CODE) {
        println("${BASE_CURR_CODE} is the base currency, thus its rate cannot be changed. Please try again!")
        foreignCurr = getCurrency("Select Foreign Currency: ",currencyList)
    }
    return foreignCurr
}

fun verifyAccount(user: User): Boolean{
    if(user.name==DEFAULT_NAME){
        println("No account registered yet. Please register an account name and try again.")
        return false
    }
    if(getString("Account Name: ")!=user.name){
        println("Account name does not match the registered account. Please try again.")
        return false
    }
    return true
}

fun getYN(prompt: String): Boolean{
    var choice = getString(prompt)
    while(!(choice == "Y" ||choice == "y" ||choice == "N" ||choice == "n")){
        choice = getString(prompt)
    }
    if(choice == "Y" ||choice == "y") 
        return false
    else 
        return true
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
}

fun recordExchangedAmt(srcCurr: Int, excCurr: Int, input: Double, currencyList: List<Currency>): Double{
    var source: Double = currencyList[srcCurr-1].currRate * input
    var exchanged: Double = source/currencyList[excCurr-1].currRate
    return exchanged
}

/* --- Displaying Outputs --- */
fun displayTransaction(user: User){
    println("Updated Balance: %.2f".format(user.balance))
}

fun displayInterestAmt(days: Int, interest: Double, balance: Double){
    var updatedBal: Double
    var temp: Double = balance

    println("Day | Interest | Balance |")
    for(i in 1..days){
        updatedBal = temp+interest
        if(i<10)
            println("${i}   | %.2f     | %.2f |".format(interest,updatedBal))
        else
            println("${i}  | %.2f     | %.2f |".format(interest,updatedBal))
        temp = updatedBal
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

fun depositWithdrawMenu(choice: Int, user: User){
    println()
        var typeName = getType(choice) // For determining which menu will be used
    println("${typeName} Amount")
    if(verifyAccount(user)){
        println("Current Balance: %.2f".format(user.balance))
        println("Currency: ${BASE_CURR_CODE}")
        if(choice == 3 && user.balance<=0){
            println("There's nothing to withdraw in balance.")
        }
        else{
            var amount = getDouble("\n${typeName} Amount: ")
            while(!(choice == 2 || (choice == 3 && amount <= user.balance))){
                println("Amount is not valid. Please try again.")
                amount = getDouble("\n${typeName} Amount: ")
            }
            recordTransaction(choice, amount, user)
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
        exit = getYN("\nConvert another currency (Y/N)? ... ")
    }while(!exit)
}

fun showInterestMenu(user: User){
    println("Show Interest Amount")
    if(verifyAccount(user)){
        println("Current Balance: %.2f".format(user.balance))
        println("Currency: ${BASE_CURR_CODE}")
        println("Interest Rate: %d%%".format((ANNUAL_INT_RATE * 100).toInt()))
            var daysNum = getInt("\nTotal Number of Days: ", 1, 365)
            var dailyInterest = "%.2f".format(user.balance * (ANNUAL_INT_RATE / 365)).toDouble()
        println()
        displayInterestAmt(daysNum, dailyInterest, user.balance)
    }
}

/* --- Main Function --- */
fun main(){
    var user = User(DEFAULT_NAME, 00.00)
    var exit = false
    var currencyList = listOf( // list of currencies utilized
        Currency(BASE_CURR_CODE, BASE_CURR_NAME, BASE_CURR_RATE), // 1.00
        Currency("USD", "United States Dollar", 62.00), // 62.00
        Currency("JPY", "Japanese Yen", 0.40), // 0.40
        Currency("GBP", "British Pound Sterling", 84.00), // 84.00
        Currency("EUR", "Euro", 72.00), // 72.00
        Currency("CNY", "Chinese Yuan Renminni", 9.00) // 9.00
    )
    // Outputs the requirements needed
    while(!exit){
        var choice = mainMenu()
        when(choice){
            1 -> registerMenu(user)
            2 -> depositWithdrawMenu(2, user)
            3 -> depositWithdrawMenu(3, user)
            4 -> currencyExchangeMenu(currencyList)
            5 -> recordExchangeMenu(currencyList)
            6 -> showInterestMenu(user)
            7 -> exit = true
        }
        if (choice in 1..6) 
            exit = getYN("\nBack to the Main Menu (Y/N): ")
    }
}