package com.example.el_wi.classforum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    //Kamus Variable
    private EditText nNama;
    private EditText nEmail;
    private EditText nPassword, nPhoneNumber;
    private Button nSignUp;
    private CircleImageView mCircleImageView;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference  nDatabase;

    private ProgressDialog nProgresDialog;
    private Uri mIImageUri;

    //background
    ConstraintLayout relativeLayout;
    AnimationDrawable animationDrawable;

    //FirebaseStorage
    StorageReference mStorage;
    FirebaseFirestore mFirestore;

    ProgressBar mProgressBar;


    UploadTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mStorage = FirebaseStorage.getInstance().getReference().child("images");

        mAuth = FirebaseAuth.getInstance();
        nDatabase = FirebaseDatabase.getInstance().getReference().child("user");

        //Firebase Firestore
        mFirestore = FirebaseFirestore.getInstance();

        mIImageUri = null;
        //intansiasi firebase auth

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                    Intent mainAct = new Intent(RegisterActivity.this, Drawer.class);
                    mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainAct);
                }
            }
        };
        nSignUp = (Button) findViewById(R.id.regBtRegis) ;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarRegis);

        nNama = (EditText) findViewById(R.id.etRegNama);
        nEmail = (EditText) findViewById(R.id.etRegEmail);
        nPassword = (EditText) findViewById(R.id.etRegPassword);
        nPhoneNumber = (EditText) findViewById(R.id.etRegPhoneNumber);

        nSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();
                //Toast.makeText(RegisterActivity.this, "hasil", Toast.LENGTH_LONG).show();
            }
        });

        //animasi backgroundya
        relativeLayout = (ConstraintLayout) findViewById(R.id.constrainlayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        //CircleImage
        mCircleImageView = (CircleImageView) findViewById(R.id.imagebtnC);

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });

     }

     //animasi pada background

    @Override
    protected void onPause() {
        super.onPause();

        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }



    private void startRegister() {
        if(mIImageUri != null){

            mProgressBar.setVisibility(View.VISIBLE);
            final String nama = nNama.getText().toString().trim();
            String email = nEmail.getText().toString().trim();
            String password = nPassword.getText().toString().trim();
            String  tmp="";
            boolean daftar = false;
            if((nPhoneNumber.getText().toString().trim()).matches("[+]{1}[6]{1}[2]{1}[8]{1}[0-9]{10}")){
                tmp = nPhoneNumber.getText().toString().trim();
                daftar=true;
            }
            final String phoneNumber = tmp;

            if(!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email) && daftar){


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            final String user_id = mAuth.getCurrentUser().getUid();
                            final StorageReference user_profile = mStorage.child(user_id).child(mIImageUri.getLastPathSegment());
                            final DatabaseReference current_user_db = nDatabase.child(user_id);
                            uploadTask = user_profile.putFile(mIImageUri);
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return user_profile.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();

                                        current_user_db.child("name").setValue(nama);
                                        current_user_db.child("image").setValue(downloadUri.toString());
                                        current_user_db.child("phone").setValue( phoneNumber );
                                        Intent mainIntent = new Intent(RegisterActivity.this, Drawer.class);

                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                        startActivity(mainIntent);
                                        finish();



                                    } else {
                                        // Handle failures
                                        mProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(RegisterActivity.this, "Error"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                });
            }else {
                Toast.makeText(RegisterActivity.this, "isi field dengan benar",Toast.LENGTH_SHORT).show();

                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }else {
            Toast.makeText(RegisterActivity.this, "No Image",Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){

            mIImageUri = data.getData();
            mCircleImageView.setImageURI(mIImageUri);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void Login(View view) {
        Intent mainAct = new Intent(RegisterActivity.this, LoginActivity.class);
        mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAct);
    }
}
