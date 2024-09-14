package com.tarsier.marianz.tarsierscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.models.Dictionary;
import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;

public class WordGridAdapter extends ArrayAdapter<WordInfo> {

    public WordGridAdapter(Context context, ArrayList<WordInfo> switches) {
        super(context, 0, switches);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        WordInfo s = getItem(i);
        if (convertView == null) {
            // convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_remote, null);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_word, parent, false);
        }
        TextView textViewWord = convertView.findViewById(R.id.textview_word);
        TextView textViewMeaning = convertView.findViewById(R.id.textview_meaning);
        //ImageView imageViewAndroid = convertView.findViewById(R.id.image_switch);

        textViewWord.setText(s.getWord());

        textViewWord.setTag(s.getId());
        textViewMeaning.setText("");
        return convertView;
    }
}
