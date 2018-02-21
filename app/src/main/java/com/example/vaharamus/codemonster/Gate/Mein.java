package com.example.vaharamus.codemonster.Gate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vaharamus.codemonster.MainScreen;
import com.example.vaharamus.codemonster.R;

public class Mein extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digictellus_activity);

        Thread thread = new Thread(){

            @Override
            public void run(){

            try                 {
                sleep(3000);        }

            catch (Exception e) {
                e.printStackTrace();      }

            finally             {
                Intent mainIntent = new Intent(Mein.this, MainScreen.class);
                startActivity(mainIntent); }
            }
        };
        thread.start();    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}


