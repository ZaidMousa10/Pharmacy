package com.example.assignment1_pharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;

public class CartManager {
    private static final String CART_KEY = "cart";

    public static void addToCart(Context ctx, Product p) {
        ArrayList<Product> cart = getCart(ctx);

        // Count current quantity in cart
        int currentCount = 0;
        for (Product item : cart) {
            if (item.getId() == p.getId()) {
                currentCount++;
            }
        }

        // Check if adding would exceed the stock
        if (currentCount >= p.getStock()) {
            Toast.makeText(ctx, "Cannot add more. Maximum stock reached!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Otherwise, add and save
        cart.add(p);
        saveCart(ctx, cart);
        Toast.makeText(ctx, p.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<Product> getCart(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = prefs.getString(CART_KEY, null);

        if (json == null || json.isEmpty()) return new ArrayList<>();

        Gson gson = new Gson();
        Product[] productArray = gson.fromJson(json, Product[].class);

        ArrayList<Product> cartList = new ArrayList<>();
        Collections.addAll(cartList, productArray);
        return cartList;
    }

    public static void saveCart(Context ctx, ArrayList<Product> cart) {
        Gson gson = new Gson();
        String json = gson.toJson(cart);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    public static void clearCart(Context ctx) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.remove(CART_KEY);
        editor.apply();
    }
}
