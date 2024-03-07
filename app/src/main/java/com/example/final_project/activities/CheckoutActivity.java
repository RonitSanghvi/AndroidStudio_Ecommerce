package com.example.final_project.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapters.CartAdapter;
import com.example.final_project.databinding.ActivityCheckoutBinding;
import com.example.final_project.model.Product;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import java.util.ArrayList;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for (Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("CAD %.2f", cart.getTotalPrice()));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("CAD %.2f", cart.getTotalPrice()));

        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("CAD " + totalPrice);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this, PaymentGateway.class);
                intent.putExtra("TotalPrice", totalPrice);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}