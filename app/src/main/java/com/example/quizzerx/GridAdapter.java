package com.example.quizzerx;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class GridAdapter extends BaseAdapter {

    private int setes=0;
    private String category;
    private InterstitialAd interstitialAd;

    public GridAdapter(int setes, String category, InterstitialAd interstitialAd) {
        this.setes = setes;
        this.category=category;
        this.interstitialAd=interstitialAd;


    }

    @Override
    public int getCount() {
        return setes;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view;
        if (convertView==null)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item,parent,false);
        }else
        {
            view=convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent qusIntent=new Intent(parent.getContext(),QuestionsActivity.class);
                        qusIntent.putExtra("category",category);
                        qusIntent.putExtra("setNo",position+1);

                        parent.getContext().startActivity(qusIntent);
                    }
                });
                if (interstitialAd.isLoaded()){
                    interstitialAd.show();
                    return;
                }


                Intent qusIntent=new Intent(parent.getContext(),QuestionsActivity.class);
                qusIntent.putExtra("category",category);
                qusIntent.putExtra("setNo",position+1);
                parent.getContext().startActivity(qusIntent);
            }
        });
        ((TextView)view.findViewById(R.id.setTextId)).setText(String.valueOf(position+1));
        return view;
    }
}
