package machine

import java.util.Scanner

fun main() {
    val machineContents = MachineContents(400,540,120,9,550)
    val myFreshCoffeeMachine = CoffeeMachine(machineContents)
    myFreshCoffeeMachine.runMachine()
}

data class MachineContents(var water: Int, var milk: Int, var coffeeBeans: Int, var cups: Int, var money: Int)

enum class CoffeeTypes(val coffeeName: String, val water: Int, val milk: Int, val coffeeBeans: Int, val cups: Int, val money: Int) {
    ESPRESSO("Espresso", 250, 0, 16, 1, 4),
    LATTE("Latte", 350, 75, 20, 1, 7),
    CAPPUCCINO("Cappuccino", 200, 100, 12, 1, 6);

    fun buyCoffee(ingredients: MachineContents): MachineContents {
        when {
            ingredients.water < this.water -> println("Sorry, not enough water")
            ingredients.milk < this.milk -> println("Sorry, not enough milk")
            ingredients.coffeeBeans < this.coffeeBeans -> println("Sorry, not enough coffee beans")
            ingredients.cups < this.cups -> println("Sorry, not enough cups")
            else -> {
                println("I have enough resources, making you a coffee")
                ingredients.coffeeBeans -= this.coffeeBeans
                ingredients.water -= this.water
                ingredients.milk -= this.milk
                ingredients.cups -= this.cups
                ingredients.money += this.money
            }
        }
        return ingredients
    }
}

enum class MachineStatus(val description: String) {
    ACTION("choosing an action"),
    BUY("choosing a variant of coffee"),
    INGREDIENTS("displaying ingredient status"),
    FILL("filling machine with ingredients"),
    TAKE("taking money from machine"),
    EXIT("switch off machine"),
    ERROR("unknown action")
}

class CoffeeMachine(var machineContents: MachineContents) {

    var status: MachineStatus = MachineStatus.ACTION
    var scanner = Scanner(System.`in`)

    fun runMachine() {
        while (status != MachineStatus.EXIT) {
            when (status) {
                MachineStatus.ACTION -> this.action()
                MachineStatus.BUY -> this.buy()
                MachineStatus.FILL -> this.fill()
                MachineStatus.INGREDIENTS -> this.ingredientStatus()
                MachineStatus.TAKE -> this.take()
                else -> println("Invalid Input")
            }
        }
    }

    private fun buy() {
        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        val choice = scanner.next()
        this.status = MachineStatus.ACTION
        machineContents = when (choice) {
            "1" -> CoffeeTypes.ESPRESSO.buyCoffee(this.machineContents)
            "2" -> CoffeeTypes.LATTE.buyCoffee(this.machineContents)
            "3" -> CoffeeTypes.CAPPUCCINO.buyCoffee(this.machineContents)
            else -> return
        }
    }

    private fun action() {
        println("Write action (buy, fill, take, remaining, exit): ")
        val choice = scanner.next()
        this.status = when (choice) {
            "buy" -> MachineStatus.BUY
            "fill" -> MachineStatus.FILL
            "take" -> MachineStatus.TAKE
            "remaining" -> MachineStatus.INGREDIENTS
            "exit" -> MachineStatus.EXIT
            else -> MachineStatus.ERROR
        }
    }

    private fun ingredientStatus() {
        println("The coffee machine has:")
        println("${machineContents.water} of water")
        println("${machineContents.milk} of milk")
        println("${machineContents.coffeeBeans} of coffee beans")
        println("${machineContents.cups} of disposable cups")
        println("${machineContents.money} of money")
        this.status = MachineStatus.ACTION
    }

    private fun take() {
        println("I gave you $${machineContents.money}")
        machineContents.money = 0
        this.status = MachineStatus.ACTION
    }

    private fun fill() {
        println("Write how many ml of water do you want to add: ")
        machineContents.water += scanner.nextInt()
        println("Write how many ml of milk do you want to add: ")
        machineContents.milk += scanner.nextInt()
        println("Write how many grams of coffee beans do you want to add: ")
        machineContents.coffeeBeans += scanner.nextInt()
        println("Write how many disposable cups of coffee do you want to add: ")
        machineContents.cups += scanner.nextInt()
        this.status = MachineStatus.ACTION
    }

}

