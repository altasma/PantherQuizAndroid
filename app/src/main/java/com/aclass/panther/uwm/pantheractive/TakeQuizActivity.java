package com.aclass.panther.uwm.pantheractive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import static com.aclass.panther.uwm.pantheractive.MainActivity.ANONYMOUS;

public class TakeQuizActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    ListView questionListview;
    Question[] questions;   //array of questions for a quiz
    QuestionModel[] questions1;
    List<QuestionModel> questionList;

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

    private String TAG = "PantherQuiz TakeQuiz Activity log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.question);
        setContentView(R.layout.activity_take_quiz);
        questionList = new ArrayList<>();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;
        Log.i(TAG,"Inside take quiz acitivty");
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
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        final List data1 = new ArrayList<QuestionModel>();



        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("classes/quizzes/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data1.clear();

                for(DataSnapshot classDataSnapShot : dataSnapshot.getChildren()){
                    // data1.clear();

                    QuestionModel q1 = new QuestionModel();
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

                        HashMap chm = new HashMap<String,String>();
                      //  chm.put("Achoice", classDataSnapShot.child("choices").getValue());

                        q1.setAnswer(classDataSnapShot.child("answer").getValue().toString());
                        q1.setQuestion(classDataSnapShot.child("question").getValue().toString());
                        Log.i("CHOICES....", classDataSnapShot.child("choices").getChildrenCount() + "");
                        long numOfChoices = classDataSnapShot.child("choices").getChildrenCount();
                        if(numOfChoices == 2){
                            chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                            chm.put("B", classDataSnapShot.child("choices").child("B").getValue());

                        }
                        if(numOfChoices == 3){
                            chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                            chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                            chm.put("C", classDataSnapShot.child("choices").child("C").getValue());


                        }
                        if(numOfChoices == 4){
                            chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                            chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                            chm.put("C", classDataSnapShot.child("choices").child("C").getValue());
                            chm.put("D", classDataSnapShot.child("choices").child("D").getValue());



                        }
                        if(numOfChoices == 5){
                            chm.put("A", classDataSnapShot.child("choices").child("A").getValue());
                            chm.put("B", classDataSnapShot.child("choices").child("B").getValue());
                            chm.put("C", classDataSnapShot.child("choices").child("C").getValue());
                            chm.put("D", classDataSnapShot.child("choices").child("D").getValue());
                            chm.put("E", classDataSnapShot.child("choices").child("E").getValue());
                        }
                        if(numOfChoices == 6){
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
                    }
                    catch(Exception e){
                        Log.i(TAG + "Eccveption", e.getMessage());
                    }


                    Log.i(TAG + "SnapShotto string",classDataSnapShot.toString());

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
                    Log.i(TAG + "After add1 q1 is", q1.toString());
                    Log.i(TAG + "After add1 data1 is ", data1.get(0).toString());
                }
               // adapter1.notifyDataSetChanged();
                Log.i("Size of Data1", " " +data1.size());
                QuestionModel[] questions1 = new QuestionModel[data1.size()];
                data1.toArray(questions1);
                Log.i("Size of questions1 : ", questions1.length + "");
                for(int i = 0; i < questions1.length; i++){
                    Log.i(i + "question: ", questions1[i].toString());
                }
               // List<QuestionModel> ls = new ArrayList<QuestionModel>();
                questionListview = (ListView) findViewById(R.id.questionListView);
                adapter1 = new QuestionsAdapter(TakeQuizActivity.this,questions1);

                questionListview.setAdapter(adapter1);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









//        List<QuestionModel> ls = new ArrayList<QuestionModel>();
//        questionListview = (ListView) findViewById(R.id.questionListView);
       // questions = new Question[4];
       // questions1 = new QuestionModel[4];
//        for(int i = 0; i < questions.length;i++){
////            List<QuestionModel> ls = new ArrayList<QuestionModel>();
//            HashMap hs = new HashMap<String,String>();
//            hs.put("A","A one");
//            hs.put("B", "B one");
//          //  questions1[i] = new QuestionModel("A", "Question one",hs);
//          //  ls.add(new QuestionModel("First Question", "A", hs));
//            //questions[i]=new Question();
////            if(i == 2){
////                String[] choices = {"Yes", "No","Maybe"};
////                questions[i] = new Question("qustion from java", 4,choices,"A" );
////            }
//        }
//        QuestionModel[] questions1 = new QuestionModel[ls.size()];
//        ls.toArray(questions1);

//        QuestionModel[] questions1 = new QuestionModel[data1.size()];
//        data1.toArray(questions1);
//        for(int i = 0; i < questions1.length; i ++) {
//            Log.i(TAG + " : questions1[i]", questions1[i].toString());
//        }


       // Log.i(TAG,questions[2].getQuestionText());
//        questions[0] = new Question();
//        questions[1] = new Question();
//        questions[2] = new Question();
//        questions[3] = new Question();
       //  adapter = new QuestionsAdapter(this, questions);
      //  QuestionModel[] q = new QuestionModel[questionList.size()];
       // q = questionList.toArray(q);
//        Log.i(TAG, "" + q.length);
//        for(int i = 0; i <q.length; i++){
//            Log.i("TAG", q[i].toString());
//        }

//        adapter = new QuestionsAdapter(this, questions1);
//
//        questionListview.setAdapter(adapter);


//        Log.i(TAG,mDatabase.toString());
//        Log.i("Database Refffffff", "Hello1");


        String myUserId = mFirebaseUser.getUid();
//        questions[0].setQuestionText("Question from Java");


//         mDatabase.child("users").child("BLhPQjoeATfJUJVWjel4u35k3O33").
//                child("classRooms").child("-KTUij2r-dyM5VVS8FQY").child("quizzes").child("-KTUj3fbboFRh2MX9KWZ")
//                .child("questions")
//                .orderByChild("question").addListenerForSingleValueEvent(valueEventListener);
      //  Log.i("Database Refffffff", "Hello2");
       // Log.i("Database Refffffff", mDatabase.toString());

//        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
//            // TODO: implement the ChildEventListener methods as documented above
//            // ...
//        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.i("TAG","Iiiinnnnssssidddddeeee onDataChangeSnaphost");
            if(dataSnapshot.getChildrenCount() == 0){
                Toast.makeText(getApplicationContext(),"Empty Quiz",Toast.LENGTH_SHORT).show();
                return;
            }
            for (DataSnapshot questionsSnapshot : dataSnapshot.getChildren()) {
                Log.i("INFO", questionsSnapshot.toString()+ " KEY "+ questionsSnapshot.getKey());
                Log.i(TAG,"Afterrr the log inside for loop");

                QuestionModel queston = questionsSnapshot.getValue(QuestionModel.class);
                QuestionModel question1 = questionsSnapshot.getValue(QuestionModel.class);
                Log.i(TAG,"After question is initializes inside the for loop");
                Log.i(TAG, "" + questionsSnapshot.getKey().charAt(1));
                char i  =  questionsSnapshot.getKey().charAt(1);
                Log.i("TAG of i", ""+ i);

                   // String[] cho = {question1.getChoices().get("A"), question1.getChoices().get("B"), question1.getChoices().get("C")};
                    //questions[i] = new Question(question1.getQuestion(), 2, cho, question1.getAnswer());
                    Log.i(TAG, "Starting toString....");
                    Log.i(TAG, question1.getQuestion());
                    Log.i(TAG, question1.getAnswer());
                    Log.i(TAG, question1.getChoices().toString());
                    Log.i(TAG, "Finishing toString....");


//                bgl.setBgl_key(messageSnapshot.getKey());
               questionList.add(queston);
                Log.i(TAG,"Before updateListZView() is called");
                updateListView();

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void updateListView(){
        Log.i("TAG","Inside update list view");
         adapter1.notifyDataSetChanged();
         questionListview.invalidate();
    }
}
