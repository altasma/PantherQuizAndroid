package com.aclass.panther.uwm.pantheractive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asmamaw on 10/26/16.
 */

public class QuestionsAdapter extends ArrayAdapter<QuestionModel> {
    Question[] questions = null;
    QuestionModel[] questions1 = null;

    Context context;

    TextView questionNumber;

    TextView questionText;
    TextView choiceA,choiceB,choiceC, choiceD,choiceE,choiceF;
 //  Button choiceA_btn,choiceB_btn, choiceC_btn;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;

    QuestionModel question ;



    public QuestionsAdapter(Context context, QuestionModel[] resource) {
        super(context, R.layout.question, resource);
        this.context = context;
        this.questions1 = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.question, parent, false);
//        convertView.setBottom(10);
//        convertView.setHorizontalFadingEdgeEnabled(true);
        if(position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(160, 198, 225));
        }
        else{
            convertView.setBackgroundColor(Color.rgb(100, 162, 206));

        }

        question = new QuestionModel();
        final List data1 = new ArrayList<QuestionModel>();


        questionText = (TextView) convertView.findViewById(R.id.questionText);
        questionNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        choiceA = (TextView) convertView.findViewById(R.id.choiceA);
        choiceB = (TextView) convertView.findViewById(R.id.choiceB);
        choiceC = (TextView) convertView.findViewById(R.id.choiceC);
        choiceD = (TextView) convertView.findViewById(R.id.choiceD);
        choiceE = (TextView) convertView.findViewById(R.id.choiceE);
        choiceF = (TextView) convertView.findViewById(R.id.choiceF);



        final TextView  correctAnsText = (TextView) convertView.findViewById(R.id.correctAnswer);




       final Button choiceA_btn = (Button) convertView.findViewById(R.id.choiceA_textBtn);
       final Button  choiceB_btn = (Button) convertView.findViewById(R.id.choiceB_textBtn);
       final Button  choiceC_btn = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        final Button choiceD_btn = (Button) convertView.findViewById(R.id.choiceD_textBtn);
        final Button  choiceE_btn = (Button) convertView.findViewById(R.id.choiceE_textBtn);
        final Button  choiceF_btn = (Button) convertView.findViewById(R.id.choiceF_textBtn);

//        final TextView  choiceD = (TextView) convertView.findViewById(R.id.choiceD);


        choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
//        choiceD.setBackgroundColor(Color.argb(255,245,245,245));




//        TextView name = (TextView) convertView.findViewById(R.id.textView1);
//        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
//        final Button trueBtn = (Button) convertView.findViewById(R.id.trueBtn);
//        final Button  falseBtn = (Button)convertView.findViewById(R.id.falseBtn);

//        choiceD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choiceD.setBackgroundColor(Color.argb(255,51, 102, 255));
//                choiceA_btn.setBackgroundColor(Color.argb(255,245,245,245));
//                choiceB_btn.setBackgroundColor(Color.argb(255,245,245,245));
//                choiceC_btn.setBackgroundColor(Color.argb(255,245,245,245));
//
//            }
//        });

        choiceA_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceA_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: A");
//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });

        choiceB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choiceB_btn.setText("This text has been changed");
                choiceB_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: B");

//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });
        choiceC_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceC_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: C");

//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });


        choiceD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceD_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: D");

//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));
            }
        });

        choiceE_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceE_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceF_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: D");

//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));
            }
        });
        choiceF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceF_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceE_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                correctAnsText.setText("You selected: D");

//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));
            }
        });



