package com.aclass.panther.uwm.pantheractive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aclass.panther.uwm.pantheractive.MainActivity.ANONYMOUS;


/**
 * Created by Asmamaw on 10/26/16.
 */

public class QuizReportByClassActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    QuizReportModel[] quizzes;   //array of quizzes for a class
    QuizReportModel[] quizzes1;

    ListView quizListview;
    List<QuizReportModel> quizList;
    List<String> extras;

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;

    private GoogleApiClient mGoogleApiClient;

    QuizReportAdapter adapter;
    QuizReportAdapter adapter1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private Button myClassListBtn, quizListbtn;

    private String TAG = "PantherQuiz QuizReportByClass Activity log";


    //Intent messsage references
    private String extra_quiz_id;
    private String extra_class_id, extra_class_name, extra_class_number;
    private String studentId; //used for adding quizzes as a key
    private Map<Integer, QuizReportModel> answeredLists;  //map of quiz number as key, and question as model

    private TextView classIdText, classNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //setContentView(R.layout.question);
        setContentView(R.layout.activity_quiz_report_by_class);

        myClassListBtn = (Button) findViewById(R.id.btnClassList);
        quizListbtn = (Button) findViewById(R.id.btnQuizList);
        extras = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        studentId = mFirebaseUser.getEmail().replace("@", "-");
        studentId = studentId.replace(".", "dot");

       /*
        Log.i("studentId", studentId);
        Log.i("mFirebasAuth:", mFirebaseAuth.toString());
        Log.i("mFirebaseUser:", mFirebaseUser.getEmail());
        */

        classNameText = (TextView) findViewById(R.id.classNameText);
        classIdText = (TextView) findViewById(R.id.classIdText);

        answeredLists = new HashMap<>(); //completed quiz lists
        Bundle class_extras = getIntent().getExtras();
        // Log.i("extrasQuizReport, ", class_extras.toString());
        if (class_extras != null) {
            extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
            extra_class_name = class_extras.getString("EXTRA_CLASS_NAME");
            classNameText.setText("CLASS NAME - " + extra_class_name);
            //Log.i("!extr_class_id,", extra_class_id + " ");
            // Log.i("!extr_class_name,", extra_class_name + " ");


        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);   //uses the ic_launcher icon as title log
        }

        quizList = new ArrayList<>();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUsername = ANONYMOUS;

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


        myClassListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myClassListIntent = new Intent(getApplicationContext(), ClassListActivity.class);
                startActivity(myClassListIntent);
            }
        });

        quizListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent quizListIntent = new Intent(getApplicationContext(), QuizListActivity.class);
                quizListIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                quizListIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);
                startActivity(quizListIntent);

            }
        });
        final List data1 = new ArrayList<AnsweredQuestionModel>(); //


        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (extra_class_id != null) {
            // Log.i("starting databse access...", extra_class_id);
            mDatabase.child("studentsQuiz").child(extra_class_id).child(studentId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            data1.clear();

                            try {
                                if(!dataSnapshot.hasChildren()){
                                    Toast toast = Toast.makeText(getApplicationContext(), "You have no attempted quizzes for "+ extra_class_name, Toast.LENGTH_LONG);
                                    toast.setGravity(0,0,0);
                                    Intent backToClassListIntent = new Intent(getApplicationContext(), ClassListActivity.class);
                                    toast.show();
                                    startActivity(backToClassListIntent);
                                    finish();

                                }

                                for (DataSnapshot s : dataSnapshot.getChildren()) {
                                    QuizReportModel q1 = new QuizReportModel();
                                    q1.setQuizId(s.child("quizId").getValue().toString());
                                    q1.setQuizName(s.child("quizName").getValue().toString());
                                    q1.setScore(s.child("score").getValue().toString() + "%");
                                    q1.setQuizKey(s.getKey());

                            /*
                            Log.i("quizKey", s.getKey());
                            Log.i("s",s.toString());
                            Log.i("s.isCompleted", s.child("isCompleted").getValue().toString());
                            Log.i("s.isInprogress", s.child("isInprogress").getValue().toString());
                            Log.i("s.score", s.child("score").getValue().toString());
                            Log.i("s.quizName", s.child("quizName").getValue().toString());
                            Log.i("s.quizId", s.child("quizId").getValue().toString());
                            */

                                    data1.add(q1);

                                    // Log.i(TAG + "After add1 q1 is", q1.toString());
                                    // Log.i(TAG + "After add1 data1 is ", data1.get(0).toString());

                                }


                            } catch (Exception e) {
                                Log.i("Exception in querying quiz lists", e.getMessage());

                            }

                            //Log.i("Size of Data1", " " + data1.size());

                            QuizReportModel[] quizzes1 = new QuizReportModel[data1.size()];
                            data1.toArray(quizzes1);

                            quizListview = (ListView) findViewById(R.id.quizListView);

                            //to be used by the QuizReportAdapter to pass it to QuestionsQuizReport actvity
                            extras.add(extra_class_name);
                            extras.add(extra_class_id);
                            extras.add(extra_class_number);

                            adapter1 = new QuizReportAdapter(QuizReportByClassActivity.this, quizzes1, extras);

                            quizListview.setAdapter(adapter1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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