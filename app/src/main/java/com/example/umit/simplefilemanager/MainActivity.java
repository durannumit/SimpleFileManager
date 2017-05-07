package com.example.umit.simplefilemanager;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "MainActivity";
    private List<String> fileList;
    private ArrayAdapter<String> adapter;
    private String[] filesArray;
    private File parentFile;
    private TextView txt;
    private ListView listView ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fileList);
        listView = (ListView) findViewById(R.id.listView);
        txt = (TextView) findViewById(R.id.txt);




        File externalStorage = Environment.getExternalStorageDirectory();
        parentFile = externalStorage;
        txt.setText(parentFile.toString());

        filesArray = externalStorage.list();

        fileList.add("../");
        for (String s : filesArray) {
            if (new File(externalStorage, s).isDirectory()) {
                fileList.add(s + "/");
            } else {
                fileList.add(s);
            }
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file;
        if (fileList.get(position).equals("..")) {
            if (parentFile.toString().equals("/")) {
                return;
            }
            file = parentFile.getParentFile();
        } else {
            file = new File(parentFile, fileList.get(position));
        }
        if (file.isDirectory()) {
            fileList.clear();
            filesArray = file.list();
            fileList.add("../");

            for (String s : filesArray) {
                if (new File(file, s).isDirectory()) {
                    fileList.add(s + "/");
                } else {
                    fileList.add(s);
                }
            }

            parentFile = file;
            adapter.notifyDataSetChanged();
            txt.setText(file.toString());
        } else if (file.isFile()) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String selectType = "*/*";
            if (file.toString().endsWith("txt") || file.toString().endsWith("xml")) {
                selectType = "text/plain";
            } else if (file.toString().endsWith("png") || file.toString().endsWith("jpg")) {
                selectType = "image/*";
            } else if (file.toString().endsWith("mp4")) {
                selectType = "video/*";
            }
            intent.setDataAndType(Uri.fromFile(file), selectType);
            startActivity(intent);
        }

        Log.e(TAG, file.toString());
        Log.e(TAG, "parent " + parentFile);
        Log.e(TAG, "file.isDirectory()" + file.isDirectory());
        Log.e(TAG, "file.isFile()" + file.isFile());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }





    public void onBackPressed(){

        final File file;

        if (parentFile.getParentFile().isDirectory()) {
            if (!parentFile.getParentFile().canRead()) {
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("Do you want to exit application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                                //close()
                            }
                        }).setCancelable(true)
                        .create();
                alertbox.show();
            } else {
                fileList.clear();

                file = parentFile.getParentFile();
                filesArray = file.list();
                fileList.add("../");

                for (String s : filesArray) {
                    if (new File(file, s).isDirectory()) {
                        fileList.add(s + "/");
                    } else {
                        fileList.add(s);
                    }
                    parentFile = file;
                    adapter.notifyDataSetChanged();
                    txt.setText(file.toString());
                }
            }
        }
    }



}



