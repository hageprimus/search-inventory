import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class Inventory {
    public static void main(String[] args) throws IOException {
        //Create inventory from provided file
        List<Product> inventory = populateInventory("inventory.csv");

        //Menu variables
        Scanner s = new Scanner(System.in);
        String userInput = "";
        int command = 0;
        boolean exitFlag = false;
        String menu = """
                What do you want to do?
                1 - List all products
                2 - Lookup a product by its id
                3 - Find all products within a price range
                4 - Add a new product
                0 - Quit the application
                Enter command:
                """;

        //Program loop
        do {
            userInput = getInputFromConsole(menu, s, false);
            command = !userInput.isEmpty() ? Integer.parseInt(userInput) : 0;
            switch (command) {
                case 1 ->   listAllProducts(inventory);
                case 2 ->   searchProductsById(inventory, s);
                case 3 ->   searchProductsByPrice(inventory, s);
                case 4 ->   addNewProduct(inventory, s);
                default -> {
                    System.out.println("Exiting application.");
                    exitFlag = true;
                }
            }
        } while (!exitFlag);
    }

    private static void listAllProducts(List<Product> inventory) {
        //Collections.sort()
        for (Product p : inventory) {
            System.out.println(p.displayProductInfo());
        }
    }

    private static void searchProductsById(List<Product> inventory, Scanner s) {
        List<Product> results = new ArrayList<>();
        String menu = "Enter the product ID to search for.";
        String userInput = getInputFromConsole(menu, s, false);
        try {
            int searchID = Integer.parseInt(userInput);
            for (Product p : inventory) {
                if (p.getId() == searchID)
                    results.add(p);
            }
            for (Product p : results) {
                System.out.println(p.displayProductInfo());
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            e.printStackTrace();
        }
    }

    private static void searchProductsByPrice(List<Product> inventory, Scanner s) {
        List<Product> results = new ArrayList<>();
        String menu = """
                Enter the price range to search for.
                Use format (low-high).
                """;
        String userInput = getInputFromConsole(menu, s, false);
        try {
            String[] tokens = userInput.split("-");
            double[] range = {Double.parseDouble(tokens[0].trim()), Double.parseDouble(tokens[1].trim())};
            Arrays.sort(range);
            for (Product p : inventory) {
                if (p.getPrice() >= range[0] && p.getPrice() <= range[1])
                    results.add(p);
            }
            for (Product p : results) {
                System.out.println(p.displayProductInfo());
            }
        } catch (Exception e) {
            System.out.println("Invalid input!");
            e.printStackTrace();
        }
    }

    private static List<Product> populateInventory(String fileToRead) {
        List<Product> inventory = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/" + fileToRead));
            do {
                String fileLine = reader.readLine();
                String[] tokens = fileLine.split("\\|");
                Product p = new Product(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2]));
                inventory.add(p);
            } while (reader.readLine() != null) ;
        } catch (IOException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        return inventory;
    }

    private static void addNewProduct(List<Product> inventory, Scanner s) {
        String menu = """
                Enter the product details to add to the list.
                Use format (id,name,price).
                """;
        String userInput = getInputFromConsole(menu, s, false);
        String[] tokens = userInput.split(",");
        Product p = new Product(Integer.parseInt(tokens[0].trim()), tokens[1].trim(), Double.parseDouble(tokens[2].trim()));
        inventory.add(p);
    }

    private static String getInputFromConsole(String display, Scanner s, boolean clearBuffer) {
        if (clearBuffer) s.nextLine();
        System.out.println(display);
        return s.nextLine().trim();
    }
}
