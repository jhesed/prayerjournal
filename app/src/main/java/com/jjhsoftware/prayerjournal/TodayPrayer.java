package com.jjhsoftware.prayerjournal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jjhsoftware.prayerjournal.adapters.ExpandableListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
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
        expListView.expandGroup(0); // select pending by default

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
//                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
//                        .show();

                // Show pop up dialog of information
                showDetails();
                return true;

                // Show dialog box
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();

        // TODO: Fetch counter in DB
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

    private void showDetails() {
        /**
         Displays details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_view_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // close dialog box on click
        Button okButton = (Button)layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // edit dialog box
        ImageButton editButton = (ImageButton)layout.findViewById(R.id.edit_icon);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDetails();
            }
        });

        dialog.show();
    }

    private void editDetails() {
        /**
         Edit details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_add_edit_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // submit button on click
        Button okButton = (Button)layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add saving to DB here

                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        // close dialog box on click
        Button cancelButton = (Button)layout.findViewById(R.id.dialogCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addDetails() {
        /**
         Edit details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_add_edit_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // submit button on click
        Button okButton = (Button)layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add saving to DB here

                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        // close dialog box on click
        Button cancelButton = (Button)layout.findViewById(R.id.dialogCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}