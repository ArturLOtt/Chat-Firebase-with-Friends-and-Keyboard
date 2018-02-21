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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar zeug;
    private EditText userNamex;
    private EditText userMailx;
    private EditText userPassx;
    private Button btnCreateAccountx;
    private ProgressDialog resLoadingx;
    private FirebaseAuth auchx;
    private DatabaseReference storex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auchx = FirebaseAuth.getInstance();

        zeug = (Toolbar) findViewById(R.id.registertoolks);
        setSupportActionBar(zeug);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userNamex = (EditText) findViewById(R.id.txtRegister1ks);
        userMailx = (EditText) findViewById(R.id.txtRegister2ks);
        userPassx = (EditText) findViewById(R.id.txtRegister3ks);
        btnCreateAccountx = (Button) findViewById(R.id.btnRegister2ks);
        // barra de carregamento
        resLoadingx = new ProgressDialog(this);

        btnCreateAccountx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                final String name = userNamex.getText().toString();
                String email = userMailx.getText().toString();
                String password = userPassx.getText().toString();
                
                RegisterAccount(name, email, password);


            }
        });
    }

    private void RegisterAccount(final String name, String email, String password) {
        // se o usuario deixo o name vazio, vai notificar ele a escrever algo.

        if (TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this, "Please write your name.", Toast.LENGTH_LONG).show();
        }

        // os de baixo fazem o mesmo com email e password
        if (TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this, "Please write your email.", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this, "Please write your password.", Toast.LENGTH_LONG).show();
        }

        // avisa de erros, caso contrário ele cria a conta usando email e password
        else{
            //resLoadingx é uma barra de progresso mostrada ao ser efetuada a operaç, acho que se tirar não causa erro
            resLoadingx.setTitle("Creating new Account");
            resLoadingx.setMessage("Please Wait");
            resLoadingx.show();

            auchx.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String DeviceToken = FirebaseInstanceId.getInstance().getToken();

                        String currentUx = auchx.getCurrentUser().getUid();
                        //                        create a refernce and stall the refence inside this variable
                        storex = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUx);
                        storex.child("user_name").setValue(name);
                        storex.child("user_status").setValue("I am using the chat");
                        storex.child("user_image").setValue("default_profile");
                        storex.child("device_token").setValue(DeviceToken);
                        storex.child("user_thumb_image").setValue("default_image")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // se a operação de registro for um sucesso, ele será redirecionado para a MainScreen
                                            Intent mainIntentx = new Intent(RegisterActivity.this, MainScreen.class);
                                            //essa 1a linha abaixo para impedir ele de voltar para a tela de registro após já ter
                                            //registrado a conta
                                            mainIntentx.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(mainIntentx);
                                            finish();
                                        }
                                    }
                                });


                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "An error occurred during this opperation",
                                Toast.LENGTH_SHORT).show();
                    }
                resLoadingx.dismiss();

                }
            });
        }

    }
}
