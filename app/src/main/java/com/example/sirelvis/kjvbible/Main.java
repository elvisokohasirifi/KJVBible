package com.example.sirelvis.kjvbible;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static String[] books = {"Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua", "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalm", "Proverbs", "Ecclesiastes",
            "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
            "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
            "Zechariah", "Malachi","Matthew", "Mark", "Luke", "John", "Acts",
            "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians",
            "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus",
            "Philemon", "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John",
            "Jude", "Revelation"};
    public static String selected = null;
    public static int pos = 0;
    public static boolean isNight = false;
    public static int version = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showQuote();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, Search.class));
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mylist, books);
        final ListView books = (ListView) findViewById(R.id.books);
        books.setAdapter(adapter);
        books.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Bookmarks.class));
        }
        else if(id == R.id.night){
            ArrayAdapter<String> adapter;
            if(item.getTitle().toString().equals("Normal Mode")) {
                adapter = new ArrayAdapter<String>(this, R.layout.mylist, books);
                item.setTitle("Night Mode");
                isNight = false;
            }
            else {
                adapter = new ArrayAdapter<String>(this, R.layout.mylistdark, books);
                item.setTitle("Normal Mode");
                isNight = true;
            }
            final ListView books = (ListView) findViewById(R.id.books);
            books.setAdapter(adapter);
            books.setOnItemClickListener(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        pos = position;
        selected = books[pos];
        startActivity(new Intent(this, Chapters.class));
    }

    public void showQuote(){
        String[] quotes = null;
        try {
            boolean prev = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                    .getIdentifier("local", "raw", getPackageName()))));
            String content = "";
            String line = "";
            while((line = br.readLine()) != null){
                content += (line + "\n");
            }
            quotes = content.split("\n\n");
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Random random = new Random();
        int n = random.nextInt(quotes.length);
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Quote of the session");
        al.setMessage(quotes[n]);
        al.setIcon(R.drawable.bib);
        al.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}
