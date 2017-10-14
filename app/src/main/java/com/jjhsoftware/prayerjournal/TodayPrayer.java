package com.jjhsoftware.prayerjournal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jjhsoftware.prayerjournal.adapters.ExpandableListAdapter;
import com.jjhsoftware.prayerjournal.db.PrayerContract;
import com.jjhsoftware.prayerjournal.db.PrayerDbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TodayPrayer extends Activity {

    List<String> groupList;
    List<String> childList;
    List<Integer> childIDList;
    Map<String, List<String>> prayerCollection;
    Map<String, List<Integer>> prayerCollectionIDs;
    ExpandableListView expListView;
    private PrayerDbHelper dbHelper;
    private static ArrayList<String> pendingItems;
    private static ArrayList<String> doneItems;
    private static ArrayList<String> answeredItems;
    private static ArrayList<Integer> pendingItemIDs;
    private static ArrayList<Integer> doneItemIDs;
    private static ArrayList<Integer> answeredItemIDs;

    // Labels
    private static final String PENDING_TITLE = "Pending";
    private static final String DONE_TITLE = "Done";
    private static final String ANSWERED_TITLE = "Answered";
    private static final String TAG = "TodayPrayer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_prayer);

        // Setup DB connection
        dbHelper = new PrayerDbHelper(this);

        initializeList();

        // Add prayer items section
        LinearLayout addButton = (LinearLayout) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPrayerItem();
            }
        });
    }

    private void initializeList() {
        /**
         * Initialize expandable list of prayer items
         * **/

        createGroupList();
        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.prayer_list);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, prayerCollection, prayerCollectionIDs);
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
                final Integer childId = (Integer) expListAdapter.getChildDBID(
                        groupPosition, childPosition);
                Log.d(TAG, "-------: " + childId);
                // Show pop up dialog of information
                showDetails(childId);
                return true;

                // Show dialog box
            }
        });
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();

        // Fetch prayer items from database
        pendingItems = getPrayerItems(0, 0, dbHelper.getDayInInteger());
        doneItems = getPrayerItems(1, 0, dbHelper.getDayInInteger());
        answeredItems = getPrayerItems(0, 1, dbHelper.getDayInInteger());

        groupList.add(this.PENDING_TITLE + " (" + pendingItems.size() + ")");
        groupList.add(this.DONE_TITLE + " (" + doneItems.size() + ")");
        groupList.add(this.ANSWERED_TITLE + " (" + answeredItems.size() + ")");
    }

    private void createCollection() {

        prayerCollection = new LinkedHashMap<String, List<String>>();
        prayerCollectionIDs = new LinkedHashMap<String, List<Integer>>();

        prayerCollection.put(groupList.get(0), pendingItems);
        prayerCollection.put(groupList.get(1), doneItems);
        prayerCollection.put(groupList.get(2), answeredItems);

        prayerCollectionIDs.put(groupList.get(0), pendingItemIDs);
        prayerCollectionIDs.put(groupList.get(1), doneItemIDs);
        prayerCollectionIDs.put(groupList.get(2), answeredItemIDs);

    }

    private ArrayList<String> getPrayerItems(int isDone, int isAnswered, int day) {

        // Initialize list\
        ArrayList<String> prayers = new ArrayList<String>();
        ArrayList<Integer> ids = new ArrayList<Integer>();

        Cursor cursor = dbHelper.selectAll(isDone, day, isAnswered);

        while (cursor.moveToNext()) {
            prayers.add(cursor.getString(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_TITLE)));

            // Set Ids as well
            Integer prayerId = cursor.getInt(cursor.getColumnIndex(PrayerContract.PrayerEntry._ID));
            ids.add(prayerId);
        }

        // Populate prayer item ids
        if (isDone == 0 && isAnswered == 0)
            pendingItemIDs = ids;
        else if (isDone == 1 && isAnswered == 0)
            doneItemIDs = ids;
        else if (isDone == 1 && isAnswered == 1)
            answeredItemIDs = ids;

        return prayers;
    }

    private void loadChild(ArrayList<String> prayerModels) {
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

    private void showDetails(Integer childId) {
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

        // Retrieve layout objects
        final TextView titleObj = (TextView)layout.findViewById(R.id.dialogTitle);
        final TextView contentObj = (TextView)layout.findViewById(R.id.content);
        final TextView answeredObj = (TextView)layout.findViewById(R.id.answeredText);

        // Retrieve details of prayer items from database
        Cursor cursor = dbHelper.select(childId);
        cursor.moveToNext();

        final String title = cursor.getString(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_TITLE));
        final String content = cursor.getString(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_CONTENT));
        final int isAnswered = cursor.getInt(cursor.getColumnIndex(PrayerContract.PrayerEntry.COL_IS_ANSWERED));
        String isAnsweredText = "Soon";
        final int prayerId = childId;

        if (isAnswered == 1)
            isAnsweredText = "Yes";

        // Update dialog conetnt
        titleObj.setText(title);
        contentObj.setText(content);
        answeredObj.setText(isAnsweredText);

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
                dialog.dismiss();
                editDetails(prayerId, title, content, isAnswered);
            }
        });

        dialog.show();
    }

    private void editDetails(int childId, String title, final String content, final int isAnswered) {
        /**
         Edit details on prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_edit_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // Retrieve layout objects
        final TextView titleObj = (TextView)layout.findViewById(R.id.prayer_title);
        final TextView contentObj = (TextView)layout.findViewById(R.id.prayer_content);
        final RadioGroup answeredObj = (RadioGroup) layout.findViewById(R.id.radio_answered_group);
        final int prayerId = childId;

        // Update content
        titleObj.setText(title);
        contentObj.setText(content);
        if (isAnswered == 1)
            answeredObj.check(R.id.radio_answered_yes);
        else
            answeredObj.check(R.id.radio_answered_no);

        dialog.show();

        // close dialog box on click
        ImageView cancelButton = (ImageView)layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // submit button on click
        Button okButton = (Button)layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.update(prayerId, titleObj.getText(), contentObj.getText(), isAnswered);
                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                initializeList();
                showDetails(prayerId);
            }
        });
    }

    private void addPrayerItem() {
        /**
         Add prayer item
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.activity_add_prayer, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        // Fetch form objects
        final EditText titleObj = (EditText)layout.findViewById(R.id.prayer_title_content);
        final EditText contentObj = (EditText)layout.findViewById(R.id.prayer_content);


        // TODO: Uncomment if reminder feature is already available
//        // Reminder Radio button
//        RadioGroup groupRadio=(RadioGroup)layout.findViewById(R.id.radio_reminder);
//        final TimePicker timePicker = (TimePicker) layout.findViewById(R.id.time_picker);
//        groupRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Overrid4e
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if(checkedId == R.id.radio_yes)
//                {
//                    timePicker.setVisibility(View.VISIBLE);
//                }
//                else if(checkedId == R.id.radio_no)
//                {
//                    timePicker.setVisibility(View.GONE);
//                }
//            }
//        });

        // submit button on click
        Button okButton = (Button)layout.findViewById(R.id.dialogOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleObj.getText().toString();
                String content = contentObj.getText().toString();

                dbHelper.insert(title, content);  // Save to database
                initializeList();  // Reload list
                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        // close dialog box on click
        ImageView cancelButton = (ImageView)layout.findViewById(R.id.cancel_icon);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}