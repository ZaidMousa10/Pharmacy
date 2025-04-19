package com.example.assignment1_pharmacy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ListView cartListView;
    ArrayList<Product> cartItems;
    Button checkoutBtn;
    TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.cartListView);
        checkoutBtn = findViewById(R.id.checkoutButton);
        totalPriceText = findViewById(R.id.totalPriceText); // Make sure this is in the XML layout
        cartItems = CartManager.getCart(this);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        CartAdapter adapter = new CartAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

        double total = calculateTotal(cartItems);  // Calculate the total based on all items
        totalPriceText.setText("Total: $" + String.format("%.2f", total));  // Display the total in the TextView

        adapter.setOnCartChangedListener(newTotal -> {
            totalPriceText.setText("Total: $" + String.format("%.2f", newTotal));
        });


        checkoutBtn.setOnClickListener(v -> {
            ArrayList<Integer> processedIds = new ArrayList<>();

            for (int i = 0; i < cartItems.size(); i++) {
                Product p = cartItems.get(i);

                if (!processedIds.contains(p.getId())) {
                    int quantity = 0;

                    for (int j = 0; j < cartItems.size(); j++) {
                        if (cartItems.get(j).getId() == p.getId()) {
                            quantity++;
                        }
                    }

                    SharedPrefManager.decreaseStock(this, p, quantity);
                    processedIds.add(p.getId());
                }
            }

            CartManager.clearCart(this);
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

    private double calculateTotal(List<Product> allItems) {
        double total = 0;

        // Group products by ID (or name if IDs are not reliable)
        ArrayList<Product> groupedItems = new ArrayList<>();
        ArrayList<Integer> addedIds = new ArrayList<>();

        for (Product p : allItems) {
            if (!addedIds.contains(p.getId())) {
                int quantity = 0;
                for (Product q : allItems) {
                    if (q.getId() == p.getId()) {
                        quantity++;
                    }
                }
                total += p.getPrice() * quantity;
                addedIds.add(p.getId());
            }
        }

        return total;
    }

}
