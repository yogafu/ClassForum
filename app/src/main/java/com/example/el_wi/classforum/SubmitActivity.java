package com.example.el_wi.classforum;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SubmitActivity extends AppCompatActivity {


    private static final int GALLERY_REQUEST = 1;
    private ImageButton mSelectImage;
    private EditText mDesc;
    private Spinner mMatkulField;
    private TextView mDate;
    private Button mSubmit;
    private Uri mUriImage = null;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private ProgressDialog mProgress;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabes;
    UploadTask uploadTask;
    //    private StorageReferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        mDatabes = FirebaseDatabase.getInstance().getReference().child("Task");

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mMatkulField = (Spinner) findViewById(R.id.matkulField);
        mDate = (TextView) findViewById(R.id.tglField);

        mDesc = (EditText) findViewById(R.id.deskField);

        mSubmit = (Button) findViewById(R.id.btn_sumitTugas);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        mProgress = new ProgressDialog(this);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPosting();
            }
        });

        mSelectImage = (ImageButton) findViewById(R.id.imageScale);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mDate.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        } );

        mMatkulField.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    private void startPosting() {

        final String sNamaMatkul = mMatkulField.getSelectedItem().toString();//String.valueOf (mMatkulField.getSelectedItemId ())  ;
        final String dateS = mDate.getText().toString().trim();
        final String sDesc = mDesc.getText().toString().trim();
        mProgress.setMessage("Submit Tugas....");
        mProgress.show();

        if(!TextUtils.isEmpty(sNamaMatkul)  &&!TextUtils.isEmpty(sDesc) &&!TextUtils.isEmpty(dateS)){

            final StorageReference filepath = mStorageReference.child("Tugas_Images").child(mUriImage.getLastPathSegment());
            uploadTask = filepath.putFile(mUriImage);

            // Register observers to listen for when the download is done or if it fails
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference newPost = mDatabes.push();

                    newPost.child("title").setValue(sNamaMatkul);
                    newPost.child("desc").setValue(sDesc);
                    newPost.child("image").setValue(downloadUri.toString());
                    newPost.child("uid").setValue((FirebaseAuth.getInstance().getUid()));
                    newPost.child("deadline").setValue(dateS);

                    mProgress.dismiss();

                    startActivity(new Intent(SubmitActivity.this, Drawer.class));

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

//            filepath.putFile(mUriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                   Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                    DatabaseReference newPost = mDatabes.push();
//
//                    newPost.child("title").setValue(sNamaMatkul);
//                    newPost.child("desc").setValue(sDesc);
//                    newPost.child("image").setValue(downloadUrl.toString());
//                    newPost.child("uid").setValue((FirebaseAuth.getInstance().getUid()));
//
//                    mProgress.dismiss();
//
//                    startActivity(new Intent(SubmitActivity.this, Drawer.class));
//                }
//            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mUriImage = data.getData();

            mSelectImage.setImageURI(mUriImage );

        }
    }
    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                mDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

}
