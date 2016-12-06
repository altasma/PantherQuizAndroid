package com.aclass.panther.uwm.pantheractive;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

public class TakeQuizActivity4 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ListView questionListview;
    Question[] questions;   //array of questions for a quiz
    QuestionModel[] questions1;
    List<QuestionModel> questionList;

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    QuestionsAdapter4 adapter;
    QuestionsAdapter4 adapter1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private Button submitButton;
    private String TAG = "PantherQuiz TakeQuiz Activity log";

    private String isAvailable = "";
    private String challenge = "";

    //Intent messsage references
    private String extra_quiz_id;
    private String extra_class_id;

    private Map<Integer, AnsweredQuestionModel> answeredLists;  //map of question number as key, and question as model


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //setContentView(R.layout.question);
        setContentView(R.layout.activity_take_quiz);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        //  Log.i("mFirebasAuth:", mFirebaseAuth.toString());
        //  Log.i("mFirebaseUser:", mFirebaseUser.getEmail());

        answeredLists = new HashMap<>();
        Bundle class_extras = getIntent().getExtras();

        // Log.i("extrasTakeQuiz, ", class_extras.toString());
        if (class_extras != null) {
            extra_quiz_id = class_extras.getString("EXTRA_QUIZ_ID");
            //  Log.i("extra_QUIZ_ID,", extra_quiz_id);
            extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
            //   Log.i("extr_class_id,", extra_class_id);

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);   //uses the ic_launcher icon as title log
            //  Log.i("ToolBar..", "toolBar is not null");
        }
        //  Log.i("ToolBarisnull..", "toolBar is null");

        questionList = new ArrayList<>();
        submitButton = (Button) findViewById(R.id.submitQuizBtn);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            new AlertDialog.Builder(TakeQuizActivity4.this)
                                    .setTitle("Submitting Quiz...")
                                    .setMessage("Are you sure you want to submit this quiz? ")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Quiz Submitted Sucessfully!", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            Intent intent = new Intent(getApplicationContext(), QuizReportByClassActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).create().show();

                        }
                    }
                });

            }
        });

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        //  Log.i(TAG, "Inside take quiz acitivty");
        // Initialize Firebase Auth

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            //  Log.i("Not signed in", "user not signed in");
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

        final List data1 = new ArrayList<QuestionModel>();


        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("classes/quizzes/")
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

                        isAvailable = dataSnapshot.child("isAvailable").getValue().toString();
                        challenge = dataSnapshot.child("challenge").getValue().toString();
                    } catch (Exception e) {
                        Log.i("Exception in querrying quiz details", e.getMessage());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (isAvailable.equals("true")) {
            Toast.makeText(getApplicationContext(), "Now you can take the quiz", Toast.LENGTH_LONG).show();
        }

        mDatabase.child("quizzes").child(extra_class_id).child(extra_quiz_id).child("isAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.i("IsAvailable: ", dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals("true")) {
                    mDatabase.child("quizzes").child(extra_class_id).child(extra_quiz_id).child("questions")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    data1.clear();

                                    for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {
                                        // data1.clear();

                                        QuestionModel q1 = new QuestionModel();
                                        try {
                                            // Log.i(TAG + "dsChildred", classDataSnapShot.getChildren().iterator().next().getValue().toString());
                                            // Log.i(TAG + "DSP get value: ", classDataSnapShot.getValue().toString());

                                            DatabaseReference ref = classDataSnapShot.getRef();
                                           /*
                                            Log.i(TAG + "Question db:", ref.child("question").getDatabase().toString());
                                            Log.i(TAG + "Ref is:", ref.toString());
                                            Log.i(TAG + "Ref answer:", ref.child("answer").getKey());
                                            Log.i(TAG + "Answer: ", classDataSnapShot.child("answer").getValue().toString());
                                            Log.i(TAG + "Question: ", classDataSnapShot.child("question").getValue().toString());
                                            Log.i(TAG + "Choices: ", classDataSnapShot.child("choices").getValue().toString());
                                            */

                                            HashMap chm = new HashMap<String, String>();

                                            q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                                            q1.setQuestion(classDataSnapShot.child("question").getValue().toString());

                                            // Log.i("CHOICES....", classDataSnapShot.child("choices").getChildrenCount() + "");

                                            long numOfChoices = classDataSnapShot.child("choices").getChildrenCount();
                                            if (numOfChoices == 2) {
                                                chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                                                chm.put("B", classDataSnapShot.child("choices").child("B").getValue());

                                            }
                                            if (numOfChoices == 3) {
                                                chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                                                chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                                                chm.put("C", classDataSnapShot.child("choices").child("C").getValue());


                                            }
                                            if (numOfChoices == 4) {
                                                chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                                                chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                                                chm.put("C", classDataSnapShot.child("choices").child("C").getValue());
                                                chm.put("D", classDataSnapShot.child("choices").child("D").getValue());


                                            }
                                            if (numOfChoices == 5) {
                                                chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                                                chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                                                chm.put("C", classDataSnapShot.child("choices").child("C").getValue());
                                                chm.put("D", classDataSnapShot.child("choices").child("D").getValue());
                                                chm.put("E", classDataSnapShot.child("choices").child("E").getValue());
                                            }
                                            if (numOfChoices == 6) {
                                                chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                                                chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                                                chm.put("C", classDataSnapShot.child("choices").child("C").getValue());
                                                chm.put("D", classDataSnapShot.child("choices").child("D").getValue());
                                                chm.put("E", classDataSnapShot.child("choices").child("E").getValue());
                                                chm.put("F", classDataSnapShot.child("choices").child("F").getValue());

                                            }


                                            q1.setChoices(chm);


                                            //  Log.i(TAG + "q1 :", q1.toString());

                                        } catch (Exception e) {
                                            // Log.i(TAG + "Exception", e.getMessage());
                                        }


                                        // Log.i(TAG + "SnapShotto string", classDataSnapShot.toString());

                                        data1.add(q1);

                                        //  Log.i(TAG + "After add1 q1 is", q1.toString());
                                        //  Log.i(TAG + "After add1 data1 is ", data1.get(0).toString());
                                    }

                                    // Log.i("Size of Data1", " " + data1.size());

                                    QuestionModel[] questions1 = new QuestionModel[data1.size()];
                                    data1.toArray(questions1);

                                    //Log.i("Size of questions1 : ", questions1.length + "");
//                                    for (int i = 0; i < questions1.length; i++) {
//                                        Log.i(i + "question: ", questions1[i].toString());
//                                    }
                                    // List<QuestionModel> ls = new ArrayList<QuestionModel>();


                                    questionListview = (ListView) findViewById(R.id.questionListView);
                                    adapter1 = new QuestionsAdapter4(TakeQuizActivity4.this, questions1);

                                    questionListview.setAdapter(adapter1);


                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.err.println("Listener was cancelled");

                                }
                            });


                } else {
                    Toast.makeText(getApplicationContext(), "This Quiz is not available.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), QuizListActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String myUserId = mFirebaseUser.getUid();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.getChildrenCount() == 0) {
                Toast.makeText(getApplicationContext(), "Empty Quiz", Toast.LENGTH_SHORT).show();
                return;
            }
            for (DataSnapshot questionsSnapshot : dataSnapshot.getChildren()) {
                // Log.i("INFO", questionsSnapshot.toString() + " KEY " + questionsSnapshot.getKey());
                // Log.i(TAG, "Afterrr the log inside for loop");

                QuestionModel queston = questionsSnapshot.getValue(QuestionModel.class);
                QuestionModel question1 = questionsSnapshot.getValue(QuestionModel.class);

                // Log.i(TAG, "After question is initializes inside the for loop");
                // Log.i(TAG, "" + questionsSnapshot.getKey().charAt(1));

                char i = questionsSnapshot.getKey().charAt(1);

                /*
                Log.i("TAG of i", "" + i);
                Log.i(TAG, "Starting toString....");
                Log.i(TAG, question1.getQuestion());
                Log.i(TAG, question1.getAnswer());
                Log.i(TAG, question1.getChoices().toString());
                Log.i(TAG, "Finishing toString....");
                */

                questionList.add(queston);

                updateListView();

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void updateListView() {
        adapter1.notifyDataSetChanged();
        questionListview.invalidate();
    }
}
