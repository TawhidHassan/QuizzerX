package com.example.quizzerx;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategoriesActivity extends AppCompatActivity {
    private androidx.appcompat.widget.Toolbar toolbarx;
    RecyclerView recyclerView;


    List<CategoryModel>categoryModelList;
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        recyclerView = findViewById(R.id.categoryRecyclerViewId);
        toolbarx = findViewById(R.id.toolbarId);
        setSupportActionBar(toolbarx);
        getSupportActionBar().setTitle("Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        categoryModelList=new ArrayList<>();

        final CategoryAdapter adapter=new CategoryAdapter(categoryModelList);
        recyclerView.setAdapter(adapter);

        myRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    categoryModelList.add(dataSnapshot1.getValue(CategoryModel.class));
                    //why we use CatehoryModel.class
                    //because .>>>amara category model a direct firebase ar cildren name gyula use korsi tai aikhana just category model call korlai hoba.
                    //ar jodi firebase ar direct name use na kortam tahola amadar alada alada vaba model diya data nita hoto

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CategoriesActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
}
