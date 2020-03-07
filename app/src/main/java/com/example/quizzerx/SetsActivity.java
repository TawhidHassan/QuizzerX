package com.example.quizzerx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
   
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SetsActivity extends AppCompatActivity {

    Toolbar toolbarx;
    GridView gridView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        loadAds();

        gridView = findViewById(R.id.setGrideViewId);

        toolbarx = findViewById(R.id.setToolbarId);
        setSupportActionBar(toolbarx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("setTitle"));

        GridAdapter adapter=new GridAdapter(getIntent().getIntExtra("sets",0),getIntent().getStringExtra("setTitle"),mInterstitialAd);
        gridView.setAdapter(adapter);





    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        Interstitial Ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitialAdsId));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }
}
