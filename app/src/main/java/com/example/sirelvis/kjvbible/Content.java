package com.example.sirelvis.kjvbible;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE;

public class Content extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    String selected = "";
    String[] quotes = null;
    public static int scroll = 1;
    ArrayList <Integer> sel;
    ListView verses = null;
    String[] translations = {"KJV", "BBE", "AMP", "NLT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Chapters.content.substring(0, Chapters.content.indexOf(":")));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refresh();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        MenuItem it = menu.getItem(2);
        if(Main.isNight) it.setTitle("Normal Mode");
        else it.setTitle("Night Mode");
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<String> trans = new ArrayAdapter<String>(this, R.layout.spin, translations);
        spinner.setAdapter(trans);
        spinner.setSelection(Main.version);
        spinner.setOnItemSelectedListener(this);
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

        else if (id == R.id.search)
            startActivity(new Intent(Content.this, Search.class));
        else if(id == R.id.night) {
            ArrayAdapter<String> adapter;
            if (item.getTitle().toString().equals("Normal Mode")) {
                item.setTitle("Night Mode");
                Main.isNight = false;
                refresh();
            } else {
                item.setTitle("Normal Mode");
                Main.isNight = true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.genesis) {
            Main.pos = 0;
            Main.selected = "Genesis";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.exodus) {
            Main.pos = 1;
            Main.selected = "Exodus";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.leviticus) {
            Main.pos = 2;
            Main.selected = "Leviticus";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.numbers) {
            Main.pos = 3;
            Main.selected = "Numbers";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.deuteronomy) {
            Main.pos = 4;
            Main.selected = "Deuteronomy";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.joshua) {
            Main.pos = 5;
            Main.selected = "Joshua";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.judges) {
            Main.pos = 6;
            Main.selected = "Judges";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.ruth) {
            Main.pos = 7;
            Main.selected = "Ruth";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.samuela) {
            Main.pos = 8;
            Main.selected = "1 Samuel";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.samuelb) {
            Main.pos = 9;
            Main.selected = "2 Samuel";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.kingsa) {
            Main.pos = 10;
            Main.selected = "1 Kings";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.kingsb) {
            Main.pos = 11;
            Main.selected = "2 Kings";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.chroniclesa) {
            Main.pos = 12;
            Main.selected = "1 Chronicles";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.chroniclesb) {
            Main.pos = 13;
            Main.selected = "2 Chronicles";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.ezra) {
            Main.pos = 14;
            Main.selected = "Ezra";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.nehemiah) {
            Main.pos = 15;
            Main.selected = "Nehemiah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.esther) {
            Main.pos = 16;
            Main.selected = "Esther";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.job) {
            Main.pos = 17;
            Main.selected = "Job";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.psalm) {
            Main.pos = 18;
            Main.selected = "Psalm";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.proverbs) {
            Main.pos = 19;
            Main.selected = "Proverbs";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.ecclesiastes) {
            Main.pos = 20;
            Main.selected = "Ecclesiastes";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.songofsolomon) {
            Main.pos = 21;
            Main.selected = "Song of Solomon";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.isaiah) {
            Main.pos = 22;
            Main.selected = "Isaiah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.jeremiah) {
            Main.pos = 23;
            Main.selected = "Jeremiah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.lamentations) {
            Main.pos = 24;
            Main.selected = "Lamentations";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.ezekiel) {
            Main.pos = 25;
            Main.selected = "Ezekiel";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.daniel) {
            Main.pos = 26;
            Main.selected = "Daniel";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.hosea) {
            Main.pos = 27;
            Main.selected = "Hosea";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.joel) {
            Main.pos = 28;
            Main.selected = "Joel";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.amos) {
            Main.pos = 29;
            Main.selected = "Amos";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.obadiah) {
            Main.pos = 30;
            Main.selected = "Obadiah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.jonah) {
            Main.pos = 31;
            Main.selected = "Jonah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.micah) {
            Main.pos = 32;
            Main.selected = "Micah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.nahum) {
            Main.pos = 33;
            Main.selected = "Nahum";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.habakkuk) {
            Main.pos = 34;
            Main.selected = "Habakkuk";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.zephaniah) {
            Main.pos = 35;
            Main.selected = "Zephaniah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.haggai) {
            Main.pos = 36;
            Main.selected = "Haggai";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.zechariah) {
            Main.pos = 37;
            Main.selected = "Zechariah";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.malachi) {
            Main.pos = 38;
            Main.selected = "Malachi";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.matthew) {
            Main.pos = 39;
            Main.selected = "Matthew";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.mark) {
            Main.pos = 40;
            Main.selected = "Mark";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.luke) {
            Main.pos = 41;
            Main.selected = "Luke";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.john) {
            Main.pos = 42;
            Main.selected = "John";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.acts) {
            Main.pos = 43;
            Main.selected = "Acts";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.romans) {
            Main.pos = 44;
            Main.selected = "Romans";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.corinthiansa) {
            Main.pos = 45;
            Main.selected = "1 Corinthians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.corinthiansb) {
            Main.pos = 46;
            Main.selected = "2 Corinthians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.galatians) {
            Main.pos = 47;
            Main.selected = "Galatians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.ephesians) {
            Main.pos = 48;
            Main.selected = "Ephesians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.philippians) {
            Main.pos = 49;
            Main.selected = "Philippians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.colossians) {
            Main.pos = 50;
            Main.selected = "Colossians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.thessaloniansa) {
            Main.pos = 51;
            Main.selected = "1 Thessalonians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.thessaloniansb) {
            Main.pos = 52;
            Main.selected = "2 Thessalonians";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.timothya) {
            Main.pos = 53;
            Main.selected = "1 Timothy";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.timothyb) {
            Main.pos = 54;
            Main.selected = "2 Timothy";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.titus) {
            Main.pos = 55;
            Main.selected = "Titus";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.philemon) {
            Main.pos = 56;
            Main.selected = "Philemon";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.hebrews) {
            Main.pos = 57;
            Main.selected = "Hebrews";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.james) {
            Main.pos = 58;
            Main.selected = "James";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.petera) {
            Main.pos = 59;
            Main.selected = "1 Peter";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.peterb) {
            Main.pos = 60;
            Main.selected = "2 Peter";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.johna) {
            Main.pos = 61;
            Main.selected = "1 John";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.johnb) {
            Main.pos = 62;
            Main.selected = "2 John";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.johnc) {
            Main.pos = 63;
            Main.selected = "3 John";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.jude) {
            Main.pos = 64;
            Main.selected = "Jude";
            startActivity(new Intent(this, Chapters.class));
        }
        else if (id == R.id.revelation) {
            Main.pos = 65;
            Main.selected = "Revelation";
            startActivity(new Intent(this, Chapters.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String search(String term) {
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
            else
                br = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources()
                        .getIdentifier("bibnlt", "raw", getPackageName()))));
            cont = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.startsWith(term))
                    cont += (line + "\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cont;
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
        String toShare = "";
        if(!sel.contains(info.position + 1))
            sel.add(info.position + 1);
        Collections.sort(sel);
        for(int i = 0; i < sel.size(); i++)
            toShare += (quotes[sel.get(i)-1] + "\n");
        //int index = mi.getItemId();
        selected = quotes[info.position];
        if(mi.getTitle().equals("Copy to clipboard")) {
            int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.HONEYCOMB){
                android.text.ClipboardManager cb = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cb.setText(toShare + "- ElRah Bible (" + translations[Main.version] + ")");
            }
            else{
                android.content.ClipboardManager cb = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData cd = android.content.ClipData.newPlainText(toShare + "- ElRah Bible (" + translations[Main.version] + ")", toShare + "- ElRah Bible (" + translations[Main.version] + ")");
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
            whatsapp.putExtra(Intent.EXTRA_TEXT, toShare + "- ElRah Bible (" + translations[Main.version] + ")");
            startActivity(Intent.createChooser(whatsapp, "Share"));
        }

        else return false;
        return true;
    }

    public void write(String content){
        final File path = Environment.getExternalStoragePublicDirectory("");
        path.isHidden();
        if(!path.exists())
            try {
                path.createNewFile();
                path.mkdirs();
            } catch (IOException e) {
                e.printStackTrace();
            }

        final File file = new File(path, "bookmarks.elv");
        try{

            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP){
                ActivityCompat.requestPermissions(Content.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            myOutWriter.append(content);
            myOutWriter.append("\n");
            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!sel.contains(position+1))
            sel.add(position + 1);
        else sel.remove(new Integer(position + 1));
        String content = "";
        for(Integer i: sel)
            content += String.valueOf(i + " ");
        final Snackbar snackbar = Snackbar.make(view, "Selected verses: " + content, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.setAction("Hide", new View.OnClickListener(){
            @Override
            public void onClick(View v){
            }
        });
        snackbar.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    public void refresh(){
        quotes = search(Chapters.content).split("\n");
        verses = (ListView) findViewById(R.id.verses);
        ArrayAdapter<String> adapter;
        if(Main.isNight) {
            adapter = new ArrayAdapter<String>(this, R.layout.contentdark, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.content_content);
            rl.setBackgroundColor(Color.BLACK);
            verses.setBackgroundColor(Color.BLACK);
        }
        else {
            adapter = new ArrayAdapter<String>(this, R.layout.content, quotes);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.content_content);
            rl.setBackgroundColor(Color.WHITE);
            verses.setBackgroundColor(Color.WHITE);
        }
        verses.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        verses.setAdapter(adapter);
        verses.setSelection(Content.scroll-1);
        registerForContextMenu(verses);
        verses.setOnItemClickListener(this);
        sel = new ArrayList<>();
        verses.setOnTouchListener(new OnSwipeTouchListener(Content.this){
            @Override
            public void onSwipeLeft(){
                //Toast.makeText(Content.this, "right", Toast.LENGTH_SHORT).show();
                int max = Chapters.chapters[Main.pos];
                if(Chapters.chapt == max){
                    Toast.makeText(Content.this, "Reached the end of the book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Chapters.content = Main.selected + " " + (Chapters.chapt + 1) + ":";
                Chapters.chapt++;
                getSupportActionBar().setTitle(Chapters.content.substring(0, Chapters.content.indexOf(":")));
                refresh();
            }

            public void onSwipeRight(){
                if(Chapters.chapt == 1){
                    Toast.makeText(Content.this, "Reached the beginning of the book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Chapters.content = Main.selected + " " + (Chapters.chapt - 1) + ":";
                Chapters.chapt--;
                getSupportActionBar().setTitle(Chapters.content.substring(0, Chapters.content.indexOf(":")));
                refresh();
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Main.version = position;
        parent.setSelection(Main.version);
        refresh();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
