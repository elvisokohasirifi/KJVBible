package com.example.sirelvis.kjvbible;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Search extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String[] quotes = null;
    EditText search;
    String selected = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.actioncontent, null);
        ((LinearLayout)v).setGravity(Gravity.RIGHT);
        actionBar.setCustomView(v);
        actionBar.setDisplayShowCustomEnabled(true);
        search = (EditText) findViewById(R.id.searchText);
    }

    public void search(View view){
        Toast.makeText(this, "searching...", Toast.LENGTH_LONG).show();
        //ImageButton img = (ImageButton) findViewById(R.id.imageButton3);
       // img.setImageResource(android.R.drawable.presence_away);
        String term = search.getText().toString();
        String cont = "";
        try {
            BufferedReader br;
            if(Main.version == 0)
                br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                    .getIdentifier("bible", "raw", getPackageName()))));
            else if(Main.version == 1)
                br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                        .getIdentifier("abible", "raw", getPackageName()))));
            else if(Main.version == 2)
                br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                        .getIdentifier("amp", "raw", getPackageName()))));
            else br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                        .getIdentifier("bibnlt", "raw", getPackageName()))));
            cont = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(term.toLowerCase()))
                    cont += (line + "\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(cont.equals("")){
            quotes = new String[]{"no results found"};
            return;
        }
        quotes = cont.split("\n");
        ListView verses = (ListView) findViewById(R.id.searchResults);
        ArrayAdapter<String> adapter;
        if(Main.isNight){
            adapter = new ArrayAdapter<String>(this, R.layout.contentdark, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_search);
            rl.setBackgroundColor(Color.BLACK);
            verses.setBackgroundColor(Color.BLACK);
        }
        else{
            adapter = new ArrayAdapter<String>(this, R.layout.content, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_search);
            rl.setBackgroundColor(Color.WHITE);
            verses.setBackgroundColor(Color.WHITE);
        }
        verses.setAdapter(adapter);
        registerForContextMenu(verses);
        //img.setImageResource(android.R.drawable.ic_menu_search);
        verses.setOnItemClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu cm, View v, ContextMenu.ContextMenuInfo cmi){
        super.onCreateContextMenu(cm, v, cmi);
        cm.setHeaderIcon(R.drawable.bib);
        cm.setHeaderTitle("Select action");
        cm.add(0, v.getId(), 0, "Copy to clipboard");
        cm.add(0, v.getId(), 0, "Share");
        cm.add(0, v.getId(), 0, "Save to bookmarks");
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
        else if (mi.getTitle().equals("Save to bookmarks")) {
            write(Chapters.content + selected);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        else if(mi.getTitle().equals("Share")){
            Intent whatsapp = new Intent(Intent.ACTION_SEND);
            whatsapp.setType("text/plain");
            whatsapp.putExtra(Intent.EXTRA_TEXT, Chapters.content + selected + "- (ElRah Bible)");
            startActivity(Intent.createChooser(whatsapp, "Share"));
        }

        else return false;
        return true;
    }

    public void write(String content){
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS +
                "/Elrah KJV Bible/");
        if(!path.exists())
            path.mkdirs();

        final File file = new File(path, "bookmarks.elv");
        try{
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.append("\n");
            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Chapters.content = ((TextView)view).getText().toString().split(":")[0] + ":";
        Content.scroll = Integer.parseInt(((TextView)view).getText().toString().split(":")[1].split(" ")[0]);
        //Toast.makeText(this, String.valueOf(Content.scroll), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Content.class));
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }
}
