/*********************
Last names: Hila, Katigbak, Mesa, Tumbocon
Language: Kotlin
Paradigm(s): Procedural
*********************/

// For initializing currency list
data class Currency(val currCode: String, val currName: String, var currRate: Double) 

/* --- Helper Functions --- */
fun divider(){
    println()
    println("***")
}

fun getChoice(): Int{
    var input = readln()
    while (input.toIntOrNull()== null || !(input.toInt()>=1 && input.toInt()<=6)) // Ensures input is not null & is only b/w 1-6
        input = readln()
    return input.toInt()
}

fun getString(): String{
    var input = readln()
    while (input.isBlank()) // Ensures input is not blank
        input = readln()
    return input
}

fun getDouble(): Double{
    var input = readln()
    while (input.toDoubleOrNull() == null) // Ensures input is not null
        input = readln()
    return input.toDouble()
}

fun findType(choice: Int): String{ // For deposit/withdraw menu 
    if (choice == 2) return "Deposit" 
    else return "Withdraw"
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
    println()
    print("Choice: ")
        var choice = getChoice()
    divider()
    println("Choice = $choice")
    return choice
}

fun registerMenu(){
    println()
    println("Register Account Name")
    print("Account Name: ")
        var name = getString()
    divider()
    println("Account Name = $name")
}

fun depositWithdrawMenu(choice: Int){
    println()
    var typeName = findType(choice) // For determining which menu will be used
    println("${typeName} Amount")
    print("Account Name: ")
        var name = getString()
    println("Current Balance: 1000.00")
    println("Currency: PHP")
    println()
    print("${typeName} Amount: ")
        var amount = getDouble()
    divider()
    println("Account Name = $name")
    println("${typeName} Amount = %.2f".format(amount))
}

fun recordExchangeMenu(currencyList: List<Currency>){
    println()
    println("Record Exchange Rate")
    println()
    // Showcases all currencies with their respective codes 
    for ((index, currVals) in currencyList.withIndex()) {
        println("[${index+1}] ${currVals.currName} (${currVals.currCode})")
    }
    println()
    print("Select Foreign Currency: ")
        var foreignCurr = getString().trim('[', ']')
    while (foreignCurr.toIntOrNull()== null || !(foreignCurr.toInt() in 1..currencyList.size)){ 
        foreignCurr = getString().trim('[', ']')}
    print("Exchange Rate: ")
        var excRate = getDouble()
    divider()
    println("Select Foreign Currency = [${foreignCurr}]")
    println("Exchange Rate = %.2f".format(excRate))
}

fun currencyExchangeMenu(currencyList: List<Currency>){
    println()
    println("Foreign Currency Exchange")
    print("Source Amount (PHP): ")
        var srcAmt = getDouble()
    println()
    println("Exchanged Currency")
    // Showcases all currencies with their respective codes and amount conversions based on currRate
    for ((index, currVals) in currencyList.withIndex()){
        println("[${index+1}] ${currVals.currName} (${currVals.currCode}) = %.2f".format(srcAmt*currVals.currRate))
    }
    divider()
    println("Source Currency = Philippine Peso (PHP)")
    println("Source Amount (PHP) = %.2f".format(srcAmt))
}

/* --- Main Function --- */
fun main(){
    var currencyList = listOf( // list of currencies utilized
        Currency("PHP", "Philippine Peso", 1.00),
        Currency("USD", "United States Dollar", 62.00),
        Currency("JPY", "Japanese Yen", 0.40),
        Currency("GBP", "British Pound Sterling", 84.00),
        Currency("EUR", "Euro", 72.00),
        Currency("CNY", "Chinese Yuan Renminni", 9.00)
    )
    // Outputs the requirements needed
    mainMenu()
    registerMenu()
    depositWithdrawMenu(2)
    depositWithdrawMenu(3)
    recordExchangeMenu(currencyList)
    currencyExchangeMenu(currencyList)
}
