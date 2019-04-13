package com.example.rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity {

    private int REQUEST_IMAGE_GALLERY = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private File currentImageFile;
    private ImageView imageView;
    private TextView add_pic;
    private String profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        getSupportActionBar().setTitle("  Create Profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.arrow_with_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        imageView = findViewById(R.id.imageView);
        add_pic = findViewById(R.id.add_pic);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

    public void profilePicture(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.save_changes, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        new CreateAsync(this).execute(uName, pswd, fname, lname, position, department, story, profile_picture);
    }

    public void sendResults(String s) {
        Toast.makeText(this, "User created sucessfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, YourProfileActivity.class);
        intent.putExtra("key", s);
        startActivity(intent);
    }
}
