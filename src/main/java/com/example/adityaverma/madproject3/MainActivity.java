package com.example.adityaverma.madproject3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adityaverma.madproject3.db.TaskContract;
import com.example.adityaverma.madproject3.db.TaskDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);



        //get data from Database
        updateUI();

    }


    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
    }

        cursor.close();
        db.close();





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d(TAG, "Add a new task");
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new chapter")
                        .setMessage("Title: ")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());

                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();



                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                values.put(TaskContract.TaskEntry.COL_TASK_NOTES, " -> ");


                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_IGNORE);



                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                updateUI();
                dialog.show();
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }




    public void deleteTask(View view)  {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);


        String task = String.valueOf(taskTextView.getText());

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }


    public void openText(View view) {

        View parent = (View) view.getParent();

        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());


        //TODO: try to get the content of the note here, given the title name 'task'
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor kor = null;

        String result = "";
        try {
            kor = db.rawQuery("SELECT notes FROM tasks2 WHERE title=?", new String[] {task});
            if (kor.getCount() > 0) {
                kor.moveToFirst();
                result = kor.getString(kor.getColumnIndex("notes"));
            }
        } finally {
            kor.close();
        }
        db.close();
        if (result != null)
        Log.d("should print res here", result);
        //now result contains the text that might have been saved before, send it along with the intent
        // sends result






        Intent myIntent = new Intent(MainActivity.this, Text.class);
        Bundle extras = new Bundle();

        extras.putString("EXTRA_SESSION_ID", task);
        extras.putString("EXTRA_SESSION_ID2", result);
        myIntent.putExtras(extras);

        //myIntent.putExtra("EXTRA_SESSION_ID", task);


        MainActivity.this.startActivity(myIntent);

    }

}
