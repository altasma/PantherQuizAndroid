package com.aclass.panther.uwm.pantheractive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudentHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_student_home);
    }
}
