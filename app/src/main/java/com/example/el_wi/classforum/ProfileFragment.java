package com.example.el_wi.classforum;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView circleImageView;
    private TextView mProfileName;

    private FirebaseAuth mAuth;

    private Button mButtonLogOut;
    private String mUserId;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        mUserId = mAuth.getCurrentUser().getUid();

        //Firebase
        mDatabase = FirebaseDatabase.getInstance();

        circleImageView = (CircleImageView) view.findViewById(R.id.imageProfile);
        mProfileName = (TextView) view.findViewById(R.id.namaUSer);
        mButtonLogOut = (Button) view.findViewById(R.id.btnLogout);

        mReference = mDatabase.getInstance().getReference().child("user").child(mUserId);



        // Read from the database
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = dataSnapshot.child("name").getValue().toString();
                String uriImage = dataSnapshot.child( "image" ).getValue().toString();
                mProfileName.setText( name );
                Picasso.get().load( uriImage ).into( circleImageView );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        return view;
    }

}
