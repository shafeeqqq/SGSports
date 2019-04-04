package com.example.shaf.sgsports;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int EDIT_PROFILE = 600 ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    public static final String LOGIN_PREFS = "LoginInfo";
    public static final String LOGGED_IN_FLAG = "isLoggedIn";
    private static final String USER_ACCT_ID = "emailAddress" ;
    SharedPreferences sharedPref;
    private FirebaseFirestore db;
    private String userId;

    private TextView nameTextView;
    private TextView aboutMeTextView;
    private Button editProfileButton;
    private CircleImageView profileIcon;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     */
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container, false);

        sharedPref = view.getContext().getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);
        Log.e("profile fragment", "user id:" + userId);

        db = FirebaseFirestore.getInstance();

        nameTextView = view.findViewById(R.id.profile_name);
        profileIcon = view.findViewById(R.id.profile_icon);
        aboutMeTextView = view.findViewById(R.id.profile_about_me_text);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditProfileActivity.class);
                intent.putExtra(USER_ACCT_ID, userId);
                startActivityForResult(intent, EDIT_PROFILE);
            }
        });

        db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {

                    User currentUser = documentSnapshot.toObject(User.class);
                    if (currentUser != null) {
                        nameTextView.setText(currentUser.getName());
                        String aboutMe = currentUser.getAboutMe();
                        if (aboutMe != null && !aboutMe.isEmpty())
                            aboutMeTextView.setText(aboutMe);
                        if (currentUser.getImageURL() != null)
                            Glide.with(getContext()).load(currentUser.getImageURL()).into(profileIcon);
                    }
                }
            }
        });
        return view;

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {

                    User currentUser = documentSnapshot.toObject(User.class);
                    if (currentUser != null) {
                        nameTextView.setText(currentUser.getName());
                        String aboutMe = currentUser.getAboutMe();
                        if (aboutMe != null && !aboutMe.isEmpty())
                            aboutMeTextView.setText(aboutMe);
                        if (currentUser.getImageURL() != null)
                            Glide.with(getContext()).load(currentUser.getImageURL()).into(profileIcon);
                    }
                }
            }
        });


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
