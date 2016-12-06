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

public class TakeQuizActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ListView questionListview;
    AnsweredQuestionModel[] questions1;

    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    QuestionsAdapter adapter;
    QuestionsAdapter adapter1;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private Button submitButton, saveButton;
    private TextView classIdText, quizIdText;
    private String TAG = "PantherQuiz TakeQuiz Activity log";

    private String isAvailable = "";
    private String isCompleted = "";
    private String isInprogress = "";
    private String quizName = "";
    private String quizId = "";
    private String challenge = "";

    //Intent messsage references
    private String extra_quiz_id, extra_quiz_name;
    private String extra_class_id, extra_class_name;
    private String studentId; //used for adding quizzes as a key
    private Map<Integer, AnsweredQuestionModel> answeredLists;  //map of question number as key, and question as model


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //setContentView(R.layout.question);
        setContentView(R.layout.activity_take_quiz);
        classIdText = (TextView) findViewById(R.id.classIdText);
        quizIdText = (TextView) findViewById(R.id.quizIdText);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        studentId = mFirebaseUser.getEmail().replace("@", "-");
        studentId = studentId.replace(".", "dot");

        /*
        Log.i("studentId", studentId);
        Log.i("mFirebasAuth:", mFirebaseAuth.toString());
        Log.i("mFirebaseUser:", mFirebaseUser.getEmail());
        */

        answeredLists = new HashMap<>();
        final Bundle class_extras = getIntent().getExtras();
        // Log.i("extrasTakeQuiz, ", class_extras.toString());
        if (class_extras != null) {
            if (class_extras.get("EXTRA_QUIZ_ID") != null) {
                extra_quiz_id = class_extras.getString("EXTRA_QUIZ_ID");
                // Log.i("extra_QUIZ_ID,", extra_quiz_id);
            }
            if (class_extras.get("EXTRA_CLASS_ID") != null) {
                extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
                // Log.i("extr_class_id,", extra_class_id);
            }
            if (class_extras.getString("EXTRA_CLASS_NAME") != null) {
                extra_class_name = class_extras.getString("EXTRA_CLASS_NAME");
                classIdText.setText("Class- " + extra_class_name);
                // Log.i("className", extra_class_name);
            }
            if (class_extras.get(("EXTRA_QUIZ_NAME")) != null) {
                extra_quiz_name = class_extras.getString("EXTRA_QUIZ_NAME");
                quizIdText.setText("Quiz- " + extra_quiz_name);
                // Log.i("quizName", extra_quiz_name);
            }

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);   //uses the ic_launcher icon as title log
        }

        submitButton = (Button) findViewById(R.id.submitQuizBtn);
        saveButton = (Button) findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    DatabaseReference quizRef = mDatabase.child("studentsQuiz").child(extra_class_id)
                            .child(studentId).child(extra_quiz_id);

                    // Log.i("Save is Completed: ", " " + isCompleted);
                    if (isCompleted != null && isCompleted.equals("true")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Quiz has already been submitted.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {

                        quizRef.child("isInprogress").setValue("true");
                        quizRef.child("isCompleted").setValue("false");
                        quizRef.child("quizId").setValue(quizId);
                        quizRef.child("quizName").setValue(quizName);
                        quizRef.child("score").setValue(0);
                        //individually add all the questions
                        for (int k = 0; k < adapter1.getCount(); k++) {
                            //Log.i("on Submit Adapater[, " + k + "]: ", adapter1.getItem(k).toString());
                            String questionKey = quizRef.child("questions").push().getKey(); //may use the original key from the quizzes node??
                            quizRef.child("questions").child(questionKey).setValue(adapter1.getItem(k));
                        }
                        Toast.makeText(getApplicationContext(), "Quiz saved successfully.", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    //Log.i("Database Error! ", "Exception in writing to the database." + e.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Database Error, please try again", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();


                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.i("OnCSubmisdfsd,:", "asdsadf");
                // Log.i("Before Submit on Success,datapter1 size ", adapter1.getCount() + "");
                int studCorrectAnsCount = 0;
                int unAttemptedQuestCount = 0;
                int totalQuestions = adapter1.getCount();
                for (int k = 0; k < adapter1.getCount(); k++) {
                    AnsweredQuestionModel studQuestionOnSubmit = adapter1.getItem(k);
                    if (studQuestionOnSubmit.getStudentAnswer() != null) {
                        //student attempted the question
                        if (studQuestionOnSubmit.getStudentAnswer().equals(studQuestionOnSubmit.getAnswer())) {
                            studCorrectAnsCount++; // student get correct this question
                        } else {
                            // do nothing
                        }
                    } else {
                        unAttemptedQuestCount++; //
                    }//end of if submittedQuestion.getStudAnswer!= null


                    //  Log.i("on Submit AdapaterBefroe[, " + k + "]: ", studQuestionOnSubmit.toString());
                }//end of for loop
                double scoreInPercent = (float) studCorrectAnsCount / (float) totalQuestions;
                scoreInPercent = (scoreInPercent * 100.0);
                String str = String.format("%1.2f", scoreInPercent); // rounding to 2 decimal place

                scoreInPercent = Double.valueOf(str);

                /*
                Log.i("scorePercent: ", scoreInPercent + "");
                Log.i("studeCorrectCount:", studCorrectAnsCount + "");
                Log.i("unattemptedCount:", unAttemptedQuestCount + "");
                Log.i("totalQuestion:", totalQuestions + "");
                */

                final int unAttempted = unAttemptedQuestCount;
                final double scorePercent = scoreInPercent;
                final int correctCount = studCorrectAnsCount;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            new AlertDialog.Builder(TakeQuizActivity.this)
                                    .setTitle("Submitting Quiz...")
                                    .setMessage("" + unAttempted + " Unattempted Questions, are you sure you want to submit it?")
                                    .setCancelable(false)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                // Log.i("Submit on Success,datapter1 size ", adapter1.getCount() + "");

                                                DatabaseReference quizRef = mDatabase.child("studentsQuiz").child(extra_class_id)
                                                        .child(studentId).child(extra_quiz_id);

                                                // Log.i("SubisCompleted: ", isCompleted);

                                                quizRef.child("score").setValue(scorePercent);

                                                quizRef.child("isInprogress").setValue("false");
                                                quizRef.child("isCompleted").setValue("true");
                                                quizRef.child("quizId").setValue(quizId);
                                                quizRef.child("quizName").setValue(quizName);
                                                //individually add all the questions
                                                for (int k = 0; k < adapter1.getCount(); k++) {
                                                    //Log.i("on Submit Adapater[, " + k + "]: ", adapter1.getItem(k).toString());
                                                    String questionKey = quizRef.child("questions").push().getKey(); //may use the original key from the quizzes node??
                                                    quizRef.child("questions").child(questionKey).setValue(adapter1.getItem(k));
                                                }


                                                Toast toast = Toast.makeText(getApplicationContext(), "Quiz Submitted Sucessfully! SCORE: " + scorePercent + "%", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                                Intent intent = new Intent(getApplicationContext(), QuizReportByClassActivity.class);
                                                intent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                                                intent.putExtra("EXTRA_CLASS_NAME", extra_class_name);

                                                startActivity(intent);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast toast = Toast.makeText(getApplicationContext(), "Database  connection error! " + scorePercent + "%", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();

                                            }
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
        Log.i(TAG, "Inside take quiz acitivty");
        // Initialize Firebase Auth

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Log.i("Not signed in", "user not signed in");
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

        final List data1 = new ArrayList<AnsweredQuestionModel>(); //


        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("classes/quizzes/")
        if (extra_class_id != null && extra_quiz_id != null) {
            mDatabase.child("quizzes").child(extra_class_id).child(extra_quiz_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Log.i("CHALLENGE: ", dataSnapshot.child("challenge").getValue().toString());
                        // Log.i("END_DATE:", dataSnapshot.child("endDate").getValue().toString());
                        Log.i("END_TIME:", dataSnapshot.child("endTime").getValue().toString());
                        //  Log.i("START_DATE:", dataSnapshot.child("startDate").getValue().toString());
                        Log.i("START_TIME:", dataSnapshot.child("startTime").getValue().toString());
                        Log.i("QUIZ_ID:", dataSnapshot.child("quizId").getValue().toString());
                        Log.i("IS_EXPIRED:", dataSnapshot.child("isExpired").getValue().toString());
                        Log.i("IS_TAKEN:", dataSnapshot.child("isTaken").getValue().toString());

                        isAvailable = dataSnapshot.child("isAvailable").getValue().toString();
                        Log.i("isAvala:", isAvailable);
                        challenge = dataSnapshot.child("challenge").getValue().toString();
                        quizId = dataSnapshot.child("quizId").getValue().toString();
                        quizName = dataSnapshot.child("quizName").getValue().toString();
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
                Log.i("IsAvailable: ", dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals("true")) {
                    mDatabase.child("quizzes").child(extra_class_id).child(extra_quiz_id).child("questions")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    data1.clear();

                                    for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {
                                        // data1.clear();

                                        AnsweredQuestionModel q1 = new AnsweredQuestionModel();
                                        try {
                                            Log.i(TAG + "dsChildred", classDataSnapShot.getChildren().iterator().next().getValue().toString());
                                            Log.i(TAG + "DSP get value: ", classDataSnapShot.getValue().toString());
                                            DatabaseReference ref = classDataSnapShot.getRef();
                                            Log.i(TAG + "Question db:", ref.child("question").getDatabase().toString());
                                            Log.i(TAG + "Ref is:", ref.toString());
                                            Log.i(TAG + "Ref answer:", ref.child("answer").getKey());
                                            Log.i(TAG + "Answer: ", classDataSnapShot.child("answer").getValue().toString());
                                            Log.i(TAG + "Question: ", classDataSnapShot.child("question").getValue().toString());
                                            Log.i(TAG + "Choices: ", classDataSnapShot.child("choices").getValue().toString());

                                            HashMap chm = new HashMap<String, String>();

                                            q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                                            q1.setQuestion(classDataSnapShot.child("question").getValue().toString());
                                            Log.i("CHOICES....", classDataSnapShot.child("choices").getChildrenCount() + "");
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


                                            //  q1 = (QuestionModel) classDataSnapShot.getValue();
                                            Log.i(TAG + "q1 :", q1.toString());
                                        } catch (Exception e) {
                                            Log.i(TAG + "Eccveption", e.getMessage());
                                        }


                                        Log.i(TAG + "SnapShotto string", classDataSnapShot.toString());


                                        data1.add(q1);
                                        Log.i(TAG + "After add1 q1 is", q1.toString());
                                        Log.i(TAG + "After add1 data1 is ", data1.get(0).toString());
                                    }
                                    Log.i("Size of Data1", " " + data1.size());
                                    AnsweredQuestionModel[] questions1 = new AnsweredQuestionModel[data1.size()];
                                    data1.toArray(questions1);
                                    Log.i("Size of questions1 : ", questions1.length + "");
                                    if (questions1.length <= 0) {
                                        Toast.makeText(getApplicationContext(), "Empty Quiz.", Toast.LENGTH_LONG).show();
                                        Intent backToQuizDetail = new Intent(getApplicationContext(), QuizDetailActivity.class);
                                        backToQuizDetail.putExtra("EXTRA_QUIZ_ID", extra_quiz_id);
                                        backToQuizDetail.putExtra("EXTRA_CLASS_ID", extra_class_id);
                                        backToQuizDetail.putExtra("EXTRA_CLASS_NAME", extra_class_name);
                                        if (backToQuizDetail != null) {
                                            startActivity(backToQuizDetail);
                                            finish();

                                        }
                                    }
                                    for (int i = 0; i < questions1.length; i++) {
                                        Log.i(i + "question: ", questions1[i].toString());
                                    }
                                    // List<QuestionModel> ls = new ArrayList<QuestionModel>();
                                    questionListview = (ListView) findViewById(R.id.questionListView);
                                    adapter1 = new QuestionsAdapter(TakeQuizActivity.this, questions1);

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

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();

    }

}
