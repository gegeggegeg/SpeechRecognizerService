package com.example.peterphchen.speechrecognizerservice;

public class LocationContract {
    public static final int SCHEMA = 1;
    public static final String TABLE_NAME = "phone_call_data";
    public static final String ID = "_id";
    public static final String LOCATION = "location";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String TIME = "time";
    public static final String PHONE_NUMBER = "number";
    public static final String GOOGLEMAP_URL = "mapUrl";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +LOCATION+" TEXT,"+LONGITUDE+" REAL,"+LATITUDE+" REAL,"+TIME+" TEXT,"+PHONE_NUMBER+" TEXT,"+ GOOGLEMAP_URL+ " TEXT);";
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
}
