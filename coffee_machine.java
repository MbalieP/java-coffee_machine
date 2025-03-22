import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CoffeeMachine {
    private static Map<String, Map<String, Object>> menu = new HashMap<>();
    private static Map<String, Integer> resources = new HashMap<>();
    private static int profit = 0;

    static {
        // Menu setup
        menu.put("latte", Map.of(
            "ingredients", Map.of("water", 500, "milk", 200, "coffee", 24),
            "cost", 150
        ));
        menu.put("espresso", Map.of(
            "ingredients", Map.of("water", 50, "coffee", 18),
            "cost", 100
        ));
        menu.put("cappuccino", Map.of(
            "ingredients", Map.of("water", 500, "milk", 200, "coffee", 24),
            "cost", 200
        ));

        // Resources setup
        resources.put("water", 500);
        resources.put("milk", 200);
        resources.put("coffee", 100);
    }

    public static boolean checkResources(Map<String, Integer> orderIngredients) {
        for (var item : orderIngredients.entrySet()) {
            if (item.getValue() > resources.getOrDefault(item.getKey(), 0)) {
                System.out.println("Sorry, there is not enough " + item.getKey() + ".");
                return false;
            }
        }
        return true;
    }

    public static int processNotes(Scanner scanner) {
        try {
            System.out.println("Please insert notes.");
            System.out.print("How many R10 notes?: ");
            int noteTen = scanner.nextInt();
            System.out.print("How many R20 notes?: ");
            int noteTwenty = scanner.nextInt();
            System.out.print("How many R50 notes?: ");
            int noteFifty = scanner.nextInt();
            return noteTen * 10 + noteTwenty * 20 + noteFifty * 50;
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter numeric values only.");
            scanner.nextLine(); // Clear the buffer
            return 0;
        }
    }

    public static boolean isPaymentSuccessful(int moneyReceived, int coffeeCost) {
        if (moneyReceived >= coffeeCost) {
            profit += coffeeCost;
            int change = moneyReceived - coffeeCost;
            System.out.println("Here is your R" + change + " in change.");
            return true;
        } else {
            System.out.println("Sorry, that's not enough money. Money refunded.");
            return false;
        }
    }

    public static void makeCoffee(String coffeeName, Map<String, Integer> coffeeIngredients) {
        for (var item : coffeeIngredients.entrySet()) {
            resources.put(item.getKey(), resources.get(item.getKey()) - item.getValue());
        }
        System.out.println("Here is your " + coffeeName + " ☕. Enjoy!");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isOn = true;

        while (isOn) {
            System.out.print("What would you like to have? (latte/espresso/cappuccino): ");
            String choice = scanner.next().toLowerCase();

            if ("off".equals(choice)) {
                isOn = false;
            } else if ("report".equals(choice)) {
                System.out.println("Water: " + resources.get("water") + "ml");
                System.out.println("Milk: " + resources.getOrDefault("milk", 0) + "ml");
                System.out.println("Coffee: " + resources.get("coffee") + "g");
                System.out.println("Money: R" + profit);
            } else if (menu.containsKey(choice)) {
                Map<String, Object> coffeeType = menu.get(choice);
                Map<String, Integer> ingredients = (Map<String, Integer>) coffeeType.get("ingredients");
                int cost = (int) coffeeType.get("cost");

                if (checkResources(ingredients)) {
                    int payment = processNotes(scanner);
                    if (isPaymentSuccessful(payment, cost)) {
                        makeCoffee(choice, ingredients);
                    }
                }
            } else {
                System.out.println("Invalid option. Please choose latte, espresso, or cappuccino.");
            }
        }

        scanner.close();
    }
}
