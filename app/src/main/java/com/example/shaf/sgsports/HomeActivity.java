package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements
        EventSearchFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        FacilitiesFragment.OnFragmentInteractionListener {

    private static final String TAG = "HomeActivity";

    private boolean check = false;

    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();

    private FirebaseAuth mFirebaseAuth;

    MenuItem searchItem;
    MenuItem filterItem;
    ArrayList<Integer> selectedItems = new ArrayList<>();

    public static final String LOGIN_PREFS = "LoginInfo";
    public static final String LOGGED_IN_FLAG = "isLoggedIn";
    SharedPreferences sharedPref;
    Boolean isLoggedIn;

    BottomNavigationView mBottomNavigationView;

    private final int CREATE_EVENT_CODE = 900;
    private final int LOGIN_INTENT_CODE = 904;

    final Fragment eventFragment = new EventSearchFragment();
    final Fragment connectFragment = new ProfileFragment();
    final Fragment scheduleFragment = new ScheduleFragment();
    final Fragment facilitiesFragment = new FacilitiesFragment();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment active_fragment = eventFragment;

    private FirebaseFirestore db;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    selectedItems = new ArrayList<>();
                    fm.beginTransaction().hide(active_fragment)
                            .show(eventFragment)
                            .commit();
                    active_fragment = eventFragment;
                    getSupportActionBar().setTitle(R.string.app_name);

                    return true;

                case R.id.navigation_facilities:
                    fm.beginTransaction().hide(active_fragment).show(facilitiesFragment).commit();
                    active_fragment = facilitiesFragment;
                    getSupportActionBar().setTitle(R.string.app_name);

                    return true;

                case R.id.navigation_create_event:
                    Intent intent = new Intent(HomeActivity.this, CreateEventActivity.class);
                    startActivityForResult(intent, CREATE_EVENT_CODE);
                    getSupportActionBar().setTitle(R.string.app_name);

                    return true;

                case R.id.navigation_schedule:
                    fm.beginTransaction().hide(active_fragment).show(scheduleFragment).commit();
                    active_fragment = scheduleFragment;
                    getSupportActionBar().setTitle(R.string.app_name);

                    return true;

                case R.id.navigation_profile:
                    fm.beginTransaction().hide(active_fragment).show(connectFragment).commit();
                    active_fragment = connectFragment;
                    getSupportActionBar().setTitle("");

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        eventListAdapter = new EventListAdapter(this,0);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }

        db = FirebaseFirestore.getInstance();
        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        isLoggedIn = sharedPref.getBoolean(LOGGED_IN_FLAG, false);

        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fixBottomNavigation();

        fm.beginTransaction().add(R.id.home_container, eventFragment).commit();
        fm.beginTransaction().add(R.id.home_container, connectFragment).hide(connectFragment).commit();
        fm.beginTransaction().add(R.id.home_container, scheduleFragment).hide(scheduleFragment).commit();
        fm.beginTransaction().add(R.id.home_container, facilitiesFragment).hide(facilitiesFragment).commit();

//        ArrayList<Facility> data = FacilitiesData.run(getResources().openRawResource(R.raw.result));
//
//        for (Facility f: data) {
//            db.collection("facility").document(f.getFacilityID()).set(f);
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_EVENT_CODE) {
            mBottomNavigationView.setSelectedItemId(R.id.navigation_schedule);

        } else if (requestCode == LOGIN_INTENT_CODE) {
            isLoggedIn = sharedPref.getBoolean(LOGGED_IN_FLAG, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        isLoggedIn = sharedPref.getBoolean(LOGGED_IN_FLAG, false);
        if (!isLoggedIn) {
            // Not signed in, launch the Sign In activity
            startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_INTENT_CODE);
        }

    }

    /**
     * helper method to disable icon magnification
     **/
    private void fixBottomNavigation() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.my_requests) {
            Intent intent = new Intent(this, MyRequestsActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.log_out) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(LOGGED_IN_FLAG, false);
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            fm.beginTransaction().remove(active_fragment).commit();
            recreate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
