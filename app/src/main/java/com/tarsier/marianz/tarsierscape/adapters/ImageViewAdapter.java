package com.tarsier.marianz.tarsierscape.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.R;

import java.util.ConcurrentModificationException;
import java.util.List;

public class ImageViewAdapter extends BaseAdapter {

    private Context _context;
    private List<String> _childs;
    private List<Integer> _childsImage;

    public ImageViewAdapter(Context context, List<String> childs, List<Integer> childsImage){
        this._context= context;
        this._childs= childs;
        this._childsImage= childsImage;

    }

    @Override
    public int getCount() {
        return this._childs.size();
    }

    @Override
    public Object getItem(int position) {
        return this._childs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_word,parent, false);
        }

        TextView text_word = (TextView)convertView.findViewById(R.id.textview_word);
        TextView text_meaning = (TextView)convertView.findViewById(R.id.textview_meaning);

        //ImageView imageview = (ImageView)convertView.findViewById(R.id.item_image);

        text_word.setText(_childs.get(position));
       //imageview.setImageResource(_childsImage.get(position));

        return convertView;
    }
}
