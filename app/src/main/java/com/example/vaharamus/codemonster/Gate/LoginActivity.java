package com.example.vaharamus.codemonster.Gate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vaharamus.codemonster.MainScreen;
import com.example.vaharamus.codemonster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    private Toolbar zeug;
    private FirebaseAuth outchx;

    private Button loginButtonx;
    private EditText loginEmailx;
    private EditText loginPasswordx;
    private ProgressDialog loadingx;
// 1st reference to store the phone token
    private DatabaseReference usersReference;

                

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        outchx = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        zeug = (Toolbar) findViewById(R.id.logintoolks);
        setSupportActionBar(zeug);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //se encontrar uma forma melhor de colocar a toolbar, deletar esse zeug e tudo dele.
        
        loginButtonx = (Button) findViewById(R.id.btnLogin2ks);
        loginEmailx = (EditText) findViewById(R.id.txtLogin2ks); 
        loginPasswordx = (EditText) findViewById(R.id.txtLogin3ks);
        loadingx = new ProgressDialog(this);
       
        loginButtonx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = loginEmailx.getText().toString();
                String password = loginPasswordx.getText().toString();
                                
            // um método para o user fazer login
                
                LoginUserAccount (email, password);
                

                
            }
            
        });
                
        
    }

    private void LoginUserAccount(String email, String password) {
    // checar se o user deixa algum campo vazio
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Please write your email.",
                    Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please write your password.",
                    Toast.LENGTH_SHORT).show();
        }
            else{
            loadingx.setTitle("Loging Account");
            loadingx.setMessage("Please Wait, we are verifying your credentials");
            loadingx.show();

            outchx.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //metodo que se a ação é bem sucedida, ela leva para a atividd.
                    if(task.isSuccessful()){

                        String online_user_id = outchx.getCurrentUser().getUid();
// using this line it will get the token and it will store in this Device Token
                        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
//   to store in the firebase database
                        usersReference.child(online_user_id).child("device_token")
                                .setValue(DeviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent mainIntent = new Intent (LoginActivity.this, MainScreen.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();


                            }
                        });
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Please check the email and/or password.",
                                Toast.LENGTH_SHORT).show();
                    }
                    loadingx.dismiss();
                }
            });





        }


    }
}
