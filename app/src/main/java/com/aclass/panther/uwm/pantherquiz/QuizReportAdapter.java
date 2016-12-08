package com.aclass.panther.uwm.pantherquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class QuizReportAdapter extends ArrayAdapter<QuizReportModel> {
    Question[] questions = null;
    QuizReportModel[] quizLists = null;
    List<String> extras;

    Context context;

    TextView quizIdLabel, scoreLabel, classAverageLabel, viewDetailLabel, rollNumberText;

    TextView quizIdTex, scoreText, classAverageText;
    Button viewDetailButton;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;


    private String TAG = "PantherQuiz QuizReportAdapter class log";

    private GoogleApiClient mGoogleApiClient;
    public static final String ANONYMOUS = "anonymous";
    private SharedPreferences mSharedPreferences;

    QuizReportModel quiz;
    QuizReportModel quiz2;
    private Map<Integer, QuizReportModel> quizReportLists;  //map of question number as key, and question as model


    public QuizReportAdapter(Context context, QuizReportModel[] resource, List<String> extras) {
        super(context, R.layout.quiz_report, resource);
        this.extras = extras;
        this.context = context;
        this.quizLists = resource;
        quizReportLists = new HashMap<>();
        for (int i = 0; i < resource.length; i++) {
            quizReportLists.put(i + 1, resource[i]);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.quiz_report, parent, false);

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(160, 198, 225));
        } else {
            convertView.setBackgroundColor(Color.rgb(100, 162, 206));

        }

        quiz = new QuizReportModel();
        final List data1 = new ArrayList<QuizReportModel>();


        //Label textViews
        rollNumberText = (TextView) convertView.findViewById(R.id.rollNumberText);
        quizIdTex = (TextView) convertView.findViewById(R.id.quizIdText);
        scoreText = (TextView) convertView.findViewById(R.id.scoreTex);
        classAverageText = (TextView) convertView.findViewById(R.id.classAverageText);

        if (quizLists.length > 0) {
            // Log.i("quizList["+  position+ "]", quizLists[position].getQuizId().toString());
            quizIdTex.setText(quizLists[position].getQuizId().toString());
            scoreText.setText(quizLists[position].getScore().toString());
            rollNumberText.setText("" + (position + 1) + " ");
        }


        final Button detailButton = (Button) convertView.findViewById(R.id.detailButton);
        detailButton.setTag(position);


        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizKey = quizLists[position].getQuizKey();

                Intent detailIntent = new Intent(context, QuizQuestionsReportActivity.class);
                detailIntent.putExtra("EXTRA_CLASS_ID", extras.get(1));
                detailIntent.putExtra("EXTRA_CLASS_NAME", extras.get(0));
                detailIntent.putExtra("EXTRA_QUIZ_KEY", quizKey);
                context.startActivity(detailIntent);
                // Log.i("Insode QuizReportAdapter viewDetail Onclick Listener:", "clicked");
            }
        });


        return convertView;
    }

}


