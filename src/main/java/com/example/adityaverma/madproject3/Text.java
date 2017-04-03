package com.example.adityaverma.madproject3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.adityaverma.madproject3.db.TaskContract;
import com.example.adityaverma.madproject3.db.TaskDbHelper;

public class Text extends AppCompatActivity {
    public String data;
    public String task;
    public String result;

    //new ***
    private TaskDbHelper mHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mHelper = new TaskDbHelper(this);

        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        task = extras.getString("EXTRA_SESSION_ID");
        result = extras.getString("EXTRA_SESSION_ID2");

        Log.d("title", task);
        if ( result != null) {
            Log.d("result", result);
        }
        EditText edt = (EditText)findViewById(R.id.textField);
        if ( result != null)
        edt.setText(result);

    }

    public void save(View view) {


        //this function is used to save the data the person enters on the textfield

        EditText edt = (EditText)findViewById(R.id.textField);
        data = edt.getText().toString();




        // old:::::::  String task = intent.getStringExtra("EXTRA_SESSION_ID");
        // :::::::::   Log.d("title: " , task);

        Log.d("data: " , data);

        //now we have task and data, need to update the table from THIS class


        //UPDATEs THE DATABASE FROM HERE WITH task and data

        SQLiteDatabase db = mHelper.getWritableDatabase();


        /**
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
        values.put(TaskContract.TaskEntry.COL_TASK_NOTES, data);

        db.update(TaskContract.TaskEntry.TABLE, values, "_id="+TaskContract.TaskEntry._ID, null);
    **/
        String strSQL = "UPDATE tasks2 SET notes ='"+data+"' WHERE title='"+task+"'";

        db.execSQL(strSQL);



        // db.update(TaskContract.TaskEntry.TABLE, values, null , null);   //  <- is wrong



        db.close();

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);



    }
}
