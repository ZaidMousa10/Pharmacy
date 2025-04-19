package com.example.assignment1_pharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText searchInput;
    Button searchBtn, cartBtn;
    ListView productListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView logo = findViewById(R.id.logo_image);
        searchInput = findViewById(R.id.editText_search);
        searchBtn = findViewById(R.id.button_search);
        cartBtn = findViewById(R.id.button_cart);
        productListView = findViewById(R.id.productListView);


        ArrayList<Product> products = SharedPrefManager.getProducts(this);
        ProductAdapter productAdapter = new ProductAdapter(this, products, true);
        productListView.setAdapter(productAdapter);

        productListView.setOnItemClickListener((parent, view, position, id) -> {
            Product p = products.get(position);
            CartManager.addToCart(this, p);
            View rootView = findViewById(android.R.id.content);
            Toast.makeText(rootView.getContext(), p.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });



        searchBtn.setOnClickListener(v -> {
            String queryText = searchInput.getText().toString().trim();

            if (queryText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if any product matches
            ArrayList<Product> allProducts = SharedPrefManager.getProducts(MainActivity.this);
            boolean hasMatch = false;
            for (Product p : allProducts) {
                if (p.getName().toLowerCase().contains(queryText.toLowerCase())) {
                    hasMatch = true;
                    break;
                }
            }

            if (hasMatch) {
                Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra("query", queryText);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No products found for: " + queryText, Toast.LENGTH_SHORT).show();
            }
        });


        cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Product> updatedProducts = SharedPrefManager.getProducts(this);
        ProductAdapter newAdapter = new ProductAdapter(this, updatedProducts, true);
        productListView.setAdapter(newAdapter);
    }

}
