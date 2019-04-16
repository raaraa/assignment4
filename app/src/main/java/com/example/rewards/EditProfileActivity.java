package com.example.rewards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private File currentImageFile;
    private String profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        getSupportActionBar().setTitle("  Edit Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

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

        //add_pic.setBackgroundColor(Color.TRANSPARENT);
    }




    public void profilePicture(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.save_changes, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.logo);
        builder.setTitle("Profile Picture");
        builder.setMessage("Take picture from:");
        builder.setView(view);

        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doCamera();
            }
        });

        builder.setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doGallery();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void doCamera() {
        currentImageFile = new File(getExternalCacheDir(), "appimage_" + System.currentTimeMillis() + ".jpg");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    public void doGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
    }

    public void doConvert(Bitmap origBitmap) {
        if (imageView.getDrawable() == null)
            return;

        int jpgQuality = 50;

        ByteArrayOutputStream bitmapAsByteArrayStream = new ByteArrayOutputStream();
        origBitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, bitmapAsByteArrayStream);
        String imgString = Base64.encodeToString(bitmapAsByteArrayStream.toByteArray(), Base64.DEFAULT);
        profile_picture = imgString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                processGallery(data);
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                processCamera();
            } catch (Exception e) {
                Toast.makeText(this, "onActivityResult: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void processCamera() {
        Uri selectedImage = Uri.fromFile(currentImageFile);
        imageView.setImageURI(selectedImage);
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        currentImageFile.delete();
        doConvert(bm);
    }

    private void processGallery(Intent data) {
        Uri galleryImageUri = data.getData();
        if (galleryImageUri == null)
            return;

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(galleryImageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(selectedImage);
        add_pic.setBackgroundColor(Color.TRANSPARENT);
        doConvert(selectedImage);
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
        Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        doConvert(bm);

        new UpdateAsync(this).execute(uName, pswd, fname, lname, position, department, story, profile_picture);
    }

    public void sendResults(String s) {
        makeCustomToast(this, s, Toast.LENGTH_LONG);
        String uName = ((EditText) findViewById(R.id.username)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.password)).getText().toString();
        new LoginAsync(this).execute("A20349053", uName,pswd);
    }

    public void logBack(String s){
        Intent new_intent = new Intent(this, YourProfileActivity.class);
        new_intent.putExtra("key",s);
        startActivity(new_intent);
    }

    public static void makeCustomToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(100, 50, 100, 50);
        tv.setTextColor(Color.WHITE);
        toast.show();
    }
}
