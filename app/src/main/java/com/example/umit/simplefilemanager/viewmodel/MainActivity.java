package com.example.umit.simplefilemanager.viewmodel;


import android.content.DialogInterface;
import android.content.Intent;
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

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;

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
        this.changeCurrentDirectory(Environment.getExternalStorageDirectory().getPath());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                finish();
                startActivity(getIntent());
                return true;

            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    public void onBackPressed() {
        this.changeCurrentDirectory(currentFile.getParent());
    }

    public void changeCurrentDirectory(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            try {
                FileOpen.openFile(getApplicationContext(), file);
            } catch (IOException ignored) {
            }
            return;
        }
        if (!file.canRead()) {
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
        }
        currentFile = file;
        adapter.reload();
    }

    public File getCurrentFile() {
        return currentFile;
    }
}



