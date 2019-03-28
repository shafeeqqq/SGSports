package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.EventSearchFragment.EVENT_ID;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class ScheduleDetailActivity extends AppCompatActivity {

    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();
    SharedPreferences sharedPref;

    private EventSearchFragment.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

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
                eventArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event event = ds.getValue(Event.class);
                    if (!event.getOrganiser().equals(userId))
                        eventArrayList.add(event);
                }
                eventListAdapter.setEvents(eventArrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        recyclerView = findViewById(R.id.schedule_detail_recycler);
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
