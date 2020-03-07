package com.example.quizzerx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startButton,bookmarkbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton=findViewById(R.id.startBtnId);
        bookmarkbtn=findViewById(R.id.bookMarkBtnId);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    
                Intent categoryIntent=new Intent(getApplicationContext(),CategoriesActivity.class);
                startActivity(categoryIntent);
            }
        });
        bookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookmarkIntent=new Intent(getApplicationContext(),BookmarkActivity.class);
                startActivity(bookmarkIntent);
            }
        });
    }
}
