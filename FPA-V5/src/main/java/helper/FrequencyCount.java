package helper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FrequencyCount {

    private static final String FOODBASICS_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Foodbasics_Info_Data.json";
    private static final String METRO_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Metro_Info_Data.json";
    private static final String ZEHRS_JSON_PATH = "C:\\Users\\Monisha\\Downloads\\FPA-V5\\src\\main\\java\\org\\example\\Zehrs_Info_Data.json";

    private static int countFrequency(String jsonPath, String productName, List<String> productsContainingName) {
        int frequency = 0;
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String name = (String) jsonObject.get("productName");
                if (name.toLowerCase().contains(productName.toLowerCase())) {
                    frequency++;
                    productsContainingName.add(name);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return frequency;
    }

    public static void printFrequency(String storeName)
    {
        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the store name (Foodbasics, Metro, Zehrs): ");
//        String storeName = scanner.nextLine().toLowerCase();

        System.out.print("Enter the product name: ");
        String productName = scanner.nextLine();

        String jsonPath = "";
        switch (storeName) {
            case "foodbasics":
                jsonPath = FOODBASICS_JSON_PATH;
                break;
            case "metro":
                jsonPath = METRO_JSON_PATH;
                break;
            case "zehrs":
                jsonPath = ZEHRS_JSON_PATH;
                break;
            default:
                System.out.println("Invalid store name.");
                return;
        }

        List<String> productsContainingName = new ArrayList<>();
        int frequency = countFrequency(jsonPath, productName, productsContainingName);

        System.out.println("Frequency count for \"" + productName + "\" at " + storeName + ": " + frequency);


    }
    public static void main(String[] args) {
        }


}