//        falseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                falseBtn.setBackgroundColor(Color.argb(255,7,132,7));
//                trueBtn.setBackgroundColor(Color.argb(255,171,197,236));
//            }
//        });
//        EditText et = (EditText) convertView.findViewById(R.id.editText1);
        TextView qnNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        qnNumber.setText("# " + (position +1) );

        TextView questionView = (TextView) convertView.findViewById(R.id.questionText);
        Button choiceA = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        Button choiceB = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        Button choiceC = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        Button choiceD = (Button) convertView.findViewById(R.id.choiceD_textBtn);
        Button choiceE = (Button) convertView.findViewById(R.id.choiceE_textBtn);
        Button choiceF = (Button) convertView.findViewById(R.id.choiceF_textBtn);




        TextView choiceCLabel = (TextView) convertView.findViewById(R.id.choiceC);
        TextView choiceDLabel = (TextView) convertView.findViewById(R.id.choiceD);
        TextView choiceELabel = (TextView) convertView.findViewById(R.id.choiceE);
        TextView choiceFLabel = (TextView) convertView.findViewById(R.id.choiceF);


        questionView.setText(" " + questions1[position].getQuestion());
        if(questions1[position].getChoices()!= null){
            if(questions1[position].getChoices().get("A")!= null){
                choiceA.setText(questions1[position].getChoices().get("A").toString());
            }
            if(questions1[position].getChoices().get("B")!= null){
                choiceB.setText(questions1[position].getChoices().get("B").toString());
            }
            if(questions1[position].getChoices().get("C")!= null){
                choiceC.setText(questions1[position].getChoices().get("C").toString());
                choiceC.setVisibility(choiceC.VISIBLE);
               choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
            }
            if(questions1[position].getChoices().get("D")!= null){
                choiceD.setText(questions1[position].getChoices().get("D").toString());
                choiceD.setVisibility(choiceD.VISIBLE);
                choiceDLabel.setVisibility(choiceDLabel.VISIBLE);
            }
            if(questions1[position].getChoices().get("E")!= null){
                choiceE.setText(questions1[position].getChoices().get("E").toString());
                choiceE.setVisibility(choiceE.VISIBLE);
                choiceELabel.setVisibility(choiceELabel.VISIBLE);
            }
            if(questions1[position].getChoices().get("F")!= null){
                choiceF.setText(questions1[position].getChoices().get("F").toString());
                choiceF.setVisibility(choiceF.VISIBLE);
                choiceFLabel.setVisibility(choiceFLabel.VISIBLE);
            }

        }


//        if(this.questions[1].getQuestionText() != null){
//            questionView.setText(this.questions[position].getQuestionText());
//
//            choiceA.setText(this.questions[1].getChoices()[0].toString());
//            choiceB.setText(this.questions[1].getChoices()[1].toString());
//            choiceC.setVisibility(choiceC.VISIBLE);
//            choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
//
//            Log.i("Inside Question Adapter", "Question text was not null");
//        }
//        else {
//            questionView.setText(" And This is set from adapaters class");
//            //Log.i("Inside Question Adapter", "Question text was  null");
//
//
//        }


//        Log.i("for loop out ",questions1[0].toString());
//
//        for(int i = 0; i < questions1.length;i++){
//            int k = position+1;
//            Log.i("for loop",questions1[i].toString());
//            questionView.setText(questions1[i].getQuestion().toString());
////            if(questions1[i].getChoices().get("A")!= null){
////                Log.i("Key of A: ", questions1[i].getChoices().get("A").toString());
////                choiceA.setText(questions1[i].getChoices().get("A").toString());
////
////            }
////            else{
////                choiceA.setText(questions1[i].getChoices().toString());
////
////            }
////            if(questions1[i].getChoices().get("B")!= null){
////                choiceB.setText(questions1[i].getChoices().get("B").toString());
////            }
////            else {
////                choiceB.setText(questions1[i].getChoices().toString());
////
////            }
//
//        }




//        for(int i =0; i < questions.length;i++){
//            int k = position + 1;
//            choiceC.setVisibility(choiceC.VISIBLE);
//            choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
//            questionView.setText(" Question text of question " + k);
//            choiceA.setText("Choice A of question " + k);
//            choiceB.setText("Choice B of question " + k);
//            choiceC.setText("Choice C of question " + k);
//
//
//        }



//        name.setText(questions[position].getName());
//        et.setText(questions[position].getTextEdit());
//        if (questions[position].getValue() == 1)
//            cb.setChecked(true);
//        else
//            cb.setChecked(false);
        return convertView;
    }

//    @Override
//    public void onClick(View v) {
////        if(v == trueBtn){
////            trueBtn.setBackgroundColor(Color.GREEN);
////            falseBtn.setBackgroundColor(Color.CYAN);
////        }
////        else if(v == falseBtn){
////           falseBtn.setBackgroundColor(Color.GREEN);
////            trueBtn.setBackgroundColor(Color.CYAN);
////        }
////        else{
////
////        }
//    }
}


