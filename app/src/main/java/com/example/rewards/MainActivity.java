package com.example.rewards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText password;
    private Button login_btn;
    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
    }

    public void Login(View view){
        String sId = "A20349053";
        String uName = ((EditText) findViewById(R.id.username_txt)).getText().toString();
        String pswd = ((EditText) findViewById(R.id.password_txt)).getText().toString();

        new LoginAsync(this).execute(sId, uName, pswd);
    }

    public void CreateAccount(View view){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    public void loginFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void sendResults(String s) {
        Intent intent = new Intent(this, YourProfileActivity.class);
        intent.putExtra("key",s);
        startActivity(intent);
    }
}
