package com.example.shaf.sgsports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shaf.sgsports.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    public static final String LOGIN_PREFS = "LoginInfo";
    public static final String LOGGED_IN_FLAG = "isLoggedIn";
    private static final String USER_ACCT_ID = "emailAddress" ;

    private FirebaseFirestore db;
    private String userId;
    private TextView nameTextView;
    private TextView aboutMeTextView;
    private CircleImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setElevation(0);

        LinearLayoutCompat con = findViewById(R.id.profile_view_container);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) con.getLayoutParams();
        params.topMargin = 0;
//        con.setLayoutParams(params);
        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra(USER_ACCT_ID);

        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setVisibility(View.GONE);
        nameTextView = findViewById(R.id.profile_name);
        profileIcon = findViewById(R.id.profile_icon);
        aboutMeTextView = findViewById(R.id.profile_about_me_text);

        setTitle("");

        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User currentUser = documentSnapshot.toObject(User.class);
                        assert currentUser != null;
                        updateUI(currentUser);

                    }
                });
    }

    private void updateUI(User currentUser) {
        nameTextView.setText(currentUser.getName());
        String aboutMe = currentUser.getAboutMe();
        if (aboutMe != null && !aboutMe.isEmpty())
            aboutMeTextView.setText(aboutMe);
        if (currentUser.getImageURL() != null)
            Glide.with(this).load(currentUser.getImageURL()).into(profileIcon);    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.log_out) {
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putBoolean(LOGGED_IN_FLAG, false);
//            editor.apply();
//
//            FirebaseAuth.getInstance().signOut();
//            finish();
//            return true;
//        }
//
//        else if (id == item.getItemId()) {
//            onBackPressed();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

}
