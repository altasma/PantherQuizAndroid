package com.aclass.panther.uwm.pantheractive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuizActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    public static final String TAG = "PantherQuiz QuizActvity";

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mQuestion;



    private RecyclerView mQuestions;
    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<QuestionModel, QuestionHolder> mRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ListView questionView = (ListView) findViewById(R.id.questionListView);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
}