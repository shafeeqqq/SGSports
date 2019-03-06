package com.example.shaf.sgsports;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {


    private Context mContext;

    private List<Event> mEvents;
    private String[] mCategories;


    public EventListAdapter(Context context) {
        mContext = context;
        mCategories = context.getResources().getStringArray(R.array.sports_categories);

    }


    @NonNull
    @Override
    public EventListAdapter.EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_event, parent, false);


        return new EventListAdapter.EventListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.EventListViewHolder holder, int position) {
//        if (mEvents != null) {
//            Event current = mEvents.get(position);
//
//            // TODO: event object binding
//
//        }
//
//        else {
//            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
//        }
        if (mCategories != null) {
            String current = mCategories[position];
            holder.textView.setText(current);
        }
    }


    @Override
    public int getItemCount() {
        // TODO: change this to event later
        if (mCategories != null)
            return mCategories.length;
        return 0;
    }

    public class EventListViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.event_item_category_text);

        }
    }
}
