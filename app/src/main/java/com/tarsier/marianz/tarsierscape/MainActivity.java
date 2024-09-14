package com.tarsier.marianz.tarsierscape;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarsier.marianz.tarsierscape.activities.CreditsActivity;
import com.tarsier.marianz.tarsierscape.activities.SettingsActivity;
import com.tarsier.marianz.tarsierscape.activities.SolutionActivity;
import com.tarsier.marianz.tarsierscape.activities.SolutionResultActivity;
import com.tarsier.marianz.tarsierscape.adapters.WordGridAdapter;
import com.tarsier.marianz.tarsierscape.database.Dictionaries;
import com.tarsier.marianz.tarsierscape.database.Histories;
import com.tarsier.marianz.tarsierscape.models.WordInfo;
import com.tarsier.marianz.tarsierscape.utilities.WordUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<WordInfo> WORDS = new ArrayList<>();
    private Toolbar toolbar;
    private TextView mTextMessage;
    private TextView mTextDescription;
   // private ListView listView;
    private GridView listView;
    private ArrayList<WordInfo> listWords;
    private ArrayAdapter<WordInfo> adapter;
    private Dictionaries dictionaries;
    private Histories histories;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    //mTextMessage.setText(R.string.title_history);
                    populateList("History");
                    return true;
                case R.id.navigation_dictionary:
                    //mTextMessage.setText(R.string.title_dictionary);
                    populateList("Dictionary");
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setSubtitle(R.string.subtitle_activity_main);
                getSupportActionBar().setElevation(0);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        setupToolbar();
        initializeControls();
        populateList("History");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.search_hint));
        searchEditText.setHint(getApplicationContext().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (listWords.contains(query)) {
                    adapter.getFilter().filter(query);
                } else {
                    Toast.makeText(MainActivity.this, "letter to search " + query, Toast.LENGTH_LONG).show();
                }
                showSolutionActivity(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_credits) {
            Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeControls() {
        //mTextMessage = (TextView) findViewById(R.id.navigation_text);
        mTextDescription = (TextView) findViewById(R.id.navigation_text_description);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //listView = (ListView) findViewById(R.id.listView);
        listView = findViewById(R.id.listView);


    }

    private void populateList(String table) {
        if (table.equals("History")) {
            histories = new Histories(this);
            histories.open();
            listWords = histories.getHistoryList();
        } else {
            //dictionaries = new Dictionaries(this);
            //dictionaries.open();
            //list = dictionaries.getDictionaryList();
            listWords = this.WORDS;
        }

        mTextDescription.setText(listWords.size()+" "+table+" found in your database");

        adapter = new WordGridAdapter(MainActivity.this, listWords);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                WordInfo wordInfo = (WordInfo) parent.getItemAtPosition(position);
                String word = wordInfo.getWord();
                if (!word.isEmpty()) {
                    showSolutionActivity(word);
                }
                return false;
            }

        });
    }

    private void showSolutionActivity(String word){
        if(WORDS.size()<=0){
            WORDS = WordUtil.getWords();
        }
        SolutionActivity.WORDS = WORDS;
        Intent intent = new Intent(MainActivity.this, SolutionActivity.class);
        intent.putExtra("LETTER", word.trim().toUpperCase());
        startActivity(intent);
    }
}
