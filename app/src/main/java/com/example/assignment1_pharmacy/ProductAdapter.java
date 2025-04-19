package com.example.assignment1_pharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class ProductAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Product> products;
    private boolean showAddToCart;

    public ProductAdapter(Context context, ArrayList<Product> products, boolean showAddToCart) {
        this.context = context;
        this.products = products;
        this.showAddToCart = showAddToCart;
    }

    public void updateProducts(ArrayList<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @Override public int getCount() { return products.size(); }
    @Override public Object getItem(int position) { return products.get(position); }
    @Override public long getItemId(int position) { return products.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        }

        Product product = products.get(position);

        ImageView image = convertView.findViewById(R.id.image_product);
        TextView name = convertView.findViewById(R.id.text_name);
        TextView price = convertView.findViewById(R.id.text_price);
        TextView stock = convertView.findViewById(R.id.text_stock);
        Button addButton = convertView.findViewById(R.id.button_add_to_cart);

        name.setText(product.getName());
        price.setText("Price: $" + product.getPrice());
        stock.setText("Stock: " + product.getStock());
        image.setImageResource(product.getImageResId()); // or use Glide/Picasso for dynamic images

        if (showAddToCart) {
            addButton.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(v -> {
                CartManager.addToCart(context, product);
            });
        } else {
            addButton.setVisibility(View.GONE);
        }

        return convertView;
    }
}
