package com.aclass.panther.uwm.pantheractive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz QuizList Activity log";

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_quiz_list);

        final List data1 = new ArrayList<QuestionModel>();


        Spinner spinner = (Spinner) findViewById(R.id.quizList_spinner);
        spinner.setOnItemSelectedListener(this);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quiz_lists, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();
//        database2.child("classes/quizzes/").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.i("dataSnapshot1", dataSnapshot.toString());
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        database2.child("classes/quizzes/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data1.clear();

                for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {
                    // data1.clear();

                    QuestionModel q1 = new QuestionModel();
                    try {
                        Log.i("dsChildred", classDataSnapShot.getChildren().iterator().next().getValue().toString());
                        Log.i("DSP get value: ", classDataSnapShot.getValue().toString());
                        DatabaseReference ref = classDataSnapShot.getRef();
                        Log.i("Question db:", ref.child("question").getDatabase().toString());
                        Log.i("Ref is:", ref.toString());
                        Log.i("Ref answer:", ref.child("answer").getKey());
                        Log.i("Answer: ", classDataSnapShot.child("answer").getValue().toString());
                        Log.i("Question: ", classDataSnapShot.child("question").getValue().toString());
                        Log.i("Choices: ", classDataSnapShot.child("choices").getValue().toString());

                        q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                        q1.setQuestion(classDataSnapShot.child("question").getValue().toString());
                        HashMap chm = new HashMap<String, String>();
                        chm.put("Achoice", classDataSnapShot.child("choices").getValue());

                        //  q1 = (QuestionModel) classDataSnapShot.getValue();
                        Log.i("q1 :", q1.toString());
                    } catch (Exception e) {
                        Log.i("Eccveption", e.getMessage());
                    }


                    Log.i("SnapShotto string", classDataSnapShot.toString());

                    if (classDataSnapShot.getKey().equals("choices")) {
                        Log.i("child tostring key", classDataSnapShot.getKey());
//                        QuestionModel q1 = new QuestionModel();
                        HashMap nhm = new HashMap();
                        nhm.put("A", classDataSnapShot.getValue().toString());
                        q1.setChoices(nhm);
                        Log.i("Q1 is ", q1.getChoices().toString());
                        Log.i("Q1", q1.toString());

                    } else if (classDataSnapShot.getKey().equals("question")) {
                        q1.setQuestion(classDataSnapShot.getValue().toString());
                    } else if (classDataSnapShot.getKey().equals("answer")) {
                        q1.setAnswer(classDataSnapShot.getValue().toString());
                    } else {

                    }
                    // QuestionModel q  = classDataSnapShot.getValue(QuestionModel.class);
                    data1.add(q1);
                    Log.i("After add1 q1 is", q1.toString());
                    Log.i("After add1 data1 is ", data1.get(0).toString());
                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //  Log.i("Data1 lower: ", data1.get(0).toString());

        Log.i(TAG, database2.toString());


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
                // mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


    }

    private int mLastSpinnerPosotion = 0;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mLastSpinnerPosotion == i) {
            return;
        }
        mLastSpinnerPosotion = i;
//        Intent listViewIntent = new Intent(this, TakeQuizActivity.class);
//        startActivity(listViewIntent);
        mLastSpinnerPosotion = 0;
        Intent quizDetailIntent = new Intent(this, QuizDetailActivity.class);
        startActivity(quizDetailIntent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                //  startActivity(new Intent(this, SignUpActivity.class));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();

    }
}
