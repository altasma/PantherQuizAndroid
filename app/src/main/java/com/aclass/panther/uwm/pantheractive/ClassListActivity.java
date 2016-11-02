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

public class ClassListActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;

    QuestionModel question ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        question = new QuestionModel();
//        question.setQuestion("Example question");
//        question.setAnswer("A");
//        HashMap hm = new HashMap<String,String>();
//        hm.put("A","A is aa");
//        hm.put("B", "B is bb");
//        question.setChoices(hm);
//
//        QuestionModel ques = new QuestionModel();
//        ques.setAnswer("C");
//        ques.setQuestion("Another Question");
//        HashMap hm2 = new HashMap<String, String>();
//        hm2.put("A","Choice A of 2nd");
//        hm2.put("B", "Choice B of 2nd");
//        ques.setChoices(hm2);

        final List data1 = new ArrayList<QuestionModel>();
       // data1.add(ques);

        Spinner spinner = (Spinner) findViewById(R.id.classList_spinner);
        spinner.setOnItemSelectedListener(this);
        final List data = new ArrayList<String>();
        data.add("--Select a Class");
        data1.add(question);
        Log.i("Data1: ", data1.get(0).toString());
//        data.add(new String("CSS1"));
//        data.add(new String("CSS2"));
//        data.add(new String("CSS3"));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
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

                for(DataSnapshot classDataSnapShot : dataSnapshot.getChildren()){
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
                        HashMap chm = new HashMap<String,String>();
                        chm.put("Achoice", classDataSnapShot.child("choices").getValue());

                        //  q1 = (QuestionModel) classDataSnapShot.getValue();
                        Log.i("q1 :", q1.toString());
                    }
                    catch(Exception e){
                        Log.i("Eccveption", e.getMessage());
                    }


                    Log.i("SnapShotto string",classDataSnapShot.toString());

//                    if(classDataSnapShot.getKey().equals("choices")){
//                        Log.i("child tostring key", classDataSnapShot.getKey());
////                        QuestionModel q1 = new QuestionModel();
//                        HashMap nhm = new HashMap();
//                        nhm.put("A",classDataSnapShot.getValue().toString());
//                        q1.setChoices(nhm);
//                        Log.i("Q1 is ", q1.getChoices().toString());
//                        Log.i("Q1", q1.toString());
//
//                    }
//                    else if(classDataSnapShot.getKey().equals("question")){
//                        q1.setQuestion(classDataSnapShot.getValue().toString());
//                    }
//                    else if(classDataSnapShot.getKey().equals("answer")){
//                        q1.setAnswer(classDataSnapShot.getValue().toString());
//                    }
//                    else {
//
//                    }
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
        Log.i("Data1 lower: ", data1.get(0).toString());

        Log.i(TAG,database2.toString());

//        database.child("classes").child("class2").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                data.clear();
//                for(DataSnapshot classDataSnapShot : dataSnapshot.getChildren()){
//                    String a = classDataSnapShot.getValue(String.class);
//                    data.add(a);
//                }
//                //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data);
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.class_lists, android.R.layout.simple_spinner_item);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, StudentLogin.class));
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
    private  int mLastSpinnerPosotion = 0;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(mLastSpinnerPosotion == i){
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
                Intent intent = new Intent(this, StudentLogin.class);
                if(Build.VERSION.SDK_INT >= 11){
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                else{
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
              //  startActivity(new Intent(this, StudentLogin.class));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
