package com.example.shaf.sgsports;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements
        EventSearchFragment.OnFragmentInteractionListener, ConnectFragment.OnFragmentInteractionListener,
        FacilitiesFragment.OnFragmentInteractionListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    BottomNavigationView mBottomNavigationView;

    private final int CREATE_EVENT_CODE = 900;

    final Fragment eventFragment = new EventSearchFragment();
    final Fragment connectFragment = new ConnectFragment();
    final Fragment scheduleFragment = new ScheduleFragment();
    final Fragment facilitiesFragment = new FacilitiesFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active_fragment = eventFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    fm.beginTransaction().hide(active_fragment)
                            .show(eventFragment)
                            .commit();
                    active_fragment = eventFragment;
                    return true;

                case R.id.navigation_facilities:
                    fm.beginTransaction().hide(active_fragment).show(facilitiesFragment).commit();
                    active_fragment = facilitiesFragment;
                    return true;

                case R.id.navigation_create_event:
                    Intent intent = new Intent(HomeActivity.this, CreateEventActivity.class);
                    startActivityForResult(intent, CREATE_EVENT_CODE);
                    return true;

                case R.id.navigation_schedule:
                    fm.beginTransaction().hide(active_fragment).show(scheduleFragment).commit();
                    active_fragment = scheduleFragment;
                    return true;

                case R.id.navigation_connect:
                    fm.beginTransaction().hide(active_fragment).show(connectFragment).commit();
                    active_fragment = connectFragment;
                    return true;
            }
            return false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // Not signed in, launch the Sign In activity
                    startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), 900);
                    mFirebaseUser = mFirebaseAuth.getCurrentUser();
                }
            }
        };

//        if (mFirebaseUser == null) {
//            // Not signed in, launch the Sign In activity
//            startActivityForResult(new Intent(this, LoginActivity.class), 900);
//            mFirebaseUser = mFirebaseAuth.getCurrentUser();
//            if (mFirebaseUser == null)
//                Log.e("Firebase User", "user not found");
//        }

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fixBottomNavigation();

        fm.beginTransaction().add(R.id.home_container, eventFragment).commit();
        fm.beginTransaction().add(R.id.home_container, connectFragment).hide(connectFragment).commit();
        fm.beginTransaction().add(R.id.home_container, scheduleFragment).hide(scheduleFragment).commit();
        fm.beginTransaction().add(R.id.home_container, facilitiesFragment).hide(facilitiesFragment).commit();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT_CODE) {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_schedule);

        }
    }

    /**
    * helper method to disable icon magnification
    **/
    private void fixBottomNavigation() {
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.notifications) {
//            Intent intent = new Intent(this, CategoryListActivity.class);
//            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
