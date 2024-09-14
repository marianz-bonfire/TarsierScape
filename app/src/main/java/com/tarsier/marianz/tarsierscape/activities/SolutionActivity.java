package com.tarsier.marianz.tarsierscape.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.adapters.ExpandableWordsListAdapter;
import com.tarsier.marianz.tarsierscape.adapters.GridExpandableAdapter;
import com.tarsier.marianz.tarsierscape.database.Histories;
import com.tarsier.marianz.tarsierscape.models.WordHeader;
import com.tarsier.marianz.tarsierscape.models.WordInfo;
import com.tarsier.marianz.tarsierscape.utilities.ResultUtil;
import com.tarsier.marianz.tarsierscape.utilities.StringUtil;
import com.tarsier.marianz.tarsierscape.utilities.WordUtil;

import java.util.ArrayList;
import java.util.List;

public class SolutionActivity extends AppCompatActivity {

    public static ArrayList<WordInfo> WORDS = new ArrayList<>();
    List<WordHeader> group = new ArrayList<>();
    List<List<String>> childs = new ArrayList<>();
    List<List<Integer>> childsImage = new ArrayList<>();
    private Toolbar toolbar;
    private TextView textLetter;
    private ProgressDialog progressDialog;
    private ExpandableListView expandListviewWords;
    private ExpandableWordsListAdapter wordsListAdapter;
    private GridExpandableAdapter expandableAdapter;
    private LinearLayout linearLayoutNoData;
    private String wordToSolve;
    private int wordLength = 0;
    private int wordCount = 0;

    private Histories histories;
    private List<String> restrictedList;
    private StringBuilder builder;

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
        initializeViews();
        loadRestrictedDictionary();
        if (WORDS != null) {
            if (WORDS.size() <= 0) {
                WORDS = WordUtil.getWords();
            }
        }
        populateLetters();
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
        expandListviewWords = (ExpandableListView) findViewById(R.id.listview_results);
        linearLayoutNoData = (LinearLayout) findViewById(R.id.layout_nodata);
        linearLayoutNoData.setVisibility(View.INVISIBLE);
    }

    private void populateLetters() {

        if (!wordToSolve.isEmpty()) {
            histories.addWord(wordToSolve);
            if (WORDS.size() <= 0) {
                textLetter.setText("No dictionary found!");
                linearLayoutNoData.setVisibility(View.VISIBLE);
                expandListviewWords.setVisibility(View.INVISIBLE);

                return;
            }
            progressDialog = ProgressDialog.show(SolutionActivity.this, "Please wait", "Generating possible words...", true);

            wordLength = 0;
            wordCount = 0;

            Thread process = new Thread(new Runnable() {
                public void run() {

                    List<String> child1 = new ArrayList<>();
                    List<Integer> childImage1 = new ArrayList<>();

                    List<String> trimList = ResultUtil.GetResults(wordToSolve, WORDS);

                    if (trimList.size() <= 0) {
                        updateResults(false);
                    }
                    for (String l : trimList) {
                        int length = l.length();
                        if (length >= 3 && length <= 9) { //limit 3 chars in wordscapes
                            if (StringUtil.allConsonants(l.toLowerCase()) == false) {
                                if (wordLength != length) {

                                    if (wordCount > 0) {
                                        WordHeader wordHeader = new WordHeader();
                                        wordHeader.setHeader(" letter words");
                                        wordHeader.setCount(child1.size() + "");
                                        wordHeader.setDescription(child1.size() + " letter words made the letters in " + wordToSolve);

                                        group.add(wordHeader);
                                        childs.add(child1);
                                        childsImage.add(childImage1);
                                    }
                                    if (child1.size() > 0) {
                                        child1 = new ArrayList<>();
                                        childImage1 = new ArrayList<>();
                                    }
                                }

                                if (restrictedList.contains(l.toLowerCase())) {
                                    //builder.append(l.toUpperCase() + "  ---- restricted word\n");
                                } else {
                                    WordInfo wordInfo = new WordInfo();
                                    wordInfo.setWord(l.toUpperCase());

                                    child1.add(wordInfo.getWord());
                                    childImage1.add(R.drawable.ic_looks_3_24);
                                }
                                wordCount++;
                            }
                        }
                        wordLength = length;
                    }
                    if (group.size() > 0) {
                        expandListviewWords.setAdapter(new GridExpandableAdapter(SolutionActivity.this, group, childs, childsImage));
                    }
                    updateResults(group.size() > 0);

                }
            });

            process.start();
            try {
                process.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
            }
        } else {
            textLetter.setText("Please enter letter to solve");
        }
    }

    private void updateResults(final boolean show) {
        runOnUiThread(new Runnable() {
            public void run() {
                // Update UI elements
                if (show) {
                    textLetter.setText(wordCount + " possible word(s) found in letters '" + wordToSolve + "'");
                    linearLayoutNoData.setVisibility(View.INVISIBLE);
                    expandListviewWords.setVisibility(View.VISIBLE);
                    textLetter.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNoData.setVisibility(View.VISIBLE);
                    expandListviewWords.setVisibility(View.INVISIBLE);
                    textLetter.setVisibility(View.INVISIBLE);
                }
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
