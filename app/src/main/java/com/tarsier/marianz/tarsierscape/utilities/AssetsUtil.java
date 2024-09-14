package com.tarsier.marianz.tarsierscape.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AssetsUtil {
    private static final Object TAG = "TarsierScape";

    public static ArrayList<WordInfo> getWordsAssets(Context context) {
        ArrayList<WordInfo> words = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("words.txt"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line;
            while ((line = reader.readLine()) != null) {
                String trimLine = StringUtil.removeNonAlphanumeric(line);
                if (!trimLine.isEmpty()) {
                    if(trimLine.length() >= 3) {
                        WordInfo wordInfo = new WordInfo();
                        wordInfo.setWord(trimLine);
                        words.add(wordInfo);
                    }
                }
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return words;
    }

    private static String WORD ="word.txt";
    public static void  writeToInternalStorage(Context context)   {
        AssetManager assetManager = context.getAssets();
        File location =  getLocationPath(context);
        //if(!location.exists()){
           try{
               InputStream inputStream = assetManager.open(WORD );
               OutputStream outputStream = new FileOutputStream(location);

               byte[] buffer = new byte[inputStream.available()];
               int length;
               while ((length= inputStream.read(buffer))!=-1){
                   outputStream.write(buffer, 0, length);
               }

           }catch (FileNotFoundException e){
               e.printStackTrace();
           }catch (IOException e){
               e.printStackTrace();
           }
      //  }

    }

    private static File getLocationPath(Context context){
        //String directory = context.getFilesDir()+"/TarsierScape";
        File fileDirectory = new File(Environment.getExternalStorageDirectory()+"/TarsierScape");
        if(!fileDirectory.exists()){
            fileDirectory.mkdir();
        }
        return new File(fileDirectory, WORD);
    }
}
