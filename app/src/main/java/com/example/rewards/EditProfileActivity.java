package com.example.rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    public EditText username;
    public EditText password;
    public EditText first_name;
    public EditText last_name;
    public EditText department;
    public EditText position;
    public EditText story;
    public TextView story_view;
    public ImageView imageView;
    public TextView add_pic;
    public static int MAX_CHARS = 360;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        Intent intent = getIntent();

        username = findViewById(R.id.username);
        username.setEnabled(false);
        password = findViewById(R.id.password);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        department = findViewById(R.id.department);
        position = findViewById(R.id.position);
        story = findViewById(R.id.story);
        imageView = findViewById(R.id.imageView);
        add_pic = findViewById(R.id.add_pic);
        story_view = findViewById(R.id.story_view);
        story.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();

        username.setText(intent.getStringExtra("username"));
        password.setText(intent.getStringExtra("password"));
        first_name.setText(intent.getStringExtra("firstname"));
        last_name.setText(intent.getStringExtra("lastname"));
        department.setText(intent.getStringExtra("department"));
        position.setText(intent.getStringExtra("position"));
        story.setText(intent.getStringExtra("story"));

        String imgString = intent.getStringExtra("image");
        byte[] imageBytes = Base64.decode(imgString,  Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imageView.setImageBitmap(bitmap);

        add_pic.setBackgroundColor(Color.TRANSPARENT);
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

    private void addTextListener() {
        story.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int len = s.toString().length();
                String countText = "Your Story: (" + len + " of " + MAX_CHARS + ")";
                story_view.setText(countText);
            }
        });
    }

    public void saveProfile() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.save_changes, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("Save Changes?");
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateAsync();
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

    public void updateAsync(){
        String uName = ((EditText) findViewById(R.id.username)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.password)).getText().toString();
        String fname = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String lname = ((EditText) findViewById(R.id.last_name)).getText().toString();
        String position = ((EditText) findViewById(R.id.position)).getText().toString();
        String department = ((EditText) findViewById(R.id.department)).getText().toString();
        String story = ((EditText) findViewById(R.id.story)).getText().toString();

        new UpdateAsync(this).execute(uName, pswd, fname, lname, position, department, story);
    }

    public void sendResults(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
