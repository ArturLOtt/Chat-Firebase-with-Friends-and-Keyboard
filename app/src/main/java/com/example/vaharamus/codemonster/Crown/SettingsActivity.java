package com.example.vaharamus.codemonster.Crown;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vaharamus.codemonster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView settingDispProfileImagex;
    private TextView settingDispNamex;
    private TextView settingDispStatusx;
    private Button settingChangeProfileImagex;
    private Button settingChangeStatusx;

    public final static int Gallery_pickx = 1;
    private StorageReference storeProfImgx;
//    reference for firebase database to retrieve data
    private DatabaseReference getUserDataReferencex;
//    to get the Uid (user id) from firebase
    private FirebaseAuth authccx;
    Bitmap thumb_bitmap = null;
    private StorageReference thumbImageRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        authccx = FirebaseAuth.getInstance();
// this will get the Unique user id if the user is online
        String online_user_id = authccx.getCurrentUser().getUid();

// created a reference to Firebase database root
//        now the reference to the Unique user Id is store in the getUserDataReferencex
        getUserDataReferencex = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        getUserDataReferencex.keepSynced(true);

// it will get the root from the firebase storage (armazenar fotos, vídeos, etc) e armazenar no storeProfImgx
        storeProfImgx = FirebaseStorage.getInstance().getReference().child("Profile_Images");
//  it will storage the thumb images here
        thumbImageRef = FirebaseStorage.getInstance().getReference().child("thumb_images");

        settingDispProfileImagex = (CircleImageView) findViewById(R.id.settingsImageks);
        settingDispNamex = (TextView) findViewById(R.id.settingsUsernameks);
        settingDispStatusx = (TextView) findViewById(R.id.settingsStatusks);
        settingChangeProfileImagex = (Button) findViewById(R.id.btn_imageks);
        settingChangeStatusx = (Button) findViewById(R.id.btn_statusks);

        loadingBar = new ProgressDialog(this);

//      using this reference we will retrieve the data from our database
        getUserDataReferencex.addValueEventListener(new ValueEventListener() {
            @Override
            //                by using datasnapshot you can get all the user data
            public void onDataChange(DataSnapshot dataSnapshot) {
//                  this datasnapshop has the reference of the getUserData Referncex. get the data from database
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
               // final (colocar no String de baixo, se seguir em usar o Picasso)
                String image = dataSnapshot.child("user_image").getValue().toString();
                String thumb_image = dataSnapshot.child("user_thumb_image").getValue().toString();


//                o dataSnapshot pega o valor do firebase (que está em " ") e guarda no name, status, image, etc
//                depois é settado no código abaixo
                settingDispNamex.setText(name);
                settingDispStatusx.setText(status);
                if (!image.equals("default_image")) {

//                    Picasso.with(SettingsActivity.this).load(image)
//              .networkPolicy(NetworkPolicy.OFFLINE)
//              .placeholder(R.drawable.default_image).into(settingChangeProfileImagex, new Callback);



//                    @Override
//                            public void OnSuccess(){
//

//
//                    }
//
//                    @Override
//                            public void OnError(){
//
//                Picasso.with(SettingsActivity.this).load(image)
 //   .placeholder(R.drawable.default_image).into(settingChangeProfileImagex);

//                    }
//
//                });
//





                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
////       Aqui é o método para mudar a foto do Profile
//        settingChangeProfileImagex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent galleryIntentx = new Intent();
//                galleryIntentx.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntentx.setType("imagel/*");
//                startActivityForResult(galleryIntentx, Gallery_pickx);
//            }
//        });


        settingChangeStatusx.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

//                essa linha abaixo para pegar o texto que já estava lá antes de mudar no Change Status e guardar no old_status
                String old_status = settingDispStatusx.getText().toString();

                Intent statusIntentx = new Intent(SettingsActivity.this, StatusActivity.class);
                statusIntentx.putExtra("user_status", old_status);
                startActivity(statusIntentx);
            }
        });
}



//// método de crop da imagem
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
        if (requestCode == Gallery_pickx && resultCode==RESULT_OK &&data !=null) {
//// esse método são as funcionalidds do botão Crop
//            Uri ImageUri = data.getData();
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);
        }
//
//    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//        CropImage.ActivityResult result = CropImage.getActivityResult(data);
//        if (resultCode == RESULT_OK) {
//
    loadingBar.setTitle("Updating Profile Image");
    loadingBar.setMessage("Please wait");
    loadingBar.show();

//            Uri resultUri = result.getUri();

////        método para compressar as imagens sem perder qualidd

//            File.thumb_filePathUri = new File(resultUri).getPath());
//
//
    String user_id = authccx.getCurrentUser().getUid();
//
//    try{
//        thumb_bitmap = new Compressor(this)
//                .setMaxWidth(200)
//                .setMaxHeight(200)
//                .setQuality(50)
//                .compressToBitmap(thumb_filePathUri);
//    }
//    catch (IOException e){
//        e.printStackTrace();
//    }
//    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream();
//    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//    final byte[] thumb_byte = byteArrayInputStream.toByteArray();


//    aqui ele guarda as imagens e nomeia segundo o user_id
    StorageReference filePathx = storeProfImgx.child(user_id + ".jpg");

    StorageReference thumb_filePath = thumbImageRef.child(user_id + " .jpg");

//    filePathx.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//            if(task.isSucessfull()){
//                Toast.makeText(SettingsActivity.this, "Saving your profile image to Storage",
//                        Toast.LENGTH_SHORT). show();
//            final String downloadUrl = task.getResult().getDownloadUrl().toString();
//
//                UploadTask uploadTask = thumb_filePath.putByte(thumb_byte);
//
//                uploadTasK.addOnCompleteListener (new OnCompleteListener<UploadTask.TaskSnapshot>(){
//                    @Override
//                            public void OnComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task){
//
//                        String thumb_donwloadUrl = thumb_task.getResult().getDownloadUrl().toString();
//                        if(task.isSuccessful()){
//                            Map update_user_data = new HashMap();
//                            update_user_data.put("user_image", downloadUrl);
//                            update_user_data.put("user_thumb_image", thumb_donwloadUrl);
//
//            getUserDataReferencex.updateChildren(update_user_data)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(SettingsActivity.this,
//                                    "Profile Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();

//        }
    //
//                        }
//                    });
//                        }
//        }
//    });



//            }
//            else{
//                Toast.makeText(SettingsActivity.this, "Error occured while uploading your profile pic.",
//                        Toast.LENGTH_SHORT).show();

    loadingBar.dismiss();

//            }
//
//        }
//    });
//
//        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//            Exception error = result.getError();
//        }
//    }
    }

}
