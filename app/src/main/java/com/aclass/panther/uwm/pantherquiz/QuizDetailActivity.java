package com.aclass.panther.uwm.pantherquiz;


/**
 * Created by Asmamaw on 10/26/16.
 */

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

public class QuizDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    public static final String ANONYMOUS = "anonymous";


    private String mUsername;
    private String mPhotoUrl;

    private GoogleApiClient mGoogleApiClient;

    private Button submitButton, listQuizzesBtn;
    private String TAG = "PantherQuiz QuizDetail  Activity log ";

    private String challenge = "";
    private String startDate, startTime, endDate, endTime, startTimePeriod, endTimePeriod;
    private String quizNumber, quizName;

    private TextView viewClassId, viewQuizId, viewDateFrom, viewDateTo, viewTimeFrom, viewTimeto;
    private TextView viewIsAvailable, viewIsTaken;
    private EditText editTextChallenge;
    private Button buttonStartQuiz;

    //Intent messsage references
    private String extra_quiz_id, extra_quiz_name;
    private String extra_class_id, extra_class_name;
    private String studentId; //stripped (@, .) emailId

    private String isAvailable = "";
    private String isCompleted = "";
    private String isInprogress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_quiz_detail);

        Bundle class_extras = getIntent().getExtras();
//        Log.i("extrasQuiz, ", class_extras.toString());
        if (class_extras != null) {
            extra_quiz_id = class_extras.getString("EXTRA_QUIZ_ID");
            //Log.i("extra_QUIZ_ID,", extra_quiz_id);
            extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
            extra_class_name = class_extras.getString("EXTRA_CLASS_NAME");
            //           Log.i("CLASSNAME", extra_class_name);
            extra_quiz_name = class_extras.getString("EXTRA_QUIZ_NAME");

            // Log.i("extr_class_id,", extra_class_id);

        }

        viewClassId = (TextView) findViewById(R.id.textViewClassId);
        viewQuizId = (TextView) findViewById(R.id.textViewQuizId);
        viewTimeFrom = (TextView) findViewById(R.id.textViewTimeFrom);
        viewTimeto = (TextView) findViewById(R.id.textViewTimeTo);

        viewIsAvailable = (TextView) findViewById(R.id.textViewIsAvailable);
        viewIsTaken = (TextView) findViewById(R.id.textViewIsTaken);

        editTextChallenge = (EditText) findViewById(R.id.editTextChallenge);
        buttonStartQuiz = (Button) findViewById(R.id.buttonStartQuiz);
        listQuizzesBtn = (Button) findViewById(R.id.buttonListQuizzes);

        viewClassId.setText(extra_class_name);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        studentId = mFirebaseUser.getEmail().replace("@", "-");
        studentId = studentId.replace(".", "dot");
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

        if (extra_class_id != null && extra_quiz_id != null) {
            DatabaseReference quizRef = mDatabase.child("studentsQuiz").child(extra_class_id)
                    .child(studentId).child(extra_quiz_id);
            quizRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("isCompleted").getValue() != null) {
                        isCompleted = dataSnapshot.child("isCompleted").getValue().toString();
                        if (isCompleted != null && isCompleted.equals("true")) {
                            buttonStartQuiz.setEnabled(false);
                        }
                    }
                    if (dataSnapshot.child("isInprogress").getValue() != null) {
                        isInprogress = dataSnapshot.child("isInprogress").getValue().toString();
                    }
                    if (isCompleted != null && isCompleted.equals("true")) {
                        viewIsTaken.setText("Quiz Attempted");
                        buttonStartQuiz.setEnabled(false);
                    } else if (isInprogress != null && isInprogress.equals("true")) {
                        viewIsTaken.setText("Quiz In Progress");
                        buttonStartQuiz.setEnabled(true);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (extra_class_id != null && extra_quiz_id != null) {
            mDatabase.child("quizzes").child(extra_class_id).child(extra_quiz_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
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
                        isAvailable = dataSnapshot.child("isAvailable").getValue().toString();
                        // Log.i("IsAvailable: ", isAvailable);
                        challenge = dataSnapshot.child("challenge").getValue().toString();


                        viewQuizId.setText(dataSnapshot.child("quizId").getValue().toString());
                        viewTimeFrom.setText(startTime);
                        viewTimeto.setText(endTime);
                        if (dataSnapshot.child("isAvailable").getValue().toString().equals("true")) {
                            viewIsAvailable.setText("Quiz Is Available");
                            if (isCompleted != null && isCompleted.equals("false")) {
                                buttonStartQuiz.setEnabled(true);
                            } else if (isCompleted != null && isCompleted.equals("true")) {
                                buttonStartQuiz.setEnabled(false);

                            } else {
                                buttonStartQuiz.setEnabled(true);

                            }


                        } else {
                            viewIsAvailable.setText("Quiz Is Not Available");
                            buttonStartQuiz.setEnabled(false);

                        }
                    }
                    catch (Exception e){
                        //Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                        Intent classListIntent = new Intent(getApplicationContext(), ClassListActivity.class);
                        startActivity(classListIntent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        listQuizzesBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent quizReportIntent = new Intent(getApplicationContext(), QuizReportByClassActivity.class);
                quizReportIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                quizReportIntent.putExtra("EXTRA_STUDENT_ID", studentId);
                quizReportIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);
                quizReportIntent.putExtra("EXTRA_QUIZ_NAME", extra_quiz_name);

                startActivity(quizReportIntent);

            }
        });

        //Once the student confirms to start the quiz, snapshot of this quiz will be
        //copied under his attempted quiz lists.
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
                                        new AlertDialog.Builder(QuizDetailActivity.this)
                                                .setTitle("Starting the quiz...")
                                                .setMessage("Are you sure you want to start this quiz? ")
                                                .setCancelable(false)
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //Log.i("BEfore takeQuiz Intent,", "Before take Quiz INtent");
                                                        Intent takeQuizIntent = new Intent(getApplicationContext(), TakeQuizActivity.class);
                                                        takeQuizIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                                                        takeQuizIntent.putExtra("EXTRA_QUIZ_ID", extra_quiz_id);
                                                        takeQuizIntent.putExtra("EXTRA_QUIZ_NAME", extra_quiz_name);
                                                        takeQuizIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);

                                                        //Save the quiz under 'studQuizzes' if not yet save node, Taking quiz from this node.

                                                        if(isInprogress.equals("true")){
                                                            //navigate to TakeQuizActivity
                                                        }
                                                        else{
                                                            //copy the quiz to studQuizzes node and navigate to
                                                        }



                                                        // Log.i("BEfore takeQuiz Intent2,", "Before take Quiz INtent");

                                                        startActivity(takeQuizIntent);
                                                        //Log.i("BEfore takeQuiz Intent3,", "Before take Quiz INtent");

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
