package com.jjhsoftware.prayerjournal.db;

import android.provider.BaseColumns;

/**
 * Created by jhesed on 7/29/2017.
 */

public class PrayerContract {

    public static final String DB_NAME = "jjhsoftware.prayerjournal.prayerDAO";
    public static final int DB_VERSION = 1;

    public class PrayerEntry implements BaseColumns {
        public static final String TABLE = "prayer";

        // properties
        public static final String COL_TITLE = "title";
        public static final String COL_CONTENT = "content";
        public static final String COL_DAY = "day";
        public static final String COL_IS_REMINDER_ENABLED = "is_reminder_enabled";
        public static final String COL_REMINDER_TIME = "reminder_time";
        public static final String COL_IS_DONE = "is_done";
        public static final String COL_IS_ANSWERED = "is_answered";

        // dates and modifications
        public static final String COL_DATE_CREATED = "date_created";
        public static final String COL_DATE_MODIFIED = "date_modified";
        public static final String COL_DATE_LAST_SYNC = "date_last_synced";
        public static final String COL_IS_SYNCED = "is_synced";
    }

}
