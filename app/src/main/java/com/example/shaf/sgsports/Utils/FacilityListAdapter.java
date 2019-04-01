package com.example.shaf.sgsports.Utils;

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
import com.example.shaf.sgsports.Model.Facility;
import com.example.shaf.sgsports.R;

import java.util.ArrayList;
import java.util.List;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.FacilityListViewHolder> {


    private Context mContext;

    private List<Facility> mFacilities;
    private String[] mCategories;


    public FacilityListAdapter(Context context) {
        mContext = context;
        mCategories = context.getResources().getStringArray(R.array.sports_categories);

    }


    @NonNull
    @Override
    public FacilityListAdapter.FacilityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_facilities, parent, false);


        return new FacilityListAdapter.FacilityListViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull FacilityListAdapter.FacilityListViewHolder holder, int position) {
        if (mFacilities != null) {
            Facility current = mFacilities.get(position);

            holder.addressTextView.setText(current.getAddressText());
            holder.nameTextView.setText(current.getName());
            Glide.with(mContext).load(current.getImageUrl()).into(holder.imageView);
        }

        else {
            Toast.makeText(mContext, "Error displaying data", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public int getItemCount() {
        if (mFacilities != null)
            return mFacilities.size();
        return 0;
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        mFacilities = facilities;
        notifyDataSetChanged();
    }

    public String getFacilityID(int position) {
        return mFacilities.get(position).getFacilityID();
    }

    public String getFacilityName(int position) {
        return mFacilities.get(position).getName();
    }

    public class FacilityListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView addressTextView;
        private ImageView imageView;

        public FacilityListViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.facilities_name_text);
            addressTextView = itemView.findViewById(R.id.facilities_address_text);
            imageView = itemView.findViewById(R.id.facilities_image_view);

        }
    }
}
