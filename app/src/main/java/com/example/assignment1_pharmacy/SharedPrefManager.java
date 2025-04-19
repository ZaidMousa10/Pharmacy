package com.example.assignment1_pharmacy;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;

public class SharedPrefManager {
    private static final String PREF_NAME = "cart_preferences";
    private static final String CART_KEY = "cart";
    public static final String PRODUCTS_KEY = "products";

    public static ArrayList<Product> getProducts(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = prefs.getString(PRODUCTS_KEY, "");
        if (json.isEmpty()) return loadInitialProducts(ctx);

        Gson gson = new Gson();
        Product[] productArray = gson.fromJson(json, Product[].class);

        ArrayList<Product> productList = new ArrayList<>();
        if (productArray != null) {
            Collections.addAll(productList, productArray);
        }

        return productList;
    }


    public static void saveProducts(Context ctx, ArrayList<Product> products) {
        Gson gson = new Gson();
        String json = gson.toJson(products);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PRODUCTS_KEY, json);
        editor.apply();
    }


    public static void decreaseStock(Context ctx, Product p, int quantity) {
        ArrayList<Product> products = getProducts(ctx);
        for (Product prod : products) {
            if (prod.getId() == p.getId()) {
                prod.setStock(prod.getStock() - quantity);
                break;
            }
        }
        saveProducts(ctx, products);
    }
    public static void saveCart(Context context, ArrayList<Product> cart) {
        Gson gson = new Gson();
        String json = gson.toJson(cart);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CART_KEY, json);
        editor.apply();
    }


    // Clear the cart from SharedPreferences
    public static void clearCart(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(CART_KEY);
        editor.apply();
    }

    private static ArrayList<Product> loadInitialProducts(Context ctx) {
        ArrayList<Product> list = new ArrayList<>();
        list.add(new Product(1, "Panadol Extra", 3.5, 10, R.drawable.panadol));
        list.add(new Product(2, "Zoloft", 5.0, 8, R.drawable.zoloft));
        list.add(new Product(3, "Amlodipine", 7.0, 15, R.drawable.amlodipine));
        list.add(new Product(4, "Amoxicillin", 8.0, 8, R.drawable.amoxicillin));
        list.add(new Product(5, "Atenolol", 7.0, 20, R.drawable.atenolol));
        list.add(new Product(6, "ibuprofen", 9.0, 17, R.drawable.ibuprofen));
        list.add(new Product(7, "Insulin", 4.0, 48, R.drawable.insulin));
        list.add(new Product(8, "Claritin", 8.0, 60, R.drawable.claritin));
        list.add(new Product(9, "Azithromycin", 6.0, 18, R.drawable.azithromycin));
        list.add(new Product(10, "Lexapro", 8.0, 17, R.drawable.lexapro));
        list.add(new Product(11, "Metformin", 10.0, 28, R.drawable.metformin));
        list.add(new Product(12, "Nitroglycerin", 8.0, 19, R.drawable.nitroglycerin));
        list.add(new Product(13, "Prozac", 14.0, 14, R.drawable.prozac));
        list.add(new Product(14, "Xanax", 25.0, 16, R.drawable.xanax));
        list.add(new Product(15, "Zyrtec", 14.0, 20, R.drawable.zyrtec));
        list.add(new Product(16, "Lisinopril", 16.0, 10, R.drawable.lisinopril));

        saveProducts(ctx, list);
        return list;
    }
}