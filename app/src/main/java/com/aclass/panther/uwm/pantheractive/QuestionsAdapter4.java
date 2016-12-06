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

public class QuestionsAdapter4 extends ArrayAdapter<QuestionModel> {
    Question[] questions = null;
    QuestionModel[] questions1 = null;

    Context context;

    TextView questionNumber;
    TextView questionText;
    TextView choiceA, choiceB, choiceC, choiceD, choiceE, choiceF;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;

    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private SharedPreferences mSharedPreferences;

    QuestionModel question;


    public QuestionsAdapter4(Context context, QuestionModel[] resource) {
        super(context, R.layout.question, resource);
        this.context = context;
        this.questions1 = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.question, parent, false);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(160, 198, 225));
        } else {
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

        final TextView correctAnsText = (TextView) convertView.findViewById(R.id.correctAnswer);


        final Button choiceA_btn = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        final Button choiceB_btn = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        final Button choiceC_btn = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        final Button choiceD_btn = (Button) convertView.findViewById(R.id.choiceD_textBtn);
        final Button choiceE_btn = (Button) convertView.findViewById(R.id.choiceE_textBtn);
        final Button choiceF_btn = (Button) convertView.findViewById(R.id.choiceF_textBtn);


        choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
        choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
        choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
        choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
        choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
        choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));


        choiceA_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceA_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: A");


            }
        });

        choiceB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choiceB_btn.setText("This text has been changed");
                choiceB_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: B");


            }
        });
        choiceC_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceC_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: C");


            }
        });


        choiceD_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceD_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: D");

            }
        });

        choiceE_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceE_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceF_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: E");
            }
        });
        choiceF_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceF_btn.setBackgroundColor(Color.argb(255, 51, 102, 255));
                choiceE_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceD_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceB_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255, 204, 204, 204));
                correctAnsText.setText("You selected: F");
            }
        });

        TextView qnNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        qnNumber.setText("# " + (position + 1));

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
        if (questions1[position].getChoices() != null) {
            if (questions1[position].getChoices().get("A") != null) {
                choiceA.setText(questions1[position].getChoices().get("A").toString());
            }
            if (questions1[position].getChoices().get("B") != null) {
                choiceB.setText(questions1[position].getChoices().get("B").toString());
            }
            if (questions1[position].getChoices().get("C") != null) {
                choiceC.setText(questions1[position].getChoices().get("C").toString());
                choiceC.setVisibility(choiceC.VISIBLE);
                choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
            }
            if (questions1[position].getChoices().get("D") != null) {
                choiceD.setText(questions1[position].getChoices().get("D").toString());
                choiceD.setVisibility(choiceD.VISIBLE);
                choiceDLabel.setVisibility(choiceDLabel.VISIBLE);
            }
            if (questions1[position].getChoices().get("E") != null) {
                choiceE.setText(questions1[position].getChoices().get("E").toString());
                choiceE.setVisibility(choiceE.VISIBLE);
                choiceELabel.setVisibility(choiceELabel.VISIBLE);
            }
            if (questions1[position].getChoices().get("F") != null) {
                choiceF.setText(questions1[position].getChoices().get("F").toString());
                choiceF.setVisibility(choiceF.VISIBLE);
                choiceFLabel.setVisibility(choiceFLabel.VISIBLE);
            }

        }

        return convertView;
    }

}


