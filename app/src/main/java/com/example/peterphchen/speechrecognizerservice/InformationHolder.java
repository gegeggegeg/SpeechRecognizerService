package com.example.peterphchen.speechrecognizerservice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class InformationHolder extends RecyclerView.ViewHolder {

    private  TextView textLocation;
    private  TextView textPhoneNumber;
    private  TextView textTime;
    public InformationHolder(@NonNull final View itemView) {
        super(itemView);
        textLocation = itemView.findViewById(R.id.textLocation);
        textTime = itemView.findViewById(R.id.textTime);
        textPhoneNumber = itemView.findViewById(R.id.textPhoneNumber);
    }
    public void setTextLocation(String location){
        textLocation.setText(location);
    }
    public void setTextTime(String time){
        textTime.setText(time);
    }
    public void setTextPhoneNumber(String phoneNumber){
        textPhoneNumber.setText(phoneNumber);
    }

    public TextView getTextLocation() {
        return textLocation;
    }

    public TextView getTextPhoneNumber() {
        return textPhoneNumber;
    }

    public TextView getTextTime() {
        return textTime;
    }
}
