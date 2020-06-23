package com.example.filepart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText textmsg;
    TextView mainView;
    static final int READ_BLOCK_SIZE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textmsg = (EditText)findViewById(R.id.editText);
        mainView = (TextView) findViewById(R.id.mainView);

        SQLiteOpenHelper dbHelper = new DatabaseHelper(getApplicationContext());

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query ("DRINK",
                    new String[] {"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID"},
                    "name = ?",
                    new String[] {"Latte"},
                    null, null,null);
            if (cursor.moveToFirst()) {
                mainView.setText("Latte's description is " + cursor.getString(1));
            }
        } catch(SQLiteException e) {
            mainView.setText("SQL error happened:\n" + e.toString());
        }
    }

    public void WriteBtn(View v) throws FileNotFoundException {
        // add-write text into file
        /*
        try {
            FileOutputStream fileout = openFileOutput("mytextfile.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(textmsg.getText().toString());
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        } */

        //Write data to SDcard
        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/MyFiles");
            directory.mkdirs();
            File file = new File(directory,  "textfile.txt");
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fOut);
            outputWriter.write(textmsg.getText().toString());
            outputWriter.close();
            Toast.makeText(getBaseContext(),"Data Written in SDCARD",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }

    public void ReadBtn(View v) {
        //reading text from file
        /*
        try {
            FileInputStream fileIn = openFileInput("mytextfile.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            char[] inputBuffer = new char[READ_BLOCK_SIZE];
            String s = ""; int charRead;

            while ((charRead = InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            mainView.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        } */

        // read file form sdcard
        String message;
        String buf = "";
        try
        {
            File f = new File("/sdcard/MyFiles/textfile.txt");
            FileInputStream fin = new FileInputStream(f);
            BufferedReader br = new BufferedReader(new InputStreamReader(fin));
            while ((message = br.readLine()) != null)
            {
                buf += message;
            }
            mainView.setText(buf);
            br.close();
            fin.close();
            Toast.makeText(getBaseContext(),"Data Recived from SDCARD",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteBtn(View v) {
        // delete file
        /*
        try {
            String dir = getFilesDir().getAbsolutePath();
            mainView.setText(dir);
            File file = new File(dir, "mytextfile.txt");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        // delete file form sdcard
        try {
            File file = new File("/sdcard/MyFiles/textfile.txt");
            boolean deleted = file.delete();
            mainView.setText("");
            Toast.makeText(getBaseContext(),"File Has been Deleted",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}