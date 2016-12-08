package com.aclass.panther.uwm.pantherquiz;

/**
 * Created by Asmamaw on 9/15/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Model> {
    Model[] modelItems = null;
    Context context;

    TextView questionNumber;

    public CustomAdapter(Context context, Model[] resource) {
        super(context, R.layout.row, resource);
        this.context = context;
        this.modelItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);

        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.rgb(255, 230, 204));
        } else {
            convertView.setBackgroundColor(Color.rgb(255, 156, 51)
            );

        }
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        final Button trueBtn = (Button) convertView.findViewById(R.id.trueBtn);
        final Button falseBtn = (Button) convertView.findViewById(R.id.falseBtn);
        trueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trueBtn.setBackgroundColor(Color.argb(255, 7, 132, 7));
                falseBtn.setBackgroundColor(Color.argb(255, 171, 197, 236));
            }
        });
        falseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                falseBtn.setBackgroundColor(Color.argb(255, 7, 132, 7));
                trueBtn.setBackgroundColor(Color.argb(255, 171, 197, 236));
            }
        });
        EditText et = (EditText) convertView.findViewById(R.id.editText1);
        TextView qnNumber = (TextView) convertView.findViewById(R.id.questionNumber);
        qnNumber.setText("# " + (position + 1));
        name.setText(modelItems[position].getName());
        et.setText(modelItems[position].getTextEdit());
        if (modelItems[position].getValue() == 1)
            cb.setChecked(true);
        else
            cb.setChecked(false);
        return convertView;
    }

}
