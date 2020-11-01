package com.example.el_wi.classforum;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private View Homeview;

    private RecyclerView mCl_list;
    private DatabaseReference mDatabase, myRef;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Homeview = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ClassForum");

        mCl_list = (RecyclerView) Homeview.findViewById(R.id.post_list);
        mCl_list.setHasFixedSize(true);
        mCl_list.setLayoutManager(new LinearLayoutManager( getContext() ));

   //pengembalian nilai
        return Homeview;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child( "Task" );
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<ClassForum>()
                .setQuery(query, ClassForum.class  )
                .build();

        FirebaseRecyclerAdapter<ClassForum, ClassForumViewHolder> adapter
                = new FirebaseRecyclerAdapter<ClassForum, ClassForumViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ClassForumViewHolder holder, int position, @NonNull ClassForum model) {


                myRef = FirebaseDatabase.getInstance ().getReference ().child("user").child ( model.getUid () );
                // Read from the database
                myRef.addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.child("name").getValue().toString();
                        holder.setName ( value );
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });

                holder.setMatkul(  model.getTitle() );
                holder.setDesc( model.getDesc());
                holder.setImage( model.getImage() );
                holder.setDaedline ( model.getDeadline () );
            }

            @NonNull
            @Override
            public ClassForumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from( viewGroup.getContext()).inflate(R.layout.cf_row,viewGroup,false  );
                ClassForumViewHolder viewHolder = new ClassForumViewHolder( view ) ;
                return viewHolder;
            }
        };
        mCl_list.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ClassForumViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView matkul;
        TextView desc ;
        ImageView imagePost;

        public ClassForumViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }
        public void setName(String name){
            TextView namePost = (TextView) mView.findViewById ( R.id.postName );
            namePost.setText ( name );
        }

        public void setMatkul(String title){

            TextView matkul = (TextView) mView.findViewById(R.id.postMatkul);
            matkul.setText(title);
        }

        public void setDesc(String title){

            TextView desc = (TextView) mView.findViewById(R.id.postDesc);
            desc.setText(title);
        }

        public void setImage(String title){

            ImageView imagePost = (ImageView) mView.findViewById(R.id.postImage);

            Picasso.get().load(title).into(imagePost);
        }

        public void setDaedline(String string){
            TextView deadline =  (TextView )mView.findViewById ( R.id.postDate );
            deadline.setText ( string );

        }


    }


}




