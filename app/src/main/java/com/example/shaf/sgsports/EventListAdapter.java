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

import java.text.SimpleDateFormat;
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
        if (mEvents != null) {
            Event current = mEvents.get(position);

            holder.nameTextView.setText(current.getName());
            holder.catTextView.setText("| " + current.getSportsCategory());

            holder.dateTextView.setText(current.dateCreatedText());
            holder.timeTextView.setText(current.timeText());

            String numText = "Looking for " + current.getVacancy() + " more players";
            holder.numTextView.setText(numText);


        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }
//        if (mCategories != null) {
//            String current = mCategories[position];
//            holder.textView.setText(current);
//        }
    }


    @Override
    public int getItemCount() {
        // TODO: change this to event later
        if (mEvents != null)
            return mEvents.size();
        return 0;
    }


    public void setEvents(List<Event> events){
        mEvents = events;
        notifyDataSetChanged();

    }

    public String getEventID(int position) {
        return mEvents.get(position).getEventID();
    }


    public class EventListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView catTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView numTextView;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.event_item_name_text);
            catTextView = itemView.findViewById(R.id.event_item_category_text);
            dateTextView = itemView.findViewById(R.id.event_item_date_text);
            timeTextView = itemView.findViewById(R.id.event_item_time_text);
            numTextView = itemView.findViewById(R.id.event_item_num_text);
        }
    }
}
