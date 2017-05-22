package com.example.umit.simplefilemanager.viewmodel;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.umit.simplefilemanager.R;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

import static com.example.umit.simplefilemanager.viewmodel.Constants.APP_CONFIG_SHARED_PREF_FILE;
import static com.example.umit.simplefilemanager.viewmodel.Constants.INITIALIZATION_PATH_SHARED_PREF;
import static com.example.umit.simplefilemanager.viewmodel.FileExistenceValidator.ValidationResult.*;

public class MainActivity extends AppCompatActivity {
    private File currentFile;
    private FileAdapter adapter;

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
        ButterKnife.bind(this);

        this.adapter = new FileAdapter(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences appConfig = getApplicationContext().getSharedPreferences(APP_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);
        String initializationPath = appConfig.getString(INITIALIZATION_PATH_SHARED_PREF, Environment.getExternalStorageDirectory().getPath());
        FileExistenceValidator.ValidationResult result = FileExistenceValidator.getInstance().validate(initializationPath);
        this.changeCurrentDirectory(result.equals(DIRECTORY) ? initializationPath : Environment.getExternalStorageDirectory().getPath(), result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                this.refreshActivity();
                return true;

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void refreshActivity() {
        this.finish();
        this.startActivity(getIntent());

    }

    public void onBackPressed() {
        this.changeCurrentDirectory(currentFile.getParent());
    }

    public void changeCurrentDirectory(String path) {
        this.changeCurrentDirectory(path, FileExistenceValidator.getInstance().validate(path));
    }


    public void changeCurrentDirectory(String path, FileExistenceValidator.ValidationResult result) {
        File file = null;
        if (result.isSuccessResult()) {
            file = new File(path);
        }

        switch (result) {
            case PERMISSION_ACCESS_ERROR:
                AlertDialog alertbox = new AlertDialog.Builder(this)
                        .setMessage("Do you want to exit application?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        }).setCancelable(true)
                        .create();
                alertbox.show();
                return;
            case FILE:
                try {
                    FileOpen.openFile(getApplicationContext(), file);
                } catch (IOException ignored) {
                }
                return;
            case DIRECTORY:
                currentFile = file;
                adapter.reload();
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }
}



