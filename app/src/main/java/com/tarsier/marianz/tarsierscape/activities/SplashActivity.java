package com.tarsier.marianz.tarsierscape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.MainActivity;
import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.constant.DbConst;
import com.tarsier.marianz.tarsierscape.database.Dictionaries;
import com.tarsier.marianz.tarsierscape.models.Dictionary;
import com.tarsier.marianz.tarsierscape.models.WordInfo;
import com.tarsier.marianz.tarsierscape.utilities.AssetsUtil;
import com.tarsier.marianz.tarsierscape.utilities.StringUtil;
import com.tarsier.marianz.tarsierscape.utilities.WordUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar mProgress;
    private TextView mProgressText;
    private TextView mProgressMessageText;
    private TextView mContentText;

    private ArrayList<Dictionary> dictionaryList;
    private Dictionaries dictionaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        //this.deleteDatabase(DbConst.DATABASE_NAME); //delete database

        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        mProgressText = (TextView) findViewById(R.id.text_progress);
        mProgressMessageText = (TextView) findViewById(R.id.text_progress_message);
        mContentText = (TextView) findViewById(R.id.fullscreen_content);
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                //doWorkDictionaries();
                AssetsUtil.writeToInternalStorage(SplashActivity.this);
                doWorkAssetsDictionaries();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWorkAssetsDictionaries() {
        ArrayList<WordInfo>  words = AssetsUtil.getWordsAssets(this);
        MainActivity.WORDS = words;
        SolutionActivity.WORDS = words;
        SolutionResultActivity.WORDS = words;
        WordUtil.setWords(words);
    }
    private void doWorkDictionaries() {
        dictionaries = new Dictionaries(this);
        dictionaries.open();
        dictionaryList = dictionaries.getDictionaries();

        if (dictionaryList.size() > 0) {

            ArrayList<WordInfo>  words = AssetsUtil.getWordsAssets(this);
            if(dictionaryList.size()< words.size()){
                int totalWords = words.size();
                int progress =1;
                mProgress.setMax(totalWords);
                for(WordInfo word : words){
                    updateProgress(progress, totalWords, word.getWord());
                    String cleanWord = StringUtil.removeNonAlphanumeric(word.getWord());
                    if(!cleanWord.isEmpty()) {
                        if (!dictionaries.existWord(cleanWord)) {
                            Dictionary d = new Dictionary();
                            d.setWord(cleanWord);
                            d.setMeaning("");
                            d.setActive(true);
                            dictionaries.add(d);
                        }
                    }
                    progress++;
                }
            }
        } else {
            ArrayList<WordInfo>  words = AssetsUtil.getWordsAssets(this);
            int totalWords = words.size();
            int progress = 0;
            mProgress.setMax(totalWords);

            for (WordInfo word : words) {
                updateProgress(progress, totalWords, word.getWord());
                String cleanWord = StringUtil.removeNonAlphanumeric(word.getWord());
                if (!cleanWord.isEmpty()) {
                    if (!dictionaries.existWord(cleanWord)) {
                        Dictionary d = new Dictionary();
                        d.setWord(cleanWord.toUpperCase());
                        d.setMeaning("");
                        dictionaries.add(d);
                    }
                }
                progress++;
            }
        }

        dictionaries.close();

    }

    private void updateProgress(final int index, final int totalWords, final String word) {
        mProgress.setProgress(index);
        final int progress = (index / totalWords) * 100;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressText.setText(String.valueOf(progress) + "%");
                mProgressMessageText.setText("Please wait... building offline dictionary..." + word);
                mContentText.setText("Please be patient...It takes a few minutes to build offline dictionary " + index + " of " + totalWords);

            }
        });
    }

    private void startApp() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
