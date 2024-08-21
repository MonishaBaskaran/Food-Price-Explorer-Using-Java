package helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PageRanking {

    private static final String FOODBASICS_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Foodbasics_Info_Data.json";
    private static final String METRO_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Metro_Info_Data.json";
    private static final String ZEHRS_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Zehrs_Info_Data.json";

    private static JSONArray loadJsonData(String filePath) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            return (JSONArray) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean productExists(JSONArray products, String productName) {
        for (Object obj : products) {
            JSONObject product = (JSONObject) obj;
            String name = (String) product.get("productName");
            if (name.equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }



    private static void displayTopRankedPages(Map<String, Map<String, Integer>> pageScores) {
        // Create a map to store the product names and their corresponding scores for each store
        Map<String, Map<String, Integer>> sortedPages = new HashMap<>();

        // Iterate through each store
        for (Map.Entry<String, Map<String, Integer>> entry : pageScores.entrySet()) {
            // Sort the products based on their scores in descending order
            Map<String, Integer> sortedProducts = entry.getValue().entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            sortedPages.put(entry.getKey(), sortedProducts);
        }

        // Display the top ranked pages
        for (Map.Entry<String, Map<String, Integer>> entry : sortedPages.entrySet()) {
            for (Map.Entry<String, Integer> productEntry : entry.getValue().entrySet()) {
                System.out.println(entry.getKey() + " - " + productEntry.getKey() + ": " + productEntry.getValue());
            }
        }
    }
    public static void printPageRanking()
    {
        Scanner scanner = new Scanner(System.in);

        // Load JSON data for Foodbasics, Metro, and Zehrs
        JSONArray foodbasicsProducts = loadJsonData(FOODBASICS_JSON_PATH);
        JSONArray metroProducts = loadJsonData(METRO_JSON_PATH);
        JSONArray zehrsProducts = loadJsonData(ZEHRS_JSON_PATH);

        if (foodbasicsProducts != null && metroProducts != null && zehrsProducts != null) {
            // Map to store product search counts for each store
            Map<String, JSONArray> storeProductsMap = new HashMap<>();
            storeProductsMap.put("foodbasics", foodbasicsProducts);
            storeProductsMap.put("metro", metroProducts);
            storeProductsMap.put("zehrs", zehrsProducts);

            // Map to store page scores for each product at each store
            Map<String, Map<String, Integer>> pageScores = new HashMap<>();

            while (true) {
                // Prompt user to enter store name
                System.out.print("Enter the store name (e.g., Foodbasics, Metro, Zehrs): ");
                String storeName = scanner.nextLine().toLowerCase();

                // Check if the entered store name is valid
                if (!storeProductsMap.containsKey(storeName)) {
                    System.out.println("Invalid store name. Please enter a valid store name.");
                    continue;
                }

                // Prompt user to enter product name
                System.out.print("Enter the product name: ");
                String productName = scanner.nextLine().toLowerCase();

                // Check if the entered product name exists in the store
                if (!productExists(storeProductsMap.get(storeName), productName)) {
                    System.out.println("Product not found at " + storeName + ". Please enter a valid product name.");
                    continue;
                }

                // Increment the page score for the product at the specified store
                int score = pageScores.computeIfAbsent(storeName, k -> new HashMap<>())
                        .merge(productName, 1, Integer::sum);

                // Display the page score for the product at the specified store
                System.out.println("Page Score for " + productName + " at " + storeName + ": " + score);



                // Ask if the user wants to search for another product
                System.out.print("Do you want to search for another product? (yes/no): ");
                String response = scanner.nextLine().toLowerCase();
                if (!response.equals("yes")) {
                    break;
                }
            }

            // Display top-ranked pages after the user has finished searching
            System.out.println("Top Ranked Pages:");
            displayTopRankedPages(pageScores);
        } else {
            System.out.println("Failed to load JSON data.");
        }
    }
    public static void main(String[] args) {

    }


}