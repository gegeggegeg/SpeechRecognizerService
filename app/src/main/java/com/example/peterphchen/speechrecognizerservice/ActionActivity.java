package com.example.peterphchen.speechrecognizerservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActionActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ActionActivity";
    private Button recordBtn;
    private Button positionBtn;
    private boolean keep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        recordBtn = findViewById(R.id.record);
        positionBtn = findViewById(R.id.position);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        keep = intent.getExtras().getBoolean("Keep");
        Log.d(TAG, "onNewIntent: keep value: "+keep);
        if(!keep){
            Log.d(TAG, "onNewIntent: finish() is called");
            ActionActivity.this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.position:
                Log.d(TAG, "onClick: postion btn is pressed");
                break;
            case R.id.record:
                Log.d(TAG, "onClick: record btn is pressed");
                break;
        }
    }
}
