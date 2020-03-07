package com.example.quizzerx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    Button startButton,bookmarkbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initial AdMob
        MobileAds.initialize(this);
        loadAds();

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

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
