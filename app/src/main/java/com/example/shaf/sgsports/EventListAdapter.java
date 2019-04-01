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

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.Event;

import java.util.Arrays;
import java.util.List;

import static com.example.shaf.sgsports.ScheduleFragment.PAST;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {


    private Context mContext;

    private List<Event> mEvents;
    private List<String> mCategories;
    private List<String> iconUrls;
    private int flag = 0;


    public EventListAdapter(Context context, int flag) {
        mContext = context;
        mCategories = Arrays.asList(context.getResources().getStringArray(R.array.sports_categories));
        iconUrls = Arrays.asList(context.getResources().getStringArray(R.array.sports_icon_urls));
        this.flag = flag;
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

            String category = current.getSportsCategory();
            int index = mCategories.indexOf(category);
            String url = iconUrls.get(index);
            Glide.with(mContext).load(url).into(holder.iconView);
            holder.catTextView.setText("| " + category);

            holder.dateTextView.setText(current.dateCreatedText());
            holder.timeTextView.setText(current.timeText());
            holder.locationTextView.setText(current.getAddress());

            int vacancy = current.getVacancy();
            String numText = "";
            if (flag != PAST) {
                if (vacancy > 0)
                    numText = "Looking for " + current.getVacancy() + " more players";
                else
                    numText = "Event is Full!";
                holder.numTextView.setText(numText);

            } else
                holder.numTextView.setText("This is an archived event.");



        } else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public int getItemCount() {
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
        private TextView locationTextView;
        private ImageView iconView;

        public EventListViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.event_item_name_text);
            catTextView = itemView.findViewById(R.id.event_item_category_text);
            dateTextView = itemView.findViewById(R.id.event_item_date_text);
            timeTextView = itemView.findViewById(R.id.event_item_time_text);
            numTextView = itemView.findViewById(R.id.event_item_num_text);
            iconView = itemView.findViewById(R.id.event_item_image);
            locationTextView = itemView.findViewById(R.id.event_item_location_text);
        }
    }
}
