package com.example.assignment1_pharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Product> cartItems;
    private List<Product> gItems = new ArrayList<>();

    public CartAdapter(Context context, List<Product> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }
    public interface OnCartChangedListener {
        void onCartChanged(double newTotal);
    }
    private OnCartChangedListener cartChangedListener;

    public void setOnCartChangedListener(OnCartChangedListener listener) {
        this.cartChangedListener = listener;
    }


    @Override
    public int getCount() {
        return getGroupedItems().size();  // Return size of grouped items list
    }

    @Override
    public Object getItem(int position) {
        return getGroupedItems().get(position);  // Get grouped item at the given position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Group items by name and calculate quantities
    private List<Product> getGroupedItems() {
        List<Product> groupedItems = new ArrayList<>();
        List<String> addedProductNames = new ArrayList<>();

        for (Product p : cartItems) {
            if (!addedProductNames.contains(p.getName())) {
                int quantity = 0;

                // Count how many times the product appears in the cart
                for (Product product : cartItems) {
                    if (product.getName().equals(p.getName())) {
                        quantity++;  // Increase quantity for each occurrence of the product
                    }
                }

                // Create a new product with the correct quantity and add it to the grouped list
                Product groupedProduct = new Product(p.getId(), p.getName(), p.getPrice(), quantity, p.getImageResId());
                groupedItems.add(groupedProduct);
                gItems.add(groupedProduct);
                addedProductNames.add(p.getName());  // Mark this product as added to avoid duplicates
            }
        }

        return groupedItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<Product> groupedItems = getGroupedItems();
        Product product = groupedItems.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.productImage);
        TextView name = convertView.findViewById(R.id.productName);
        TextView quantityText = convertView.findViewById(R.id.productQuantity);
        TextView priceText = convertView.findViewById(R.id.productPrice);
        Button removeButton = convertView.findViewById(R.id.removeFromCartButton);
        Button subButton = convertView.findViewById(R.id.subFromCartButton);


        image.setImageResource(product.getImageResId());
        name.setText(product.getName());
        quantityText.setText("x" + product.getStock());  // Show quantity selected
        double totalForProduct = product.getPrice() * product.getStock();  // Correctly calculate the total for this product
        priceText.setText("$" + String.format("%.2f", totalForProduct));  // Display the correct total for this row

        removeButton.setOnClickListener(v -> {
            // Remove all instances of the product with the same name or ID
            String productName = product.getName();  // or use product.getId()
            boolean productFound = false;

            // Use iterator to safely remove while looping
            for (int i = cartItems.size() - 1; i >= 0; i--) {
                if (cartItems.get(i).getName().equals(productName)) {
                    cartItems.remove(i);
                    productFound = true;
                }
            }

            if (productFound) {
                // Update SharedPreferences
                SharedPrefManager.saveCart(context, (ArrayList<Product>) cartItems);

                // Notify the adapter that the data has changed
                notifyDataSetChanged();

                if (cartChangedListener != null) {
                    double newTotal = calculateGroupedTotal();
                    cartChangedListener.onCartChanged(newTotal);
                }

                Toast.makeText(context, product.getName() + " removed from cart", Toast.LENGTH_SHORT).show();

                if (cartItems.isEmpty()) {
                    Toast.makeText(context, "Your cart is now empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Product not found in cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle remove button click
        subButton.setOnClickListener(v -> {
            // Ensure that 'product' refers to the product to be removed (it's passed correctly in this context)
            if (cartItems.contains(product)) {
                // Remove the specific product from the cart
                cartItems.remove(product);

                // Update SharedPreferences with the new cart state
                SharedPrefManager.saveCart(context, (ArrayList<Product>) cartItems);  // Save the updated cart to SharedPreferences

                // Notify the adapter that the data has changed
                notifyDataSetChanged();

                if (cartChangedListener != null) {
                    double newTotal = calculateGroupedTotal(); // helper method like calculateTotal
                    cartChangedListener.onCartChanged(newTotal);
                }


                // Show a message to the user
                Toast.makeText(context, product.getName() + " removed from cart", Toast.LENGTH_SHORT).show();

                // If the cart is empty, show an empty cart message or take additional actions
                if (cartItems.isEmpty()) {
                    Toast.makeText(context, "Your cart is now empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle case if the product is not found in the cart
                Toast.makeText(context, "Product not found in cart", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    private double calculateGroupedTotal() {
        double total = 0;
        ArrayList<Integer> addedIds = new ArrayList<>();

        for (Product p : cartItems) {
            if (!addedIds.contains(p.getId())) {
                int quantity = 0;
                for (Product q : cartItems) {
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

