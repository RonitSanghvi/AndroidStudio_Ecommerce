package com.example.final_project.activities;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.final_project.R;
import com.example.final_project.adapters.ProductAdapter;
import com.example.final_project.databinding.ActivitySearchBinding;
import com.example.final_project.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String image = jsonObject.getString("image");
                double price = jsonObject.getDouble("price");
                String description = jsonObject.getString("description");
                int id = jsonObject.getInt("id");
                int categoryId = jsonObject.getInt("categoryId");
                String categoryName = jsonObject.getString("categoryName");
                String status = jsonObject.getString("status");
                double discount = jsonObject.getDouble("discount");
                int stock = jsonObject.getInt("stock");
                Product product = new Product(name, image, price, description, id, categoryId, categoryName, status,  discount, stock);
                products.add(product);
            }
        } catch (Exception e) {e.printStackTrace();}


        String query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())
                    || product.getCategoryName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        productAdapter = new ProductAdapter(this, filteredProducts);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}