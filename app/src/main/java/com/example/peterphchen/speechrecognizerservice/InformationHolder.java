package com.example.peterphchen.speechrecognizerservice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class InformationHolder extends RecyclerView.ViewHolder {

    private  TextView textLocation;
    private  TextView textPhoneNumber;
    private  TextView textTime;
    public InformationHolder(@NonNull View itemView) {
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
}
