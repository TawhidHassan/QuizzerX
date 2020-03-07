package com.example.quizzerx;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private TextView qustion, noIdecator;
    private FloatingActionButton bookmarkButn;
    LinearLayout optionContainer;
    Button shareButn, nextButton;
    List<QuestionModel> list;
    private int count = 0;
    private int potion = 0;
    int score = 0;

    private String category;
    private int setNo;

    Dialog loadingDialog;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    //for sahre prefarence and gson for ofline all question
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
    List<QuestionModel>bookmarlist;

    public static final String FILE_NAME="QUIZZER";
    public static final String KEY_NAME="QUESTION";

    int matchedQuestionPotion;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        loadAds();

        Toolbar toolbar = findViewById(R.id.qusToolbar);

        qustion = findViewById(R.id.questionId);
        noIdecator = findViewById(R.id.numberIndecatorId);
        bookmarkButn = findViewById(R.id.bookmarkButtonId);
        optionContainer = findViewById(R.id.optionContenierId);
        shareButn = findViewById(R.id.sharebuttonId);
        nextButton = findViewById(R.id.nextButtonId);


        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_button));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        //add questiuon in offline into device
        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

//        get the offline bookmakarks
        getBookmarks();

        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo", 1);

        list = new ArrayList<>();
        loadingDialog.show();
        myRef.child("SETES").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    list.add(dataSnapshot1.getValue(QuestionModel.class));
                }
                if (list.size() > 0) {
                    //click answer
                    for (int i = 0; i < 4; i++) {
                        optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {
                                checkAnswer((Button) v);
                            }
                        });
                    }
                    //set first qus
                    playAnim(qustion, 0, list.get(potion).getQuestion());
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            nextButton.setEnabled(false);
                            nextButton.setAlpha(0.7f);
                            enableOption(true);
                            potion++;
                            if (potion == list.size()) {
                                Intent scoreIntent=new Intent(getApplicationContext(),ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("total",list.size());
                                startActivity(scoreIntent);
                                finish();

                                return;
                            }
                            count = 0;
                            playAnim(qustion, 0, list.get(potion).getQuestion());
                        }
                    });


                }else
                {
                    finish();
                    Toast.makeText(getApplicationContext(),"No Question is available",Toast.LENGTH_LONG).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
                finish();
            }
        });

        bookmarkButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check this questrion are add on book mark or not
                if (modelMatch())
                {
                    bookmarlist.remove(matchedQuestionPotion);
                    bookmarkButn.setImageDrawable(getDrawable(R.drawable.bookmark));
                }else
                {
                    bookmarlist.add(list.get(potion));
                    bookmarkButn.setImageDrawable(getDrawable(R.drawable.bookmarkselected));
                }
                //check this questrion are add on book mark or not
            }
        });

        //share button
        shareButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body=list.get(potion).getQuestion()+"\n1)"+list.get(potion).getOptionA()+"\n2)"
                        +list.get(potion).getOptionB()+"\n3)"
                        +list.get(potion).getOptionC()+"\n4)"
                        +list.get(potion).getOptionD();

                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Quizzer Challange");
                shareIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(shareIntent,"share via"));
            }
        });

    }


    //store bookmark on offline device
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    private void playAnim(final View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                        if (value == 0 && count < 4) {
                            String option = "";
                            if (count == 0) {
                                option = list.get(potion).getOptionA();
                            } else if (count == 1) {
                                option = list.get(potion).getOptionB();
                            } else if (count == 2) {
                                option = list.get(potion).getOptionC();
                            } else if (count == 3) {
                                option = list.get(potion).getOptionD();
                            }
                            playAnim(optionContainer.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //data change
                        if (value == 0) {
                            try {
                                ((TextView) view).setText(data);
                                noIdecator.setText(potion + 1 + "/" + list.size());


                                //check this questrion are add on book mark or not
                                if (modelMatch())
                                {
                                    bookmarkButn.setImageDrawable(getDrawable(R.drawable.bookmarkselected));
                                }else
                                {
                                    bookmarkButn.setImageDrawable(getDrawable(R.drawable.bookmark));
                                }
                                //check this questrion are add on book mark or not
                            } catch (ClassCastException ex) {
                                ((Button) view).setText(data);
                            }
                            view.setTag(data);
                            playAnim(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(Button selectedOption) {
        enableOption(false);
        nextButton.setEnabled(true);
        nextButton.setAlpha(1);
        if (selectedOption.getText().toString().equals(list.get(potion).getCorrectAns())) {
            //correct
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        } else {
            //incorrect
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption = (Button) optionContainer.findViewWithTag(list.get(potion).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            optionContainer.getChildAt(i).setEnabled(enable);
            if (enable) {
                optionContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9E9C9C")));

            }
        }
    }

    //for get question into offline device
    private void getBookmarks()
    {
        String json= preferences.getString(KEY_NAME,"");
        Type type=new TypeToken<List<QuestionModel>>(){}.getType();

        bookmarlist=gson.fromJson(json,type);

        if (bookmarlist==null)
        {
            bookmarlist=new ArrayList<>();
        }
    }

    //check which questuion we bookmarks
    private boolean modelMatch()
    {
        boolean matched=false;
        int i=0;
        for (QuestionModel model:bookmarlist)
        {

            if (model.getQuestion().equals(list.get(potion).getQuestion())
                    &&model.getCorrectAns().equals(list.get(potion).getCorrectAns())
                    &&model.getSetNo()==list.get(potion).getSetNo())
            {
                matched=true;
                matchedQuestionPotion=i;
            }
            i++;
        }
        return matched;
    }

    //for store question into offline device
    private void storeBookmarks()
    {
        String json=gson.toJson(bookmarlist);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }

    private void loadAds() {
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
