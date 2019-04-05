package com.example.rewards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

    }

    public void save(View v) {
        String uName = ((EditText) findViewById(R.id.username)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.password)).getText().toString();
        String fname = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lname = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String position = ((EditText) findViewById(R.id.position)).getText().toString();
        String department = ((EditText) findViewById(R.id.department)).getText().toString();
        String story = ((EditText) findViewById(R.id.story)).getText().toString();

        new CreateAsync(this).execute(uName, pswd, fname, lname, position, department, story);
    }

    public void sendResults(String s) {
        ((TextView) findViewById(R.id.res)).setText(s);
    }
}
