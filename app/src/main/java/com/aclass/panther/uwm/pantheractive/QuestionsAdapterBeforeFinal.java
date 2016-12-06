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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Asmamaw on 10/26/16.
 */

public class QuestionsAdapterBeforeFinal extends ArrayAdapter<AnsweredQuestionModel> {
    Question[] questions = null;
    AnsweredQuestionModel[] answeredQuestions1 = null;

    Context context;

    TextView questionNumber;
    TextView questionText;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;

    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private SharedPreferences mSharedPreferences;

    AnsweredQuestionModel question;
    AnsweredQuestionModel answeredQuestion;

    private Map<Integer, AnsweredQuestionModel> answeredLists;  //map of question number as key, and question as model


    public QuestionsAdapterBeforeFinal(Context context, AnsweredQuestionModel[] resource) {
        super(context, R.layout.question, resource);
        this.context = context;
        this.answeredQuestions1 = resource;
        answeredLists = new HashMap<>();
        for (int i = 0; i < resource.length; i++) {
            answeredLists.put(i + 1, resource[i]);

        }
        //  Log.i("answeredLists.lengthOut", answeredLists.size()+ "");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.question, parent, false);
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(160, 198, 225));
        } else {
            convertView.setBackgroundColor(Color.rgb(100, 162, 206));

        }

        question = new AnsweredQuestionModel();
        final List data1 = new ArrayList<AnsweredQuestionModel>();


        //Label textViews
        questionText = (TextView) convertView.findViewById(R.id.questionText);
        questionNumber = (TextView) convertView.findViewById(R.id.questionNumber);

        final TextView correctAnsText = (TextView) convertView.findViewById(R.id.correctAnswer);

        final Button choiceA_btn = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        choiceA_btn.setTag(position);
        final Button choiceB_btn = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        final Button choiceC_btn = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        final Button choiceD_btn = (Button) convertView.findViewById(R.id.choiceD_textBtn);
        final Button choiceE_btn = (Button) convertView.findViewById(R.id.choiceE_textBtn);
        final Button choiceF_btn = (Button) convertView.findViewById(R.id.choiceF_textBtn);

        TextView qnNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        qnNumber.setText("# " + (position + 1));

        TextView questionView = (TextView) convertView.findViewById(R.id.questionText);
        final Button choiceA = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        Button choiceB = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        Button choiceC = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        Button choiceD = (Button) convertView.findViewById(R.id.choiceD_textBtn);
        Button choiceE = (Button) convertView.findViewById(R.id.choiceE_textBtn);
        Button choiceF = (Button) convertView.findViewById(R.id.choiceF_textBtn);


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

                answeredLists.get(position + 1).setStudentAnswer("A");
                // Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());


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

                answeredLists.get(position + 1).setStudentAnswer("B");
                //Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());


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

                answeredLists.get(position + 1).setStudentAnswer("C");
                //  Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());


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

                answeredLists.get(position + 1).setStudentAnswer("D");

                // Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());


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

                answeredLists.get(position + 1).setStudentAnswer("E");

                //Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());

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

                answeredLists.get(position + 1).setStudentAnswer("F");

                //   Log.i("answered for "+ position+1, answeredLists.get(position+1).getStudentAnswer());

            }
        });


        TextView choiceCLabel = (TextView) convertView.findViewById(R.id.choiceC);
        TextView choiceDLabel = (TextView) convertView.findViewById(R.id.choiceD);
        TextView choiceELabel = (TextView) convertView.findViewById(R.id.choiceE);
        TextView choiceFLabel = (TextView) convertView.findViewById(R.id.choiceF);


        questionView.setText(" " + answeredQuestions1[position].getQuestion());

        if (answeredQuestions1[position].getChoices() != null) {
            String studAns = answeredLists.get(position + 1).getStudentAnswer();
            if (answeredQuestions1[position].getChoices().get("A") != null) {
                choiceA.setText(answeredQuestions1[position].getChoices().get("A").toString());
                if (studAns != null && studAns.equals("A")) {
                    choiceA.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "A");

                }
            }
            if (answeredQuestions1[position].getChoices().get("B") != null) {
                choiceB.setText(answeredQuestions1[position].getChoices().get("B").toString());
                if (studAns != null && studAns.equals("B")) {
                    choiceB.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "B");

                }
            }
            if (answeredQuestions1[position].getChoices().get("C") != null) {
                choiceC.setText(answeredQuestions1[position].getChoices().get("C").toString());
                choiceC.setVisibility(choiceC.VISIBLE);
                choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
                if (studAns != null && studAns.equals("C")) {
                    choiceC.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "C");

                }
            }
            if (answeredQuestions1[position].getChoices().get("D") != null) {
                choiceD.setText(answeredQuestions1[position].getChoices().get("D").toString());
                choiceD.setVisibility(choiceD.VISIBLE);
                choiceDLabel.setVisibility(choiceDLabel.VISIBLE);
                if (studAns != null && studAns.equals("D")) {
                    choiceD.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "D");

                }
            }
            if (answeredQuestions1[position].getChoices().get("E") != null) {
                choiceE.setText(answeredQuestions1[position].getChoices().get("E").toString());
                choiceE.setVisibility(choiceE.VISIBLE);
                choiceELabel.setVisibility(choiceELabel.VISIBLE);
                if (studAns != null && studAns.equals("E")) {
                    choiceE.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "E");

                }
            }
            if (answeredQuestions1[position].getChoices().get("F") != null) {
                choiceF.setText(answeredQuestions1[position].getChoices().get("F").toString());
                choiceF.setVisibility(choiceF.VISIBLE);
                choiceFLabel.setVisibility(choiceFLabel.VISIBLE);
                if (studAns != null && studAns.equals("F")) {
                    choiceF.setBackgroundColor(Color.argb(255, 51, 102, 255));
                    correctAnsText.setText("You selected: " + "F");

                }
            }

        }

        return convertView;
    }

}


