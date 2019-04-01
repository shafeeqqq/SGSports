package com.example.shaf.sgsports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaf.sgsports.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileActivity extends AppCompatActivity {

    public static final String LOGIN_PREFS = "LoginInfo";
    public static final String LOGGED_IN_FLAG = "isLoggedIn";
    private static final String USER_ACCT_ID = "emailAddress" ;

    private FirebaseFirestore db;
    private String userId;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setElevation(0);

        userId = getIntent().getStringExtra(USER_ACCT_ID);
        db = FirebaseFirestore.getInstance();

        Button editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setVisibility(View.GONE);
        nameTextView = findViewById(R.id.profile_name);

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

    private void updateUI(User user) {
        nameTextView.setText(user.getName());
    }

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

}
