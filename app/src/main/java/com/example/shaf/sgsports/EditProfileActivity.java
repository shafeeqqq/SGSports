package com.example.shaf.sgsports;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaf.sgsports.Model.Event;
import com.example.shaf.sgsports.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.example.shaf.sgsports.CreateEventDetailsFragment.LOGIN_PREFS;
import static com.example.shaf.sgsports.CreateEventDetailsFragment.UNKNOWN;
import static com.example.shaf.sgsports.LoginActivity.USER_ACCT_ID;

public class EditProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    User mUser;
    DatabaseReference ref = null;
    String userId;

    ArrayList<Integer> selectedItems;
    final int[] checkedItem = {0};


    private ValueEventListener mUserDetailsListener;


    EditText nameEditText;
    EditText aboutMeEditText;
    TextView genderText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        selectedItems = new ArrayList<>();

        getSupportActionBar().setElevation(0);

        nameEditText = findViewById(R.id.edit_profile_name);
        aboutMeEditText = findViewById(R.id.edit_profile_about_me);
        genderText = findViewById(R.id.edit_profile_gender_text);

        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });

        getUser();

    }

    private void showGenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(R.array.gender, checkedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem[0] = which;
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               //
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
    public void onStart() {
        super.onStart();

        ValueEventListener eventDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                mUser = dataSnapshot.getValue(User.class);
                if (mUser != null) {
                    nameEditText.setText(mUser.getName());

                    if (mUser.getAboutMe() != null)
                      aboutMeEditText.setText(mUser.getAboutMe());
                    else
                        aboutMeEditText.setHint("Edit About Me");

                    if (mUser.getGender() != null)
                        genderText.setText(mUser.getGender());
                    else
                        genderText.setText("Unknown");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                Toast.makeText(EditProfileActivity.this, "Failed to load event.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        ref.addValueEventListener(eventDetailsListener);
        // Keep copy of post listener so we can remove it when app stops
        mUserDetailsListener = eventDetailsListener;
    }


    private void getUser() {
        sharedPref = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        userId = sharedPref.getString(USER_ACCT_ID, UNKNOWN);

        String name = nameEditText.getText().toString();
        String aboutMe = aboutMeEditText.getText().toString();
        String gender = genderText.getText().toString();

        ref = FirebaseDatabase.getInstance().getReference("users").child(userId);

    }

    private void updateUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(userId).setValue(mUser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.update_profile) {
            updateUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
