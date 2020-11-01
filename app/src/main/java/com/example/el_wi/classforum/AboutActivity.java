package com.example.el_wi.classforum;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mTogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about );
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("My new title"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide(); // or even hide the actionbar

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Membaca file menu dan menambahkan isinya ke action bar jika ada.
        getMenuInflater().inflate(R.menu.drawermenu, menu);
        return true;
    }
}
