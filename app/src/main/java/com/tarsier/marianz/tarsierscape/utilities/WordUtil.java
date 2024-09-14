package com.tarsier.marianz.tarsierscape.utilities;

import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;
import java.util.List;

public class WordUtil {
    private static ArrayList<WordInfo> _words;
    public static void setWords( ArrayList<WordInfo> words){
        _words= words;
    }

    public static  ArrayList<WordInfo> getWords(){
        return _words;
    }
}
