package com.example.final_project.activities;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.final_project.R;
import com.example.final_project.adapters.ProductAdapter;
import com.example.final_project.databinding.ActivityCategoryBinding;
import com.example.final_project.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        try {
            Resources resources = getResources();
            InputStream inputStream = resources.openRawResource(R.raw.products);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            String jsonStr = new String(buffer);
            int catId = getIntent().getIntExtra("categoryId", 0);


            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int categoryId = jsonObject.getInt("categoryId");


                if(categoryId == catId){
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getString("image");
                    double price = jsonObject.getDouble("price");
                    String description = jsonObject.getString("description");
                    int id = jsonObject.getInt("id");
                    String categoryName = jsonObject.getString("categoryName");
                    String status = jsonObject.getString("status");
                    double discount = jsonObject.getDouble("discount");
                    int stock = jsonObject.getInt("stock");
                    Product product = new Product(name, image, price, description, id, categoryId, categoryName, status,  discount, stock);
                    products.add(product);
                }

            }
        } catch (Exception e) {e.printStackTrace();}

        productAdapter = new ProductAdapter(this, products);

        String categoryName = getIntent().getStringExtra("categoryName");

        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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