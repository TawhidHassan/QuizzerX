package com.example.quizzerx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;


    //for sahre prefarence and gson for ofline all question
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Gson gson;
    List<QuestionModel>bookmarlist;

    public static final String FILE_NAME="QUIZZER";
    public static final String KEY_NAME="QUESTION";

    int matchedQuestionPotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        toolbar=findViewById(R.id.bToolbarId);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //add questiuon in offline into device
        preferences=getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor=preferences.edit();
        gson=new Gson();

        //        get the offline bookmakarks
        getBookmarks();


        recyclerView=findViewById(R.id.rv_bookmarksId);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        BookmarkAdapter adapter=new BookmarkAdapter(bookmarlist);
        recyclerView.setAdapter(adapter);



    }

    //store bookmark on offline device
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmarks();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
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



    //for store question into offline device
    private void storeBookmarks()
    {
        String json=gson.toJson(bookmarlist);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}


