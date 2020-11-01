package com.example.el_wi.classforum;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<User> users = new ArrayList<>();

    public interface DataStatus{
        void DataLoaded(List<User> users, List<String> keys);
        void DataInserted();
        void DataUpdated();
        void DataDeleted();
    }

    public FirebaseDatabaseHelper(){

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("user");
    }

    public void readUser(final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode: dataSnapshot.getChildren()){

                    keys.add(keyNode.getKey());
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                dataStatus.DataLoaded(users, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
