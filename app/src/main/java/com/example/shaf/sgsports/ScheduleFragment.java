package com.example.shaf.sgsports;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaf.sgsports.Model.Event;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventListAdapter upcomingEventListAdapter;
    private static ArrayList<Event> upcomingEventArrayList = new ArrayList<Event>();

    SharedPreferences sharedPref;


    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_schedule, container, false);

        sharedPref = getActivity().getSharedPreferences(LOGIN_PREFS, getActivity().MODE_PRIVATE);
        final String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        upcomingEventListAdapter = new EventListAdapter(view.getContext());

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("events");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                upcomingEventArrayList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Event event = ds.getValue(Event.class);
                    if (event.getOrganiser().equals(userId))
                        upcomingEventArrayList.add(event);

                }
                upcomingEventListAdapter.setEvents(upcomingEventArrayList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        recyclerView = view.findViewById(R.id.schedule_upcoming_recycler_view);
        recyclerView.setAdapter(upcomingEventListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(view.getContext(),
                recyclerView, new RecyclerItemCustomListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
                intent.putExtra(EVENT_ID, upcomingEventListAdapter.getEventID(position));
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        return view;
    }

}
