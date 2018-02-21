package com.example.vaharamus.codemonster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vaharamus.codemonster.Crown.AllUsersActivity;
import com.example.vaharamus.codemonster.Crown.SettingsActivity;
import com.example.vaharamus.codemonster.Crown.StartPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * Created by Vaharamus on 22/12/2017.
 */

public class MainScreen extends AppCompatActivity {

    private FirebaseAuth authx;
    private Toolbar zeug;
    private ViewPager pagerx;
    private TabLayout tabLayx;
    private TabsPagerAdapter tabAdapterx;

    FirebaseUser currentUserx;
    private DatabaseReference UserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);


        //check for user validating, if sign in or not sign in
        authx = FirebaseAuth.getInstance();

        currentUserx = authx.getCurrentUser();

        if(currentUserx != null){
            String online_user_id = authx.getCurrentUser().getUid();
            UserReference = FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(online_user_id);
        }

        // aqui eles set as coisas

        //tab para a Main Activity
        pagerx = (ViewPager) findViewById(R.id.pagerks);
        tabAdapterx = new TabsPagerAdapter(getSupportFragmentManager());
        pagerx.setAdapter(tabAdapterx);
        tabLayx = (TabLayout) findViewById(R.id.mainTabks);
        tabLayx.setupWithViewPager(pagerx);

        zeug = (Toolbar) findViewById(R.id.screentoolks);
        setSupportActionBar(zeug);
        getSupportActionBar().setTitle("Code Monster");
            }

    @Override
    protected void onStart() {
        super.onStart();

        // it will get the current user from firebase and store it here
        currentUserx = authx.getCurrentUser();

        //if current user is not log in, log out him
        if(currentUserx == null){
            LogOutUser();
            }
        else if(currentUserx != null){
            UserReference.child("online").setValue("true");
        }
    }

//    this method is for when the user minimaze the app, to become offline
    @Override
    protected void onStop() {
        super.onStop();
        if(currentUserx != null){
            UserReference.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }



    private void LogOutUser() {

        // isso manda o user para a tela de Login
        Intent startPageIntentx = new Intent(MainScreen.this, StartPageActivity.class);
        startPageIntentx.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntentx);
        finish();
    }

    // faz ele reconhecer o menu e o faz funcionar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // faz que quando clicar no logout do menu, ele vai deslogar
        if (item.getItemId() == R.id.logoutks) {

            if(currentUserx != null){
                UserReference.child("online").setValue(ServerValue.TIMESTAMP);
            }

            //esse método dá sign out do firebase
            authx.signOut();
            // esse método move ele de tela após sign out
            LogOutUser();
        }

        if(item.getItemId() == R.id.settingks){
            Intent settingsIntent = new Intent(MainScreen.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if(item.getItemId() == R.id.all_usersks){
            Intent allUsersIntent = new Intent(MainScreen.this, AllUsersActivity.class);
            startActivity(allUsersIntent);
        }

        return true;
    }
}


//ver 22 para corrigir erro no AllUsers

// tutorial 16, 17 e 18 comentadas, talvez procure funções de crop e image de outras fontes
// tutorial 17 não coloquei foto no firebase, se for seguir, rever vídeo
// tutorial 24

// tutorial 34 erro em fazer login correto no firebase, acho que pode ser a internet do Senai
// tutorial 35 tentar fazer o Firebase notification em casa
// tutorial 36, deu erro na Classe FirebaseMessagingService, acho que por conta do notification ai de cima que não foi bem executado
// tutorial 37, assim como os outros, fazer em casa no programa de notas dele
// tutorial 38, continuação dos demais



