package com.example.peterphchen.speechrecognizerservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class InformationAdapter extends RecyclerView.Adapter<InformationHolder> {
    private Context context;
    private static final String TAG = "InformationAdapter";
    private ArrayList<CurserData> curserData;

    public InformationAdapter(Context context) {
        super();
        String[] projection = {LocationContract.ID,LocationContract.LOCATION,LocationContract.TIME,LocationContract.PHONE_NUMBER,LocationContract.GOOGLEMAP_URL};
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        //Cursor cursor = database.query(LocationContract.TABLE_NAME,projection,null,null
                //,null,null,LocationContract.ID);
        Cursor cursor = context.getContentResolver().query(LocationContract.CONTENT_URI,projection,null,
        null,LocationContract.ID);
        this.context=context;
        curserData = new ArrayList<>();
        while (cursor.moveToNext()){
            curserData.add(new CurserData(cursor.getString(1),cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getInt(0)));

        }
        cursor.close();
    }

    @NonNull
    @Override
    public InformationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new InformationHolder(inflater.inflate(R.layout.holder_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final InformationHolder informationHolder, final int position) {
        informationHolder.setTextLocation(curserData.get(position).getAddrees());
        informationHolder.setTextTime(curserData.get(position).getDate());
        informationHolder.setTextPhoneNumber(curserData.get(position).getNumber());
        informationHolder.getTextTime().setClickable(false);
        informationHolder.getTextLocation().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(view.getContext());
                dialogbuilder.setMessage("Do you want to open googlemap ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(curserData.get(position).getUrl()));
                        context.startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                }).show();
            }
        });
        informationHolder.getTextPhoneNumber().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(view.getContext());
                dialogbuilder.setMessage("Do you want to call this number ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + curserData.get(position).getNumber()));
                        try {
                            context.startActivity(intent);
                        }catch (SecurityException e){
                            Log.e(TAG, "onClick: "+e.getMessage());
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                }).show();
            }
        });
        informationHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(view.getContext());
                dialogbuilder.setMessage("Do you want to delete this item?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.getContentResolver().delete(LocationContract.CONTENT_URI,LocationContract.ID+"=?",
                                new String[]{String.valueOf(curserData.get(position).getId())});
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        curserData.remove(position);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return curserData.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull InformationHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

}
