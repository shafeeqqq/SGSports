package com.example.shaf.sgsports;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.BottomNavigationViewHelper;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.provider.CalendarContract.Instances.EVENT_ID;
import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class HomeActivity extends AppCompatActivity implements
        EventSearchFragment.OnFragmentInteractionListener, ConnectFragment.OnFragmentInteractionListener,
        FacilitiesFragment.OnFragmentInteractionListener {

    private static final String TAG = "HomeActivity";

    private boolean check = false;

    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();


    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    MenuItem searchItem;
    MenuItem filterItem;
    ArrayList<Integer> selectedItems;

    public static final String LOGIN_PREFS = "LoginInfo";
    public static final String LOGGED_IN_FLAG = "isLoggedIn";
    SharedPreferences sharedPref;
    Boolean isLoggedIn;

    BottomNavigationView mBottomNavigationView;

    private final int CREATE_EVENT_CODE = 900;
    private final int LOGIN_INTENT_CODE = 904;

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
                    selectedItems = new ArrayList<>();
                    fm.beginTransaction().hide(active_fragment)
                            .show(eventFragment)
                            .commit();
                    active_fragment = eventFragment;

                    if (searchItem != null)
                        searchItem.setVisible(true);

                    if (filterItem != null)
                        filterItem.setVisible(true);

                    return true;

                case R.id.navigation_facilities:
                    fm.beginTransaction().hide(active_fragment).show(facilitiesFragment).commit();
                    active_fragment = facilitiesFragment;

                    if (searchItem != null)
                        searchItem.setVisible(false);

                    if (filterItem != null)
                        filterItem.setVisible(false);
                    return true;

                case R.id.navigation_create_event:
                    Intent intent = new Intent(HomeActivity.this, CreateEventActivity.class);
                    startActivityForResult(intent, CREATE_EVENT_CODE);

                    if (searchItem != null)
                        searchItem.setVisible(false);

                    if (filterItem != null)
                        filterItem.setVisible(false);
                    return true;

                case R.id.navigation_schedule:
                    fm.beginTransaction().hide(active_fragment).show(scheduleFragment).commit();
                    active_fragment = scheduleFragment;

                    if (searchItem != null)
                        searchItem.setVisible(false);

                    if (filterItem != null)
                        filterItem.setVisible(false);
                    return true;

                case R.id.navigation_connect:
                    fm.beginTransaction().hide(active_fragment).show(connectFragment).commit();
                    active_fragment = connectFragment;

                    if (searchItem != null)
                        searchItem.setVisible(false);
                    if (filterItem != null)
                        filterItem.setVisible(false);

                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        selectedItems = new ArrayList<>();

        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        isLoggedIn = sharedPref.getBoolean(LOGGED_IN_FLAG, false);

        String log_msg = "ONCREATE: isLoggedIn = " + String.valueOf(isLoggedIn);
        Log.i(TAG, log_msg);

//
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null) {
//                    // Not signed in, launch the Sign In activity
//                    startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), LOGIN_INTENT_CODE);
//                }
//            }
//        };

//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

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
        } else if (requestCode == LOGIN_INTENT_CODE) {
            isLoggedIn = sharedPref.getBoolean(LOGGED_IN_FLAG, false);

            String log_msg = "ONRESULT: isLoggedIn = " + String.valueOf(isLoggedIn);
            Log.i(TAG, log_msg);
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

        filterItem = menu.findItem(R.id.filter);
        searchItem = menu.findItem(R.id.search_badge);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                updateList
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        // Define the listener
        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.getQuery();
                return true;  // Return true to expand action view
            }
        };

        searchItem.setOnActionExpandListener(expandListener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.my_requests) {
            Intent intent = new Intent(this, MyRequestsActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.filter) {
            openCategoryDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openCategoryDialog() {

        final boolean[] checkedItems = new boolean[10];
        for (int i = 0; i < 9; i++) {
            if (selectedItems != null && selectedItems.contains(i))
                checkedItems[i] = true;
            else
                checkedItems[i] = false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMultiChoiceItems(R.array.sports_categories, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!check)
                    check = true;
                else
                    check = false;
                filterEvents();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterEvents() {
        eventListAdapter = new EventListAdapter(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("events");

        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        final String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String[] categories = getResources().getStringArray(R.array.sports_categories);
                eventArrayList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event event = ds.getValue(Event.class);
                    if (!event.getOrganiser().equals(userId)) {
                        if (!selectedItems.isEmpty()) {
                            for (int i : selectedItems) {
                                if (event.getSportsCategory().equals(categories[i]))
                                    eventArrayList.add(event);
                            }
                        } else
                            eventArrayList.add(event);

                    }
                }

                eventListAdapter.setEvents(eventArrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        recyclerView = findViewById(R.id.eventsearch_recycler_view);
        recyclerView.setAdapter(eventListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(this,
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
                intent.putExtra(EVENT_ID, eventListAdapter.getEventID(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

    }
}
