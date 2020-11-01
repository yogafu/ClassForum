package com.example.el_wi.classforum;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;

public class Drawer extends AppCompatActivity implements View.OnClickListener {


    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mTogle;
    private NavigationView navigationView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private RecyclerView mCl_list;
    private DatabaseReference mDatabase, myRef;

    private TextView mHomeLabel;
    private TextView mScheduleLabel;
    private TextView mProfileLabel, navNama;
    private CircleImageView circleImageView;

    private FloatingActionButton mAddButton;

    //example

    private ViewPager mMainPager;

    private PagerViewAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(Drawer.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        setContentView(R.layout.activity_drawer);

        mAddButton = (FloatingActionButton) findViewById( R.id.AddButton );
        mAddButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lSubmit = new Intent(Drawer.this, SubmitActivity.class);
                lSubmit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lSubmit);
            }
        } );

        myRef = FirebaseDatabase.getInstance().getReference().child( "user" );


        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mTogle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);

        mDrawerlayout.addDrawerListener(mTogle);
        mTogle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view) ;
        setupDrawerContent(navigationView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Fragments
        mHomeLabel = findViewById(R.id.HomeLabel);
        mScheduleLabel = findViewById(R.id.scheduleLabel);
        mProfileLabel = findViewById(R.id.profileLabel);

        mMainPager = findViewById(R.id.mainPager);

        mPageAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPageAdapter);

        mHomeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(0);
            }
        });

        mScheduleLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(1);
            }
        });
        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(2);
            }
        });

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                changeTabs(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.M)
    private void changeTabs(int i) {


        if(i == 0){
            mHomeLabel.setTextColor(getColor(R.color.textTabRight));
            mHomeLabel.setTextSize(20);

            mScheduleLabel.setTextColor(getColor(R.color.textTabLight));
            mScheduleLabel.setTextSize(16);

            mProfileLabel.setTextColor(getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(16);

            mAddButton.setVisibility( View.VISIBLE );

        }else if (i == 1){

            mHomeLabel.setTextColor(getColor(R.color.textTabLight));
            mHomeLabel.setTextSize(16);

            mScheduleLabel.setTextColor(getColor(R.color.textTabRight));
            mScheduleLabel.setTextSize(20);

            mProfileLabel.setTextColor(getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(16);
            mAddButton.setVisibility( View.VISIBLE );

        }else if (i == 2){

            mHomeLabel.setTextColor(getColor(R.color.textTabLight));
            mHomeLabel.setTextSize(16);

            mScheduleLabel.setTextColor(getColor(R.color.textTabLight));
            mScheduleLabel.setTextSize(16);

            mProfileLabel.setTextColor(getColor(R.color.textTabRight));
            mProfileLabel.setTextSize(20);
            mAddButton.setVisibility( View.INVISIBLE );
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mTogle.onOptionsItemSelected(item)){

            if(item.getItemId() == R.id.logout){
                logout();
                mAuth.signOut();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);

                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;

        switch (menuItem.getItemId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.tambah:
                Intent lSubmit = new Intent(Drawer.this, SubmitActivity.class);
                lSubmit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lSubmit);
                break;
            case R.id.pesan:
                Intent SSubmit = new Intent(Drawer.this, LookActivity.class);
                SSubmit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(SSubmit);
                break;
            case R.id.about:
                Intent intentAbot = new Intent(Drawer.this, AboutActivity.class);
                intentAbot.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAbot);
                break;
        }

        menuItem.setChecked(true);
        mDrawerlayout.closeDrawers();
    }


    @Override
    public void onClick(View v) {


    }

    private void logout() {
        mAuth.signOut();
    }


}
