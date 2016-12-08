package com.aclass.panther.uwm.pantherquiz;

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
import java.util.List;

/**
 * Created by Asmamaw on 10/26/16.
 */

public class ClassListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String TAG = "PantherQuiz ClassList Activity log";

    private GoogleApiClient mGoogleApiClient;

    public static final String ANONYMOUS = "anonymous";

    private String mUsername;
    private String userEmail;
    private String formattedEmail;
    private SharedPreferences mSharedPreferences;

    final List data = new ArrayList<String>();
    final List data1 = new ArrayList<ClassRoomModel>();
    final List<ClassRoomModel> classList = new ArrayList<ClassRoomModel>();


    ClassRoomModel classRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_icon_tab);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_class_list);


        classRoom = new ClassRoomModel();
        classRoom.setName("--Select a Class--"); //place holder for index 0,


        data1.add(classRoom);  //dummy classRoom for displaying select message
        classList.add(classRoom);


        Spinner spinner = (Spinner) findViewById(R.id.classList_spinner);
        spinner.setOnItemSelectedListener(this);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, data1);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userEmail = mFirebaseUser.getEmail();

        String[] splitedEmail;
        String[] splitedEmail2;
        if (userEmail != null) {
            splitedEmail = userEmail.split("@");
//            Log.i("SplitedEmail", splitedEmail[0].toString() + ", "+ splitedEmail[1].toString());
            splitedEmail2 = splitedEmail[1].split("\\.");
//            Log.i("Legth", splitedEmail2.length + "");
            if (splitedEmail2.length > 0) {
//                Log.i("splitedEmail2", splitedEmail2[0].toString() + ", " + splitedEmail2[0].toString());
            }
            formattedEmail = splitedEmail[0] + "-" + splitedEmail2[0] + "dot" + splitedEmail2[1]; //used as a child of studentList
//            Log.i("formattedEmail", formattedEmail);
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference();

        database2.child("studentsList/").child(formattedEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot classDataSnapShot : dataSnapshot.getChildren()) {
                    data1.clear();
                    classList.clear();
                    data1.add(classRoom); //Reading the dummy classRoom
                    classList.add(classRoom);
                    ClassRoomModel q1 = new ClassRoomModel();

                    for (DataSnapshot snap : classDataSnapShot.getChildren()) {
                        try {
                            q1 = new ClassRoomModel();

                            q1.setName(snap.child("name").getValue().toString());
                            q1.setId(snap.child("classId").getValue().toString());
                            q1.setDepartment(snap.child("department").getValue().toString());
                            q1.setSchool(snap.child("school").getValue().toString());

                        } catch (Exception e) {
                            Log.i("Exception:", e.toString());
                        }
                        data1.add(q1);
                        classList.add(q1);

                    }

                    try {
                        /*
                        Log.i("DataSnapShotString: ", dataSnapshot.toString());
                        Log.i("DataSnapShotValue: ", dataSnapshot.getValue().toString());
                        Log.i("DataSnapShotChildre: ", dataSnapshot.getChildren().iterator().next().toString());


                        Log.i("dsChildred", classDataSnapShot.getChildren().iterator().next().getValue().toString());
                        Log.i("DSP get value: ", classDataSnapShot.getValue().toString());
                        */
                        DatabaseReference ref = classDataSnapShot.getRef();
                        Log.i("Question db:", ref.child("classes").getDatabase().toString());

                    } catch (Exception e) {
                        Log.i("Exception", e.getMessage());
                    }

                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignUpActivity.class));
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


    }

    private int mLastSpinnerPosotion = 0;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mLastSpinnerPosotion == i) {
            return;
        }
        mLastSpinnerPosotion = i;
//        Log.i("Data1 at" + i  , data1.get(i).toString());

        Intent listViewIntent = new Intent(this, QuizListActivity.class);
        listViewIntent.putExtra("EXTRA_CLASS_NAME", classList.get(i).getName());
//        Log.i("EXTRA_CLASS_NAME", classList.get(i).getName());
//        Log.i("EXTRA_CLASS_ID", "" + classList.get(i).getId());

        ClassRoomModel cm = classList.get(i);
//        Log.i("classList.size "+ i, classList.size() + "");
//        Log.i("cm:", cm.toString());
        String classId = cm.getId();
        listViewIntent.putExtra("EXTRA_CLASS_ID", classList.get(i).getId().toString());
//        Log.i("EXTRA_CLASS_ID", classId);
        mLastSpinnerPosotion = 0;
        startActivity(listViewIntent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                Intent intent = new Intent(this, SignUpActivity.class);
                if (Build.VERSION.SDK_INT >= 11) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
                return true;
            case R.id.user_settings:
                Intent settingsIntent = new Intent(getApplicationContext(), UserSettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.help_menu:
                Intent helpIntent = new Intent(getApplicationContext(), UserManualActvity.class);
                startActivity(helpIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast toast = Toast.makeText(getApplicationContext(), "Connection Failed, unable to Authenticate", Toast.LENGTH_SHORT);
        toast.show();

    }
}
