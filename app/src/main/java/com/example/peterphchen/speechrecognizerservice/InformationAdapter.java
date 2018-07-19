package com.example.peterphchen.speechrecognizerservice;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class InformationAdapter extends RecyclerView.Adapter<InformationHolder> {
    private Cursor cursor;
    private Context context;

    public InformationAdapter(Context context,Cursor cursor) {
        super();
        this.context=context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public InformationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new InformationHolder(inflater.inflate(R.layout.holder_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformationHolder informationHolder, int position) {
        cursor.moveToPosition(position);
        informationHolder.setTextLocation(cursor.getString(1));
        informationHolder.setTextTime(cursor.getString(2));
        informationHolder.setTextPhoneNumber(cursor.getString(3));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull InformationHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

}
