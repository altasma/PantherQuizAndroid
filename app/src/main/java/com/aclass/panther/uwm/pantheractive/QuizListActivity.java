package com.aclass.panther.uwm.pantheractive;


/**
 * Created by Asmamaw on 10/26/16.
 */

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

import java.util.ArrayList;
import java.util.List;

public class QuizListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz QuizList Activity log";

    private GoogleApiClient mGoogleApiClient;

    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;

    final List data1 = new ArrayList<QuizModel>();
    final List<QuizModel> quizList = new ArrayList<QuizModel>();

    private String extra_class_name;
    private String extra_class_id;
    private String extra_quiz_name;

    private TextView classTitleText;
    private QuizModel quiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_quiz_list);

        classTitleText = (TextView) findViewById(R.id.classTitle);

        Bundle class_extras = getIntent().getExtras();
        if (class_extras != null) {
            //Log.i("extras, ", class_extras.toString());
            extra_class_name = class_extras.getString("EXTRA_CLASS_NAME");
            classTitleText.setText(extra_class_name);
            // Log.i("extr_class_name,", extra_class_name);
            extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
            // Log.i("extr_class_id,", extra_class_id);

        }


        quiz = new QuizModel();
        quiz.setQuizName("--Select a Quiz--"); //place holder for index 0,


        data1.add(quiz);  //dummy classRoom for displaying select message
        quizList.add(quiz);


        Spinner spinner = (Spinner) findViewById(R.id.quizList_spinner);
        spinner.setOnItemSelectedListener(this);


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data1);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();

        if (extra_class_id != null) {
            database2.child("quizzes").child(extra_class_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data1.clear();
                    data1.add(quiz); //Re-adding the dummy quiz

                    quizList.clear();
                    quizList.add(quiz);

                    for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {

                        QuizModel q1 = new QuizModel();
                        try {
                            if(classDataSnapShot.child("isAvailable").getValue().toString().equals("true")) {
                                q1.setQuizName(classDataSnapShot.child("quizName").getValue().toString());
                                //Log.i("quizName:", classDataSnapShot.child("quizName").getValue().toString());
                                q1.setQuizId(classDataSnapShot.getKey());

                                data1.add(q1);
                                quizList.add(q1);
                            }
                            else{
                                    //Quiz is locked, do not add it to the spinner
                                continue;
                            }


                        } catch (Exception e) {
                            Log.i("Exception", e.getMessage());
                        }

                        // Log.i("SnapShotto string", classDataSnapShot.toString());

                    }
                    adapter.notifyDataSetChanged();

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
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
        /*
        Log.i("BeforeINtentStart1: ", i + "");
        Log.i("View:", view.toString());
        Log.i("item i:", i + "");
        Log.i("long l: ", l + "");
        Log.i("adaptwrView", adapterView.getItemIdAtPosition(i) + "");
        */

        if (mLastSpinnerPosotion == i) {
            return;
        }
        mLastSpinnerPosotion = i;
        // Log.i("BeforeINtentStart2: ", i + "");
        Intent listViewIntent = new Intent(this, QuizDetailActivity.class);
        listViewIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
        listViewIntent.putExtra("EXTRA_QUIZ_ID", quizList.get(i).getQuizId());
        listViewIntent.putExtra("EXTRA_QUIZ_NAME", quizList.get(i).getQuizName());
        listViewIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);
        mLastSpinnerPosotion = 0;
        // Log.i("BeforeINtentStart3: ", i + "");
        // Log.i("Data1 at" + i  , data1.get(i).toString());
        startActivity(listViewIntent);
        // Log.i("BeforeINtentStart4: ", i + "");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_quiz, menu);
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

            case R.id.score_menu:
                Intent quizReportIntent = new Intent(getApplicationContext(), QuizReportByClassActivity.class);
                quizReportIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                quizReportIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);
                startActivity(quizReportIntent);
                return true;
            case R.id.help_menu:
                Intent helpIntent = new Intent(getApplicationContext(), UserManualActvity.class);
                startActivity(helpIntent);
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
