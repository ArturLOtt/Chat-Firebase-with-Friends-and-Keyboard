package com.example.vaharamus.codemonster.Crown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vaharamus.codemonster.Gate.LoginActivity;
import com.example.vaharamus.codemonster.Gate.RegisterActivity;
import com.example.vaharamus.codemonster.R;

public class StartPageActivity extends AppCompatActivity {

    private Button btnSignx;
    private Button btnRegisterx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        btnRegisterx = (Button) findViewById(R.id.btnRegisterks);
        btnSignx = (Button) findViewById(R.id.btnSignks);

        //botão redireciona, por clique, para a RegisterActivity
        btnRegisterx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(StartPageActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        //botão redireciona, por clique, para a LoginActivity
        btnSignx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(logInIntent);
            }
        });

    }
}
