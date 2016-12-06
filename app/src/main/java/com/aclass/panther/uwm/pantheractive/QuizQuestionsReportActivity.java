package com.aclass.panther.uwm.pantheractive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class QuizQuestionsReportActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    public static final String ANONYMOUS = "anonymous";
    private String TAG = "PantherQuiz QuizQuestionsReportActivity  log";

    private String extra_class_id, extra_class_name, extra_quiz_key;

    private TextView classIdText, quizIdText, scoreText;
    private String studentId;
    private String isCompleted;
    private Button backToReportButton;

    ListView questionListview;
    CompletedQuestionsAdapter adapter1;

    private Map<Integer, AnsweredQuestionModel> questionLists;  //map of question number as key, and question as model

    private String mUsername;
    private String mPhotoUrl;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_quiz_questions_report);

        classIdText = (TextView) findViewById(R.id.classIdText);
        quizIdText = (TextView) findViewById(R.id.quizIdText);
        scoreText = (TextView) findViewById(R.id.scoreText);

        backToReportButton = (Button) findViewById(R.id.backToReportButton);

        backToReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToReportIntent = new Intent(getApplicationContext(), QuizReportByClassActivity.class);
                backToReportIntent.putExtra("EXTRA_CLASS_ID", extra_class_id);
                backToReportIntent.putExtra("EXTRA_QUIZ_KEY", extra_quiz_key);
                backToReportIntent.putExtra("EXTRA_CLASS_NAME", extra_class_name);
                startActivity(backToReportIntent);
            }
        });


        Bundle class_extras = getIntent().getExtras();
        // Log.i("extrasQuizReport, ", class_extras.toString());
        if (class_extras != null) {

            extra_class_id = class_extras.getString("EXTRA_CLASS_ID");
            extra_class_name = class_extras.getString("EXTRA_CLASS_NAME");
            extra_quiz_key = class_extras.getString("EXTRA_QUIZ_KEY");
            // classNameText.setText("CLASS NAME - " + extra_class_name);
            // Log.i("extr_class_id,", extra_class_id);
            // Log.i("etra_quiz_key", extra_quiz_key);

        }
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

        studentId = mFirebaseUser.getEmail().replace("@", "-");
        studentId = studentId.replace(".", "dot");
        // Log.i(TAG + "studentId", studentId);
        // Log.i(TAG + "mFirebasAuth:", mFirebaseAuth.toString());
        // Log.i(TAG + " mFirebaseUser:", mFirebaseUser.getEmail());

        questionLists = new HashMap<>();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final List data1 = new ArrayList<AnsweredQuestionModel>(); //

        if (extra_class_id != null && extra_quiz_key != null) {
            mDatabase.child("studentsQuiz").child(extra_class_id).child(studentId).child(extra_quiz_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        // Log.i("DataSnapShot:", dataSnapshot.toString());
                        // Log.i("QUIZ_ID:", dataSnapshot.child("quizId").getValue().toString());
                        quizIdText.setText("QUIZ ID: " + (dataSnapshot.child("quizId").getValue().toString()));
                        if(dataSnapshot.child("score").getValue() != null){
                            try {
                                String score = dataSnapshot.child("score").getValue().toString();
                                if (Double.parseDouble(score) > 80.0) {
                                    scoreText.setText(score + "%");
                                    scoreText.setTextColor(Color.rgb(0, 153, 51));
                                }
                                else if(Double.parseDouble(score) > 60){
                                    scoreText.setText(score + "%");
                                    scoreText.setTextColor(Color.rgb(255, 153, 0));
                                }
                                else{
                                    scoreText.setText(score + "%");
                                    scoreText.setTextColor(Color.rgb(179, 179,179));

                                }
                            }
                            catch(Exception e){

                            }


                        }
                        classIdText.setText("" + extra_class_name.toString());

                        isCompleted = dataSnapshot.child("isCompleted").getValue().toString();

                    } catch (Exception e) {
                        Log.i("Exception in querying quiz details", e.getMessage());

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (isCompleted != null && isCompleted.equals("true")) {
            Toast.makeText(getApplicationContext(), "Now you can take the quiz", Toast.LENGTH_LONG).show();
        }

        mDatabase.child("studentsQuiz").child(extra_class_id).child(studentId).child(extra_quiz_key).child("isCompleted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("isCompleted: ", dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals("true")) {
                    mDatabase.child("studentsQuiz").child(extra_class_id).child(studentId).child(extra_quiz_key).child("questions")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    data1.clear();

                                    for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {

                                        AnsweredQuestionModel q1 = new AnsweredQuestionModel();
                                        try {
                                            DatabaseReference ref = classDataSnapShot.getRef();

                                            HashMap chm = new HashMap<String, String>();

                                            q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                                            q1.setQuestion(classDataSnapShot.child("question").getValue().toString());
                                            q1.setStudentAnswer(classDataSnapShot.child("studentAnswer").getValue().toString());

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


                                            //Log.i(TAG + "q1 :", q1.toString());
                                        } catch (Exception e) {
                                            Log.i(TAG + "Exception", e.getMessage());
                                        }


                                        //Log.i(TAG + "SnapShotto string", classDataSnapShot.toString());


                                        data1.add(q1);
                                        //Log.i(TAG + "After add1 q1 is", q1.toString());
                                        // Log.i(TAG + "After add1 data1 is ", data1.get(0).toString());
                                    }
                                    // Log.i("Size of Data1", " " + data1.size());
                                    AnsweredQuestionModel[] questions1 = new AnsweredQuestionModel[data1.size()];
                                    data1.toArray(questions1);
                                    //Log.i("Size of questions1 : ", questions1.length + "");
                                    if (questions1.length <= 0) {
                                        Toast.makeText(getApplicationContext(), "Empty Quiz.", Toast.LENGTH_LONG).show();
                                        Intent backToQuizDetail = new Intent(getApplicationContext(), QuizDetailActivity.class);
                                        backToQuizDetail.putExtra("EXTRA_QUIZ_ID", extra_quiz_key);
                                        backToQuizDetail.putExtra("EXTRA_CLASS_ID", extra_class_id);
                                        if (backToQuizDetail != null) {
                                            startActivity(backToQuizDetail);
                                            finish();

                                        }
                                    }
//                                    for (int i = 0; i < questions1.length; i++) {
//                                        Log.i(i + "question: ", questions1[i].toString());
//                                    }
                                    // List<QuestionModel> ls = new ArrayList<QuestionModel>();
                                    questionListview = (ListView) findViewById(R.id.questionListView);
                                    adapter1 = new CompletedQuestionsAdapter(QuizQuestionsReportActivity.this, questions1);

                                    questionListview.setAdapter(adapter1);


                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.err.println("Listener was cancelled");

                                }
                            });


                } else {
                    Toast.makeText(getApplicationContext(), "Quiz has not been completed yet or  is not available yet.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();
    }
}
