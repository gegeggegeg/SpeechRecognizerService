package com.peterchen.WheresCall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final static int OVERLAY_PERMISSION_CODE = 999;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        addOverlay();
        if(isDatabaseEmpty()){
            Log.d(TAG, "onCreate: Database is Empty, set special layout");
            setContentView(R.layout.empty_main);
        }else {
            setContentView(R.layout.activity_main);
            recyclerView = findViewById(R.id.RecyclerViewMain);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new InformationAdapter(this));
        }
    }

    private boolean isDatabaseEmpty(){
        String[] projection = {LocationContract.ID,LocationContract.LOCATION, LocationContract.PHONE_NUMBER};
        Cursor cursor = getContentResolver().query(LocationContract.CONTENT_URI,projection,
                null,null,LocationContract.ID);
        if(!cursor.moveToFirst())
            return true;
        else
            return false;
    }

    private void requestPermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Settings.ACTION_MANAGE_OVERLAY_PERMISSION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    ,Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.CALL_PHONE, Manifest.permission.FOREGROUND_SERVICE}
                    ,999);
        }
    }

    public void addOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "ACTION_MANAGE_OVERLAY_PERMISSION Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                databaseHelper.onUpgrade(database,1,2);
                database.close();
                if(isDatabaseEmpty())
                    setContentView(R.layout.empty_main);
                return true;
            case R.id.about:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.update:
                if(isDatabaseEmpty()){
                    Log.d(TAG, "onCreate: Database is Empty, set special layout");
                    setContentView(R.layout.empty_main);
                }else {
                    setContentView(R.layout.activity_main);
                    recyclerView = findViewById(R.id.RecyclerViewMain);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.setAdapter(new InformationAdapter(this));
                }
                return true;
            default:
                throw new IllegalArgumentException("wrong argument");
        }
    }
}