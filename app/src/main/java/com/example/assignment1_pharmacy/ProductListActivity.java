package com.example.assignment1_pharmacy;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    ListView resultListView;
    RadioGroup sortGroup;
    ArrayList<Product> allProducts;
    ProductAdapter searchAdapter;
    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        resultListView = findViewById(R.id.searchResultListView);
        sortGroup = findViewById(R.id.sortGroup);

        query = getIntent().getStringExtra("query").toLowerCase();
        allProducts = SharedPrefManager.getProducts(this);

        // Initial filtered list
        ArrayList<Product> filtered = filterProducts(allProducts, query);
        searchAdapter = new ProductAdapter(this, filtered, true);
        resultListView.setAdapter(searchAdapter);

        sortGroup.setOnCheckedChangeListener((group, checkedId) -> {
            ArrayList<Product> filteredList = filterProducts(allProducts, query);

            if (checkedId == R.id.radio_price_asc) {
                filteredList.sort(Comparator.comparingDouble(Product::getPrice));
            } else if (checkedId == R.id.radio_price_desc) {
                filteredList.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
            }
            // For R.id.radio_match, we already have the filtered list (default)

            searchAdapter.updateProducts(filteredList);
        });
    }

    private ArrayList<Product> filterProducts(List<Product> products, String query) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(query)) {
                result.add(p);
            }
        }
        return result;
    }
}
