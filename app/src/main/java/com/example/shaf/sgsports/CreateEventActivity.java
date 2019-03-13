package com.example.shaf.sgsports;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CreateEventActivity extends AppCompatActivity implements CategoryListFragment.OnFragmentInteractionListener {



    final FragmentManager fm = getSupportFragmentManager();

    final Fragment categoryListFragment = new CategoryListFragment();
    final Fragment createEventDetailsFragment = new CreateEventDetailsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        fm.beginTransaction().add(R.id.home_container, categoryListFragment).commit();
//        fm.beginTransaction().add(R.id.home_container, connectFragment).hide(connectFragment).commit();


    }

    @Override
    public void onCategorySelected(String category) {
        Bundle args = new Bundle();
        args.putString(CreateEventDetailsFragment.ARG_CATEGORY, category);
        createEventDetailsFragment.setArguments(args);
        fm.beginTransaction()
                .replace(R.id.home_container, createEventDetailsFragment)
                .addToBackStack(null)
                .commit();

    }
}
