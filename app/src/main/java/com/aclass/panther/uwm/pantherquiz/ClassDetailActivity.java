package com.aclass.panther.uwm.pantherquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asmamaw on 10/26/16.
 */

public class ClassDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    public static final String ANONYMOUS = "anonymous";


    private String mUsername;
    private String mPhotoUrl;

    private GoogleApiClient mGoogleApiClient;

    private Button submitButton;
    private String TAG = "PantherQuiz QuizDetail  Activity log ";

    private String isAvailable = "";
    private String challenge = "";
    private String startDate, startTime, endDate, endTime, startTimePeriod, endTimePeriod;
    private String quizNumber, quizName;

    private TextView viewClassId, viewQuizId, viewDateFrom, viewDateTo, viewTimeFrom, viewTimeto;
    private TextView viewIsAvailable, viewIsTaken;
    private EditText editTextChallenge;
    private Button buttonStartQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_quiz_detail);

        viewClassId = (TextView) findViewById(R.id.textViewClassId);
        viewQuizId = (TextView) findViewById(R.id.textViewQuizId);
        viewTimeFrom = (TextView) findViewById(R.id.textViewTimeFrom);
        viewTimeto = (TextView) findViewById(R.id.textViewTimeTo);
        viewIsAvailable = (TextView) findViewById(R.id.textViewIsAvailable);
        viewIsTaken = (TextView) findViewById(R.id.textViewIsTaken);

        editTextChallenge = (EditText) findViewById(R.id.editTextChallenge);
        buttonStartQuiz = (Button) findViewById(R.id.buttonStartQuiz);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users/jFWwvImyV1Ului5UV2dGy4sdmdB2/classRooms/-KVQyXok2xJvO-JuqWpr/" +
                "quizzes/-KVQydF7CQWGg0QZH1v8/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                Log.i("CHALLENGE: ", dataSnapshot.child("challenge").getValue().toString());
                Log.i("END_TIME:", dataSnapshot.child("endTime").getValue().toString());
                Log.i("START_TIME:", dataSnapshot.child("startTime").getValue().toString());
                Log.i("QUIZ_ID:", dataSnapshot.child("quizId").getValue().toString());
                Log.i("IS_EXPIRED:", dataSnapshot.child("isExpired").getValue().toString());
                Log.i("IS_TAKEN:", dataSnapshot.child("isTaken").getValue().toString());
                */

                startTime = dataSnapshot.child("startTime").getValue().toString();
                endTime = dataSnapshot.child("endTime").getValue().toString();
                startTimePeriod = dataSnapshot.child("startTimePeriod").getValue().toString();
                endTimePeriod = dataSnapshot.child("endTimePeriod").getValue().toString();


                challenge = dataSnapshot.child("challenge").getValue().toString();

                isAvailable = dataSnapshot.child("isAvailable").getValue().toString();
                challenge = dataSnapshot.child("challenge").getValue().toString();


                viewQuizId.setText(dataSnapshot.child("quizId").getValue().toString());

                viewTimeFrom.setText(startTime);
                viewTimeto.setText(endTime);
                if (dataSnapshot.child("isAvailable").getValue().toString().equals("true")) {
                    viewIsAvailable.setText("Quiz Is Available");
                    buttonStartQuiz.setEnabled(true);

                } else {
                    viewIsAvailable.setText("Quiz Is Not Available");
                    buttonStartQuiz.setEnabled(false);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss aaa");

                    Calendar currentDate = Calendar.getInstance();

                    String currentDateString = myFormat.format(currentDate.getTime());

                    Date formattedCurrentDate = myFormat.parse(currentDateString);

                    Date formattedStartTime = myFormat.parse(startTime);
                    Date formattedEndTime = myFormat.parse(endTime);


                    if ((formattedCurrentDate.compareTo(formattedStartTime) > 0) &&
                            (formattedCurrentDate.compareTo(formattedEndTime) < 0)) {
                        if (editTextChallenge.getText().toString().equals(challenge)) {


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (!isFinishing()) {
                                        new AlertDialog.Builder(ClassDetailActivity.this)
                                                .setTitle("Starting the quiz...")
                                                .setMessage("Are you sure you want to start this quiz? ")
                                                .setCancelable(false)
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent takeQuizIntent = new Intent(getApplicationContext(), TakeQuizActivity.class);

                                                        startActivity(takeQuizIntent);
                                                    }
                                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }).create().show();

                                    }
                                }
                            });


                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Wrong Quiz Pass Code", Toast.LENGTH_SHORT);
                            toast.show();

                        }


                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Quiz is not available at this time", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (Exception e) {
                    Log.i("Exception caught: ", e.getMessage());
                }
            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                Intent intent = new Intent(this, SignUpActivity.class);
                if (Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                return true;
            case R.id.home_menu:
                Intent homeIntent = new Intent(getApplicationContext(), ClassListActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.user_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), UserSettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.help_menu:
                Intent helpIntent = new Intent(getApplicationContext(), UserManualActvity.class);
                startActivity(helpIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
