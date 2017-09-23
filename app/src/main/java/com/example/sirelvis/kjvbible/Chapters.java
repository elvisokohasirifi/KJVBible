package com.example.sirelvis.kjvbible;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Chapters extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static int[] chapters = {50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13,
            10, 42, 150, 31, 12, 8, 66, 52, 5, 48, 12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28,
            16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22};
    public static String content = "";
    public static int chapt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        GridView books = (GridView) findViewById(R.id.chapters);
        String[] chaps = new String[chapters[Main.pos]];
        for(int i = 1; i <= chapters[Main.pos]; i++)
            chaps[i-1] = String.valueOf(i);

        ArrayAdapter<String> adapter;
        if(Main.isNight){
            adapter = new ArrayAdapter<String>(this, R.layout.mylistcdark, chaps);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_chapters);
            rl.setBackgroundColor(Color.BLACK);
            books.setBackgroundColor(Color.BLACK);
        }
        else {
            adapter = new ArrayAdapter<String>(this, R.layout.mylistc, chaps);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_chapters);
            rl.setBackgroundColor(Color.WHITE);
            books.setBackgroundColor(Color.WHITE);
        }

        books.setAdapter(adapter);
        books.setOnItemClickListener(this);
        getSupportActionBar().setTitle(Main.selected);
        Content.scroll = 1;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        content = Main.selected + " " + (position + 1) + ":";
        chapt = position + 1;
        startActivity(new Intent(this, Content.class));
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}
