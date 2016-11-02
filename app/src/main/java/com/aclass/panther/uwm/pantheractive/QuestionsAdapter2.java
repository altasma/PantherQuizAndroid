package com.aclass.panther.uwm.pantheractive;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asmamaw on 10/26/16.
 */

public class QuestionsAdapter2 implements ListAdapter {
    ArrayList<QuestionModel> questions = null;
    Context context;

    TextView questionNumber;

    TextView questionText;
    TextView choiceA,choiceB,choiceC,correctAnsText;
    //  Button choiceA_btn,choiceB_btn, choiceC_btn;


    public QuestionsAdapter2(Context context, ArrayList<QuestionModel> resource) {
        this.context = context;
        this.questions = resource;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.question, parent, false);

        if(position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(160, 198, 225));
        }
        else{
            convertView.setBackgroundColor(Color.rgb(100, 162, 206));

        }

        questionText = (TextView) convertView.findViewById(R.id.questionText);
        questionNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        choiceA = (TextView) convertView.findViewById(R.id.choiceA);
        choiceB = (TextView) convertView.findViewById(R.id.choiceB);
        choiceC = (TextView) convertView.findViewById(R.id.choiceC);




        final Button choiceA_btn = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        final Button  choiceB_btn = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        final Button  choiceC_btn = (Button) convertView.findViewById(R.id.choiceC_textBtn);



        choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
        choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));




        choiceA_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceA_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });

        choiceB_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceB_btn.setText("This text has been changed");
                choiceB_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceC_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });
        choiceC_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceC_btn.setBackgroundColor(Color.argb(255,51, 102, 255));
                choiceB_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
                choiceA_btn.setBackgroundColor(Color.argb(255,204, 204, 204));
//                choiceD.setBackgroundColor(Color.argb(255,245,245,245));


            }
        });

        TextView qnNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        qnNumber.setText("# " + (position +1) );

        TextView questionView = (TextView) convertView.findViewById(R.id.questionText);
        Button choiceA = (Button) convertView.findViewById(R.id.choiceA_textBtn);
        Button choiceB = (Button) convertView.findViewById(R.id.choiceB_textBtn);
        Button choiceC = (Button) convertView.findViewById(R.id.choiceC_textBtn);
        TextView choiceCLabel = (TextView) convertView.findViewById(R.id.choiceC);


//        if(this.questions[position].getQuestionText() != null){
//            questionView.setText(this.questions[position].getQuestionText());
//
//            choiceA.setText(this.questions[position].getChoices()[0].toString());
//            choiceB.setText(this.questions[position].getChoices()[1].toString());
//            choiceC.setVisibility(choiceC.VISIBLE);
//            choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
//
//            Log.i("Inside Question Adapter", "Question text was not null");
//        }
//        else {
//            questionView.setText(" And This is set from adapaters class");
//
//        }
        for(QuestionModel a : questions){
            if(a.getQuestion() != null){
                questionView.setText(a.getQuestion());
            }
        }

//        if(this.questions[position].getQuestionText() != null){
//            questionView.setText(this.questions[position].getQuestionText());
//
//            choiceA.setText(this.questions[position].getChoices()[0].toString());
//            choiceB.setText(this.questions[position].getChoices()[1].toString());
//            choiceC.setVisibility(choiceC.VISIBLE);
//            choiceCLabel.setVisibility(choiceCLabel.VISIBLE);
//
//            Log.i("Inside Question Adapter", "Question text was not null");
//        }
//        else {
//            questionView.setText(" And This is set from adapaters class");
//
//        }


        return convertView;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}


