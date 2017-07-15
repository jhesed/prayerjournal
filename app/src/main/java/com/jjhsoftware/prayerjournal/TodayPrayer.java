package com.jjhsoftware.prayerjournal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jjhsoftware.prayerjournal.adapters.ExpandableListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class TodayPrayer extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> prayerCollection;
    ExpandableListView expListView;

    // Labels
    private static final String PENDING_TITLE = "Pending";
    private static final String DONE_TITLE = "Done";
    private static final String ANSWERED_TITLE = "Answered";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_prayer);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.prayer_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, prayerCollection);
        expListView.setAdapter(expListAdapter);

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add(this.PENDING_TITLE + " (4)");
        groupList.add(this.DONE_TITLE + " (0)");
        groupList.add(this.ANSWERED_TITLE + " (0)");
    }

    private void createCollection() {
        // preparing prayers collection(child)
        String[] pendingItems = { "Prayer Title 1", "Prayer Title 2",
                "Prayer Title 3" };
        String[] doneItems = {};
        String[] answeredItems = {};

        prayerCollection = new LinkedHashMap<String, List<String>>();

        // TODO: Fetch this dynamically from DB
        loadChild(pendingItems);
        prayerCollection.put(groupList.get(0), childList);
        loadChild(doneItems);
        prayerCollection.put(groupList.get(1), childList);
        loadChild(answeredItems);
        prayerCollection.put(groupList.get(2), childList);
    }

    private void loadChild(String[] prayerModels) {
        childList = new ArrayList<String>();
        for (String model : prayerModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
}