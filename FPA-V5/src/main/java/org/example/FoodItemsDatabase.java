package org.example;

import helper.InvertedIndex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodItemsDatabase {

    static HashMap<String, Long> foodItems = new HashMap<>();
    static List<Product> foodList = new ArrayList<>();
    static List<String> invertedIndexedList = new ArrayList<>();
    public static HashMap<String, Long> populateFoodItems() {


        JSONParser parser = new JSONParser();
        try {
            JSONArray productJsonArray = (JSONArray) parser.parse(new FileReader("src\\main\\java\\org\\example\\scratch.json"));
            for (Object obj : productJsonArray)
            {
                JSONObject js = (JSONObject)obj;
                String productname = (String) js.get("productName");
                Long productprice = (Long) js.get("productPrice");
                Product productObject = new Product(productname.toLowerCase(),productprice);
                foodList.add(productObject);
                foodItems.put(productname.toLowerCase(),productprice);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return foodItems;
    }

    public static List<String> indexedIndexing(String item)
    {
        InvertedIndex inv = new InvertedIndex();
        inv.buildIndex(foodList);
        for (Product p : inv.getProducts(item.toLowerCase()))
        {
           // System.out.println("Value:"+p.productName);
            invertedIndexedList.add(p.productName);
        }
        return invertedIndexedList ;
    }

    public static void main(String[] args)
    {

        populateFoodItems();
    }

}