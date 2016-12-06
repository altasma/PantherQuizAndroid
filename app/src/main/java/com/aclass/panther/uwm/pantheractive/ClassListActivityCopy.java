package com.aclass.panther.uwm.pantheractive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class ClassListActivityCopy extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;

    QuestionModel question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_class_list_copy);


        question = new QuestionModel();

        final List data1 = new ArrayList<QuestionModel>();
        // data1.add(ques);

        Spinner spinner = (Spinner) findViewById(R.id.classList_spinner);
        spinner.setOnItemSelectedListener(this);
        final List data = new ArrayList<String>();
        data.add("--Select a Class");
        data1.add(question);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();

        database2.child("classes/quizzes/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data1.clear();

                for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {

                    QuestionModel q1 = new QuestionModel();
                    try {

                        DatabaseReference ref = classDataSnapShot.getRef();

                        q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                        q1.setQuestion(classDataSnapShot.child("question").getValue().toString());
                        HashMap chm = new HashMap<String, String>();
                        chm.put("Achoice", classDataSnapShot.child("choices").getValue());


                    } catch (Exception e) {
                        Log.i("Eccveption", e.getMessage());
                    }

                    data1.add(q1);

                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignUpActivity.class));
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
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
        Intent listViewIntent = new Intent(this, QuizListActivity.class);
        startActivity(listViewIntent);
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
