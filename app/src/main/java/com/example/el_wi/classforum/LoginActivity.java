package com.example.el_wi.classforum;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText nEmailField;
    private EditText nPasswordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CheckBox mCheckBox;
    private TextView mSignUp;

    //background
    ConstraintLayout relativeLayout;
    AnimationDrawable animationDrawable;

    //Progres Bar
    private ProgressBar mProgressBar;

    private Button btSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar) findViewById( R.id.progressBar ) ;

        nEmailField = (EditText) findViewById(R.id.etEmail);
        nPasswordField = (EditText) findViewById(R.id.etPassword);

        mCheckBox = (CheckBox) findViewById( R.id.lookPass ) ;

        mCheckBox.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCheckBox.isChecked()){
                    nPasswordField.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD );
                }
                if (!mCheckBox.isChecked()){
                    nPasswordField.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
                }
            }
        } );

        //intansiasi firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){

                    Intent mainAct = new Intent(LoginActivity.this, Drawer.class);
                    mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainAct);
                }
            }
        };

        //animasi backgroundya
        relativeLayout = (ConstraintLayout) findViewById(R.id.constrainlayoutLogin);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
        mSignUp = (TextView) findViewById(R.id.tvSignUp);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btSignIn = (Button) findViewById(R.id.btSignIn) ;
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

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



    private void startSignIn(){
        String email = nEmailField.getText().toString();
        String password = nPasswordField.getText().toString();
        mProgressBar.setVisibility( View.VISIBLE );
        Thread t = new Thread(  ){
            public void run (){
                try {
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

            Toast.makeText(LoginActivity.this,"Field is Empity", Toast.LENGTH_LONG).show();
            mProgressBar.setVisibility ( View.INVISIBLE );
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(!task.isSuccessful()){

                        Toast.makeText(LoginActivity.this, "Email or password didn't match", Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility( View.INVISIBLE );
                    }else {
                        mProgressBar.setVisibility( View.INVISIBLE );
                    }
                }
            });
        }

    }

}


