package com.jjhsoftware.prayerjournal.adapters;

import java.util.List;
import java.util.Map;

import com.jjhsoftware.prayerjournal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> prayerCollections;
    private List<String> prayers;

    public ExpandableListAdapter(Activity context, List<String> prayers,
                                 Map<String, List<String>> prayerCollections) {
        this.context = context;
        this.prayerCollections = prayerCollections;
        this.prayers = prayers;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return prayerCollections.get(prayers.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String prayer = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.accordion_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.prayer_item);

        ImageView infoIcon = (ImageView) convertView.findViewById(R.id.info_icon);

        item.setText(prayer);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return prayerCollections.get(prayers.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return prayers.get(groupPosition);
    }

    public int getGroupCount() {
        return prayers.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String prayerName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.accordion_group,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.group_title);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(prayerName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}