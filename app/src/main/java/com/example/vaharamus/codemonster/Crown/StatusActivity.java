package com.example.vaharamus.codemonster.Crown;

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

import com.example.vaharamus.codemonster.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar zeugenx;
    private EditText txtInputx;
    private Button btnSavex;
    private ProgressDialog loadingx;

    private DatabaseReference changeStatusRef;
//    firebase auth to get the current online user
    private FirebaseAuth atchim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        atchim = FirebaseAuth.getInstance();
        String user_id = atchim.getCurrentUser().getUid();
        changeStatusRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        zeugenx = (Toolbar) findViewById(R.id.status_app_bar);
        setSupportActionBar(zeugenx);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtInputx = (EditText) findViewById(R.id.txtStatusStatusks);
        btnSavex = (Button) findViewById(R.id.btnStatusStatusks);
        loadingx = new ProgressDialog(this);

//        eles pegam o nome old_status e mostram na tela de ediç de status para ser editavel
        String old_status = getIntent().getExtras().get("user_status").toString();
        txtInputx.setText(old_status);

        btnSavex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_status = txtInputx.getText().toString();
                ChangeProfileStatus(new_status);

            }
        });
    }

    private void ChangeProfileStatus(String new_status) {
        if(TextUtils.isEmpty(new_status)){
            Toast.makeText(StatusActivity.this, "Please write your status here",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            loadingx.setTitle("Change Profile Status");
            loadingx.setMessage("Please wait, while we update your profile");
            loadingx.show();

            changeStatusRef.child("user_status").setValue(new_status)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loadingx.dismiss();

                                Intent statusIntent = new Intent(StatusActivity.this, SettingsActivity.class);
                                startActivity(statusIntent);

                                Toast.makeText(StatusActivity.this, "Profile Updated Successfully",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(StatusActivity.this, "An Error has occurred",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }
}
