package com.tarsier.marianz.tarsierscape.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.models.WordHeader;
import com.tarsier.marianz.tarsierscape.models.WordInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableWordsListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<WordHeader> _listDataHeader;
    private HashMap<String, ArrayList<WordInfo>> _listDataChild;

    public ExpandableWordsListAdapter(Context context, ArrayList<WordHeader> listDataHeader,
                                      HashMap<String, ArrayList<WordInfo>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getHeader()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final WordInfo wordInfo = (WordInfo) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_word, null);
        }

        TextView textAmount = (TextView) convertView.findViewById(R.id.textview_word);
        TextView textMeaning = (TextView) convertView.findViewById(R.id.textview_meaning);


        textAmount.setText(wordInfo.getWord());
        textMeaning.setText("");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getHeader()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if (_listDataHeader == null)
            return 0;
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        WordHeader header = (WordHeader) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_group, null);
        }

        TextView textHeader = (TextView) convertView.findViewById(R.id.textview_header);
        TextView textCount = (TextView) convertView.findViewById(R.id.textview_count);
        TextView textDescription = (TextView) convertView.findViewById(R.id.textview_description);

        //lblFilter.setTypeface(null, Typeface.BOLD);
        textHeader.setText(header.getHeader());
        textDescription.setText(header.getDescription());
        textCount.setText(header.getCount());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}