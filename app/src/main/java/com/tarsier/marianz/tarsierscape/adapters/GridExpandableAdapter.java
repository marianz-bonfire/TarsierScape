package com.tarsier.marianz.tarsierscape.adapters;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tarsier.marianz.tarsierscape.R;
import com.tarsier.marianz.tarsierscape.models.WordHeader;
import com.tarsier.marianz.tarsierscape.views.CustomGridView;

import java.util.List;

public class GridExpandableAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private int[] _groupLogo = new int[]{
            R.drawable.ic_looks_3_24, R.drawable.ic_looks_4_24,
            R.drawable.ic_looks_5_24, R.drawable.ic_looks_6_24,
            R.drawable.ic_looks_7_24, R.drawable.ic_looks_8_24,
            R.drawable.ic_looks_9_24, R.drawable.ic_looks_24, };
    private List<WordHeader> _group;
    private List<List<String>> _childs;
    private List<List<Integer>> _childsImage;


    private LayoutInflater _inflater;
    public GridExpandableAdapter(Context context, List<WordHeader> group, List<List<String>> childs, List<List<Integer>> childsImage) {
        this._context = context;
        this._group = group;
        this._childs = childs;
        this._childsImage = childsImage;

        _inflater = LayoutInflater.from(context);
    }
    @Override
    public int getGroupCount() {
        return this._group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        WordHeader header = (WordHeader) getGroup(groupPosition);
        if(convertView==null) {
            convertView = _inflater.inflate(R.layout.layout_group, null);
        }

        groupHolder= new GroupHolder();
        groupHolder.text = (TextView)convertView.findViewById(R.id.textview_header);
        groupHolder.description = (TextView)convertView.findViewById(R.id.textview_description);
        groupHolder.count = (TextView)convertView.findViewById(R.id.textview_count);
        groupHolder.imageview = (ImageView)convertView.findViewById(R.id.group_image);

        groupHolder.text.setText(header.getHeader());
        groupHolder.description.setText(header.getDescription());
        groupHolder.count.setText(header.getCount());
        groupHolder.imageview.setImageResource(_groupLogo[groupPosition]);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = _inflater.inflate(R.layout.layout_grid, null);
        }

        CustomGridView gv = (CustomGridView)convertView.findViewById(R.id.gridview);
        ImageViewAdapter adapter = new ImageViewAdapter(this._context, this._childs.get(groupPosition), _childsImage.get(groupPosition));
        gv.setAdapter(adapter);
        int totalHeight = 0;
        for (int size = 0; size < adapter.getCount(); size++) {

           ConstraintLayout relativeLayout = (ConstraintLayout) adapter.getView(
                    size, null, gv);
            LinearLayout textView = (LinearLayout) relativeLayout.getChildAt(0);
            textView.measure(0, 0);

            //totalHeight +=  textView.getMeasuredHeight();
            totalHeight += 13.5;// textView.getMeasuredHeight();
        }
        gv.setHeight(totalHeight);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class GroupHolder {
    public TextView text;
    public TextView description;
    public TextView count;
    public ImageView imageview;
}