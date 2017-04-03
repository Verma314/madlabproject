package com.example.adityaverma.madproject3.db;

/**
 * Created by Aditya Verma on 02/04/2017.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.adityaverma.madproject3.db";
    public static final int DB_VERSION = 3;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks2";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_NOTES = "notes";
    }
}
