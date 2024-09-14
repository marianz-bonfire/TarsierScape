package com.tarsier.marianz.tarsierscape.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.adapters.ExpandableWordsListAdapter;
import com.tarsier.marianz.tarsierscape.adapters.GridExpandableAdapter;
import com.tarsier.marianz.tarsierscape.database.Histories;
import com.tarsier.marianz.tarsierscape.models.WordHeader;
import com.tarsier.marianz.tarsierscape.models.WordInfo;
import com.tarsier.marianz.tarsierscape.utilities.ResultUtil;
import com.tarsier.marianz.tarsierscape.utilities.StringLengthComparator;
import com.tarsier.marianz.tarsierscape.utilities.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SolutionResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textLetter;
    private ProgressDialog progressDialog;
    public static  ArrayList<WordInfo>  WORDS = new ArrayList<>();
    private ExpandableListView expandListviewWords;
    List<WordHeader> group = new ArrayList<>();
    List<List<String>> childs = new ArrayList<>();
    List<List<Integer>> childsImage = new ArrayList<>();

    private String wordToSolve;
    private int wordLength = 0;
    private int wordCount = 0;

    private Histories histories;
    private List<String> restrictedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        setupToolbar();

        histories = new Histories(this);
        histories.open();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            wordToSolve = bundle.getString("LETTER").trim();
        }
        loadRestrictedDictionary();
        initializeViews();
        //populateLetters();
        new ProcessWaitCompletion().execute(); //create task to wait speech
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setSubtitle(R.string.subtitle_activity_solution);
                getSupportActionBar().setElevation(0);
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }

    private void initializeViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        textLetter = (TextView) findViewById(R.id.text_letter);
        textLetter.setText(wordToSolve);
        expandListviewWords = (ExpandableListView)findViewById(R.id.listview_results);
    }


    private void populateLetters() {
        try {


            if (!wordToSolve.isEmpty()) {
                histories.addWord(wordToSolve);
                if(WORDS.size()<= 0){
                    textLetter.setText("No dictionary found!");
                    return;
                }
                progressDialog = ProgressDialog.show(SolutionResultActivity.this, "Please wait", "Generating possible words...", true);

                wordLength = 0;
                wordCount = 0;

                new Thread(new Runnable() {
                    public void run() {
                        try {

                            List<String> child = new ArrayList<>();
                            List<Integer> childImage = new ArrayList<>();

                            List<String> trimList = ResultUtil.GetResults(wordToSolve, WORDS);

                            if(trimList.size() <=0) {
                                updateResults("trimList is empty");
                            }else {
                                for (String l : trimList) {
                                    int length = l.length();

                                    if (length >= 3) { //limit 3 chars in wordscapes
                                        if (StringUtil.allConsonants(l.toLowerCase()) == false) {
                                            if (wordLength != length) {
                                                WordHeader wordHeader = new WordHeader();
                                                wordHeader.setHeader(length + "");
                                                wordHeader.setDescription(length + " letter words made the letters in " + wordToSolve);

                                                group.add(wordHeader);

                                                if (child.size() > 0) {
                                                    child = new ArrayList<>();
                                                    childImage = new ArrayList<>();
                                                }
                                            }

                                            if (restrictedList.contains(l.toLowerCase())) {
                                                //builder.append(l.toUpperCase() + "  ---- restricted word\n");
                                            } else {
                                                WordInfo wordInfo = new WordInfo();
                                                wordInfo.setWord(l.toUpperCase());

                                                child.add(wordInfo.getWord());
                                                childImage.add(R.drawable.ic_looks_3_24);
                                            }
                                            wordCount++;
                                        }
                                    }
                                    wordLength = length;
                                }


                                expandListviewWords.setAdapter(new GridExpandableAdapter(SolutionResultActivity.this, group, childs, childsImage));

                                updateResults("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // progressDialog.dismiss();
                    }

                }).start();
            }else{
                textLetter.setText("Please enter letter to solve");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ProcessWaitCompletion extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            populateLetters();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //TTS has finished speaking.
           if(progressDialog!=null){
               progressDialog.dismiss();
           }
        }
    }

    private void updateResults(final String result){
        runOnUiThread(new Runnable() {
            public void run() {
                // Update UI elements
                if (wordCount > 0) {
                   // textResults.setTextColor(getResources().getColor(R.color.gray));
                }
               // textResults.setMovementMethod(new ScrollingMovementMethod());

               // textResults.setText(result);
                textLetter.setText(wordCount + " possible words found in letters '" + wordToSolve + "'");
                //textResults.invalidate();
            }
        });
    }

    /**
     * Create dictionary of restricted words
     */
    private void loadRestrictedDictionary() {
        restrictedList = new ArrayList<String>();
        restrictedList.add("shit");
        restrictedList.add("fuck");
        restrictedList.add("bitch");
    }



}
