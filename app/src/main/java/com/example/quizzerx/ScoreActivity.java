package com.example.quizzerx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    TextView score,totalscore;
    Button doneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score=findViewById(R.id.scoreId);
        totalscore=findViewById(R.id.totalscoreId);
        doneBtn=findViewById(R.id.doneBtnId);

        score.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        totalscore.setText("OUT OF "+String.valueOf(getIntent().getIntExtra("total",0)));

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

