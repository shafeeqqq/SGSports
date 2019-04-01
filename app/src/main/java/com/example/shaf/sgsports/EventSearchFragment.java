package com.example.shaf.sgsports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Utils.RecyclerItemCustomListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;


public class EventSearchFragment extends Fragment {

    private static final String TAG = "EVENT_FRAGMENT";
    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();
    FirebaseFirestore db;
    SharedPreferences sharedPref;

    public static final String EVENT_ID = "eventID";

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;

    public EventSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        sharedPref = getActivity().getSharedPreferences(LOGIN_PREFS, getActivity().MODE_PRIVATE);
        final String userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        db = FirebaseFirestore.getInstance();
        eventListAdapter = new EventListAdapter(view.getContext(),0);

        db.collection("events").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (snapshots.isEmpty())
                        eventArrayList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {

                            Event event = doc.toObject(Event.class);
                            if (!event.getOrganiser().equals(userId))
                                eventArrayList.add(event);
                        }
                        Log.e(TAG, eventArrayList.toString());
                        eventListAdapter.setEvents(eventArrayList);
                    }
                });

        recyclerView = view.findViewById(R.id.eventsearch_recycler_view);
        recyclerView.setAdapter(eventListAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemCustomListener(view.getContext(),
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

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
