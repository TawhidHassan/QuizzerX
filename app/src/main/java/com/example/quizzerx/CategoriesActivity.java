package com.example.quizzerx;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


public class CategoriesActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbarx;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        recyclerView=findViewById(R.id.categoryRecyclerViewId);
        toolbarx = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbarx);
        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<CategoryModel>categoryModelList=new ArrayList<>();
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        categoryModelList.add(new CategoryModel("","sifat"));
        CategoryAdapter adapter=new CategoryAdapter(categoryModelList);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
