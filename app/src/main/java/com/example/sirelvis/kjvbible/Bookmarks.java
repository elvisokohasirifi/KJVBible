package com.example.sirelvis.kjvbible;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Bookmarks extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String[] quotes = null;
    String selected = "";
    ListView verses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        verses = (ListView) findViewById(R.id.bookmarks);

        try {
            final File path = Environment.getExternalStoragePublicDirectory("bookmarks.elv");
            if(!path.exists())
                return;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String content = "";
            String line = "";
            while((line = br.readLine()) != null){
                content += (line + "\n");
            }
            quotes = content.split("\n");
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verses.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter;
        if(Main.isNight){
            adapter = new ArrayAdapter<String>(this, R.layout.contentdark, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_bookmarks);
            rl.setBackgroundColor(Color.BLACK);
        }
        else {
            adapter = new ArrayAdapter<String>(this, R.layout.content, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_bookmarks);
            rl.setBackgroundColor(Color.WHITE);
        }
        verses.setAdapter(adapter);
        registerForContextMenu(verses);
        verses.setOnItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Chapters.content = ((TextView)view).getText().toString().split(":")[0] + ":";
        Content.scroll = Integer.parseInt(((TextView)view).getText().toString().split(":")[1].split(" ")[0]);
        //Toast.makeText(this, String.valueOf(Content.scroll), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Content.class));
    }

    @Override
    public void onCreateContextMenu(ContextMenu cm, View v, ContextMenu.ContextMenuInfo cmi){
        super.onCreateContextMenu(cm, v, cmi);
        cm.setHeaderIcon(R.drawable.bib);
        cm.setHeaderTitle("Select action");
        cm.add(0, v.getId(), 0, "Copy to clipboard");
        cm.add(0, v.getId(), 0, "Share");
    }

    @Override
    public boolean onContextItemSelected(MenuItem mi){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) mi.getMenuInfo();

        //int index = mi.getItemId();
        selected = quotes[info.position];
        if(mi.getTitle().equals("Copy to clipboard")) {
            int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.HONEYCOMB){
                android.text.ClipboardManager cb = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cb.setText(Chapters.content + selected);
            }
            else{
                android.content.ClipboardManager cb = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData cd = android.content.ClipData.newPlainText(Chapters.content + selected, Chapters.content + selected);
                cb.setPrimaryClip(cd);
            }
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
        }
        else if(mi.getTitle().equals("Share")){
            Intent whatsapp = new Intent(Intent.ACTION_SEND);
            whatsapp.setType("text/plain");
            whatsapp.putExtra(Intent.EXTRA_TEXT, Chapters.content + selected + " (ElRah KJV Bible)");
            startActivity(Intent.createChooser(whatsapp, "Share"));
        }

        else return false;
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

}
