package com.example.shaf.sgsports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.EventSearchFragment.EVENT_ID;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;
import static com.example.shaf.sgsports.ScheduleFragment.EVENT_TYPE;
import static com.example.shaf.sgsports.ScheduleFragment.PAST;
import static com.example.shaf.sgsports.ScheduleFragment.TIME;
import static com.example.shaf.sgsports.ScheduleFragment.UPCOMING;

public class ScheduleDetailActivity extends AppCompatActivity {

    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();
    SharedPreferences sharedPref;
    FirebaseFirestore db;

    public static final String TAG = ScheduleDetailActivity.class.getSimpleName();

    private EventSearchFragment.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;

    private TextView emptyMsgText;

    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        final String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        db = FirebaseFirestore.getInstance();

        emptyMsgText = findViewById(R.id.schedule_detail_empty_text);
        emptyMsgText.setVisibility(View.GONE);


        final String field = getIntent().getStringExtra(EVENT_TYPE);
        flag = getIntent().getIntExtra(TIME, 0);
        eventListAdapter = new EventListAdapter(this, flag);

        db.collection("userInEvents").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                ArrayList<String> f = (ArrayList<String>) documentSnapshot.get(field);

                if (f == null){
                    Log.e(TAG, field + " empty " + userId);
                    return;
                }
                if (f.isEmpty())
                    showEmptyMessage(true);

                else {
                    showEmptyMessage(false);
                    loadEvents(f);
                }

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


    private void loadEvents(final ArrayList<String> list) {
        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }
                if (snapshots.isEmpty())
                    Log.e(TAG, "No data");

                eventArrayList.clear();
                for (QueryDocumentSnapshot doc : snapshots) {
                    Event event = doc.toObject(Event.class);

                    if (list.contains(event.getEventID())) {
                        if (flag == UPCOMING && !event.isPast())
                            eventArrayList.add(event);

                        else if (flag == PAST && event.isPast())
                            eventArrayList.add(event);
                    }
                }

                if (eventArrayList.isEmpty())
                    showEmptyMessage(true);

                else {
                    showEmptyMessage(false);
                }
                Log.e(TAG, "num requested events: " + eventArrayList.size());
                eventListAdapter.setEvents(eventArrayList);
            }
        });
    }

    private void showEmptyMessage(boolean show) {
        if (show)
            emptyMsgText.setVisibility(View.VISIBLE);
        else
            emptyMsgText.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

}
