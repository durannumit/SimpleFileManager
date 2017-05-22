package com.example.umit.simplefilemanager.viewmodel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umit.simplefilemanager.R;

import static com.example.umit.simplefilemanager.viewmodel.Constants.APP_CONFIG_SHARED_PREF_FILE;
import static com.example.umit.simplefilemanager.viewmodel.Constants.INITIALIZATION_PATH_SHARED_PREF;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
    private EditText defaultPathEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_acticity);

        TextView externalPathTextView = (TextView)findViewById(R.id.externalPathTextView);
        externalPathTextView.setText(EXTERNAL_PATH);

        Button setDefaultPathButton = (Button)findViewById(R.id.setDefaultPathButton);
        setDefaultPathButton.setOnClickListener(this);

        defaultPathEditText = (EditText)findViewById(R.id.defaultPathE1ditText);
        SharedPreferences appConfig = getApplicationContext().getSharedPreferences(APP_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);
        String initializationPath = appConfig.getString(INITIALIZATION_PATH_SHARED_PREF, "").replaceAll(EXTERNAL_PATH, "");
        defaultPathEditText.setText(initializationPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.thread_example, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        String path = EXTERNAL_PATH + defaultPathEditText.getText();
        FileExistenceValidator.ValidationResult result = FileExistenceValidator.getInstance().validate(path);
        if (result.equals(FileExistenceValidator.ValidationResult.DIRECTORY)) {
            SharedPreferences appConfig = getApplicationContext().getSharedPreferences(APP_CONFIG_SHARED_PREF_FILE, MODE_PRIVATE);
            SharedPreferences.Editor edit = appConfig.edit();
            edit.putString(INITIALIZATION_PATH_SHARED_PREF, path);
            edit.apply();

            this.finish();
            NavUtils.navigateUpFromSameTask(this);
        } else {
            Toast.makeText(getApplicationContext(), "This is a not valid directory!", Toast.LENGTH_SHORT).show();
        }
    }
}
