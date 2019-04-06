package com.example.rewards;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveProfile() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.save_changes, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Changes?");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createAsync();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void createAsync(){
        String uName = ((EditText) findViewById(R.id.username)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.password)).getText().toString();
        String fname = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lname = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String position = ((EditText) findViewById(R.id.position)).getText().toString();
        String department = ((EditText) findViewById(R.id.department)).getText().toString();
        String story = ((EditText) findViewById(R.id.story)).getText().toString();

        new CreateAsync(this).execute(uName, pswd, fname, lname, position, department, story);
    }

    public void sendResults() {
        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show();
    }
}
