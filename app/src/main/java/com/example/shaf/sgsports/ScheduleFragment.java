package com.example.shaf.sgsports;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaf.sgsports.Model.Event;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {

    private static final String CREATED = "created" ;
    private static final String JOINED = "joined" ;

    public static final int PAST = -1;
    public static final int UPCOMING = 1 ;

    public static final String EVENT_TYPE = "event_type";
    public static final String TIME = "time" ;


    private RecyclerView recyclerView;
    private EventListAdapter upcomingEventListAdapter;
    private static ArrayList<Event> upcomingEventArrayList = new ArrayList<Event>();

    SharedPreferences sharedPref;

    CardView up_joined;
    CardView up_created;

    CardView past_joined;
    CardView past_created;

    public ScheduleFragment() {
        // Required empty public constructor
    }

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

        up_joined = view.findViewById(R.id.upcoming_joined);
        up_created = view.findViewById(R.id.upcoming_created);
        past_created = view.findViewById(R.id.past_created);
        past_joined = view.findViewById(R.id.past_joined);

        up_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(JOINED, UPCOMING);
            }
        });

        up_created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { launchActivity(CREATED, UPCOMING);
            }
        });

        past_joined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(JOINED, PAST);
            }
        });

        past_created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(CREATED, PAST);
            }
        });

        return view;
    }


    private void launchActivity(String type, int time) {
        Intent intent = new Intent(getContext(), ScheduleDetailActivity.class);

        intent.putExtra(EVENT_TYPE, type);
        intent.putExtra(TIME, time);
        startActivity(intent);
    }
}
