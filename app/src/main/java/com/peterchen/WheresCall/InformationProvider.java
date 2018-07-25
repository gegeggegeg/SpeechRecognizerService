package com.peterchen.WheresCall;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class InformationProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final int INFORMATION_LIST = 101;
    private final int INFORMATION_ITEM = 102;
    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        uriMatcher.addURI(LocationContract.CONTENT_AUTHORITY,LocationContract.TABLE_NAME,INFORMATION_LIST);
        uriMatcher.addURI(LocationContract.CONTENT_AUTHORITY,LocationContract.TABLE_NAME+"/#",INFORMATION_ITEM);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sort) {
        switch (uriMatcher.match(uri)){
            case INFORMATION_LIST:
                sort = LocationContract.ID;
                break;
            case INFORMATION_ITEM:
                selection = LocationContract.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Can't query unknow URI"+uri);
        }
        Cursor cursor = databaseHelper.getReadableDatabase().query(LocationContract.TABLE_NAME,projection,selection,selectionArgs,
                null,null,sort);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case INFORMATION_LIST:
                return LocationContract.CONTENT_LIST_TYPE;
            case INFORMATION_ITEM:
                return LocationContract.CONTENT_ITEM_TYPE;
        }
        throw new IllegalStateException("unknown URI "+ uri );
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        switch (uriMatcher.match(uri)){
            case INFORMATION_ITEM:
                throw new SQLException("Fail to insert row id "+ uri);
            case INFORMATION_LIST:
                long rowID = databaseHelper.getWritableDatabase().insert(LocationContract.TABLE_NAME,null,contentValues);
                if(rowID > 0)
                    return ContentUris.withAppendedId(uri,rowID);
                else
                    throw new SQLException("Fail to insert row id"+ uri);
            default:
                throw new IllegalArgumentException("Insertion is not supported for"+uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectioArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case INFORMATION_LIST:
                count = databaseHelper.getWritableDatabase().delete(LocationContract.TABLE_NAME,selection,selectioArgs);
                break;
            case INFORMATION_ITEM:
                selection = LocationContract.ID + "=?";
                selectioArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = databaseHelper.getWritableDatabase().delete(LocationContract.TABLE_NAME,selection,selectioArgs);
                break;
            default:
                throw new IllegalArgumentException("Unable to delete this uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case INFORMATION_LIST:
                count = databaseHelper.getWritableDatabase().update(LocationContract.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            case INFORMATION_ITEM:
                selection = LocationContract.ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                count = databaseHelper.getWritableDatabase().update(LocationContract.TABLE_NAME,contentValues,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unable to delete this uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
