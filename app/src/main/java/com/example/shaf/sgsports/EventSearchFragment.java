package com.example.shaf.sgsports;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Arrays;
import java.util.List;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.HomeActivity.LOGIN_PREFS;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;


public class EventSearchFragment extends Fragment {

    private static final String TAG = "EVENT_FRAGMENT";
    private static ArrayList<Event> eventArrayList = new ArrayList<Event>();
    FirebaseFirestore db;
    ArrayList<Integer> selectedItems = new ArrayList<>();
    private boolean check = false;

    SharedPreferences sharedPref;
    final boolean[] checkedItems = new boolean[10];


    MenuItem searchItem;
    MenuItem filterItem;

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
        setHasOptionsMenu(true);
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

                        eventArrayList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {

                            Event event = doc.toObject(Event.class);
                            if (!event.getOrganiser().equals(userId) && !event.isPast())
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.event_filter)
            openCategoryDialog();

        return super.onOptionsItemSelected(item);
    }

    public void openCategoryDialog() {

        for (int i = 0; i < 9; i++) {
            if (selectedItems != null && selectedItems.contains(i))
                checkedItems[i] = true;
            else
                checkedItems[i] = false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                filterCategory();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.event_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        filterItem = menu.findItem(R.id.event_filter);
        searchItem = menu.findItem(R.id.event_search_badge);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchFilter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchFilter(s.toLowerCase());
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

    }

    private void filterCategory() {
        ArrayList<Event> arr = new ArrayList<>();
        List<String> categoryList = Arrays.asList(getResources().getStringArray(R.array.sports_categories));
        if (selectedItems.isEmpty()){
            eventListAdapter.setEvents(eventArrayList);
            return;
        }

        for (Event event: eventArrayList) {
            int index = categoryList.indexOf(event.getSportsCategory());
            if (selectedItems.contains(index))
                arr.add(event);
        }

        eventListAdapter.setEvents(arr);
    }

    private void searchFilter(String s) {
        Log.e(TAG, "search filter enter" + s);
        ArrayList<Event> arr = new ArrayList<>();

        if (s.isEmpty())
            eventListAdapter.setEvents(eventArrayList);

        Log.e(TAG, eventArrayList.size() + "<- main array");

        for (Event event: eventArrayList) {
            Log.e(TAG, event.getName() + s);
            if (event.getName().toLowerCase().contains(s))
                arr.add(event);
        }

        eventListAdapter.setEvents(arr);
    }

}
