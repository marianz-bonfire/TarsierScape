package com.tarsier.marianz.tarsierscape.utilities;

import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultUtil {
    public static List<String> GetResults(String wordToSolve, ArrayList<WordInfo> WORDS ){

        List<String> trimList = new ArrayList<String>();
        for (WordInfo line : WORDS) {
            String s = line.getWord().toLowerCase().trim();
            if (StringUtil.containsChars(s, wordToSolve.toLowerCase())) {
                if (!trimList.contains(s)) {
                    trimList.add(s);
                }
            }
        }

        Collections.sort(trimList, new StringLengthComparator());
        return  trimList;
    }
}
