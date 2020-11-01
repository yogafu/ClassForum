package com.example.el_wi.classforum;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class LookActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private RecyclerView mCl_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_look );

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ClassForum");

        mCl_list = (RecyclerView) findViewById(R.id.CFL_list);
        mCl_list.setHasFixedSize(true);
        mCl_list.setLayoutManager(new LinearLayoutManager( this ));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child( "ClassForum" );
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<ClassForum>()
                        .setQuery(query, ClassForum.class  )
                        .build();

        FirebaseRecyclerAdapter<ClassForum, ClassForumViewHolder> adapter
                = new FirebaseRecyclerAdapter<ClassForum, ClassForumViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ClassForumViewHolder holder, int position, @NonNull ClassForum model) {


                holder.setMatkul(  model.getTitle() );
                holder.setDesc( model.getDesc());
                holder.setImage( model.getImage() );
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


    }
}
