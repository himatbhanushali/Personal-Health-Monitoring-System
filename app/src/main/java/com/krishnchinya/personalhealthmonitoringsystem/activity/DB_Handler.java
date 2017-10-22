package com.krishnchinya.personalhealthmonitoringsystem.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import com.krishnchinya.personalhealthmonitoringsystem.other.GlobalVars;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KrishnChinya on 2/12/17.
 */

public class DB_Handler extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 11;
    // Database Name
    private static final String DATABASE_NAME = "PHMS";
    // Contacts table name
    private static final String TABLE_REGISTRATION = "REGISTRATION";
    private static final String TABLE_LOGIN = "LOGIN";
    private static final String TABLE_MEDICINE = "MEDICATION";
    private static final String TABLE_VITALS = "VITALS";
    private static final String TABLE_NOTES = "NOTES";
    private static final String TABLE_CALORIES = "CALORIES";


    public DB_Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_REGISTRATION = "create table " + TABLE_REGISTRATION + " (fname TEXT , lname TEXT, dob TEXT, gender TEXT," +
                " mailId TEXT PRIMARY KEY, weight TEXT,height TEXT)";

        String CREATE_TABLE_LOGIN = "create table " + TABLE_LOGIN + " (mailId TEXT PRIMARY KEY, password TEXT)";

        String CREATE_TABLE_MEDICATION = "create table " + TABLE_MEDICINE + " (mailid TEXT,medicinename TEXT, doctorname Text, medicinetype Text, start_date TEXT," +
                " end_date TEXT, woftd TEXT, remainder TEXT, time TEXT)";

        String CREATE_TABLE_VITALS = "create table " + TABLE_VITALS + " (mailid TEXT, spbloodtype TEXT , spcholesterol TEXT, spbp TEXT," +
                " glucose TEXT, heartrate TEXT, bmi TEXT, bodytemp TEXT)";

        String CREATE_TABLE_NOTES = "create table " + TABLE_NOTES + " (Id TEXT, mailid TEXT, notename TEXT , description TEXT, datetime TEXT, bytesarray BLOB )";

        String CREATE_TABLE_CALORIES = "create table " + TABLE_CALORIES + " (mailId TEXT, Date TEXT, itemName TEXT,nfCalories TEXT," +
                "nf_total_fat TEXT, nf_cholesterol TEXT,nf_total_carbohydrate TEXT, nf_serving_size_qty TEXT)";

        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_REGISTRATION);
        db.execSQL(CREATE_TABLE_MEDICATION);
        db.execSQL(CREATE_TABLE_VITALS);
        db.execSQL(CREATE_TABLE_NOTES);
        db.execSQL(CREATE_TABLE_CALORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VITALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALORIES);
        onCreate(db);
    }

    public void addRegistration(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fname", dbSetterGetter.getfName());
        values.put("lname", dbSetterGetter.getlName());
        values.put("dob", dbSetterGetter.getdob());
        values.put("gender", dbSetterGetter.getGender());
        values.put("mailId", dbSetterGetter.getMailID().toLowerCase());
        values.put("weight", dbSetterGetter.getWeight());
        values.put("height", dbSetterGetter.getHeight());

        db.insert(TABLE_REGISTRATION, null, values);
        db.close();

    }

    public void editegistration(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fname", dbSetterGetter.getfName());
        values.put("lname", dbSetterGetter.getlName());
        values.put("dob", dbSetterGetter.getdob());
        values.put("gender", dbSetterGetter.getGender());
        values.put("weight", dbSetterGetter.getWeight());
        values.put("height", dbSetterGetter.getHeight());

        db.update(TABLE_REGISTRATION, values, "mailId='" + dbSetterGetter.getMailID().toLowerCase() + "'", null);
        db.close();

    }

    public void addLogin(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mailId", dbSetterGetter.getMailID().toLowerCase());
        values.put("password", dbSetterGetter.getPassword());

        db.insert(TABLE_LOGIN, null, values);
        db.close();

    }

    public void addmedication(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mailId", dbSetterGetter.getMailId().toLowerCase());
        values.put("medicinename", dbSetterGetter.getMedicinename());
        values.put("doctorname", dbSetterGetter.getDoctorName());
        values.put("medicinetype", dbSetterGetter.getMedicineType());
        values.put("start_date", dbSetterGetter.getStart_date());
        values.put("end_date", dbSetterGetter.getEnd_date());
        values.put("woftd", dbSetterGetter.getWkofd());
        values.put("remainder", dbSetterGetter.getRemainder());
        values.put("time", dbSetterGetter.getTime());

        db.insert(TABLE_MEDICINE, null, values);
        db.close();
    }


    public void addvitalSigns(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mailid", dbSetterGetter.getMailId().toLowerCase());
        values.put("spbloodtype", dbSetterGetter.getSpbloodtype());
        values.put("spcholesterol", dbSetterGetter.getSpcholesterol());
        values.put("spbp", dbSetterGetter.getSpbp());
        //values.put("haemoglobin",dbSetterGetter.getHaemoglobin());
        values.put("glucose", dbSetterGetter.getGlucose());
        values.put("heartrate", dbSetterGetter.getHeartrate());
        values.put("bmi", dbSetterGetter.getBmi());
        values.put("bodytemp", dbSetterGetter.getBodytemp());

        db.insert(TABLE_VITALS, null, values);
        db.close();


    }


    public String[][] getmedication(String mailid, String currentdate) {
        int rowcount = getmedicationcount(mailid);
        int noofrows = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date temp;

        String[][] medicationdetailsdup = new String[rowcount][6];
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDICINE, new String[]{"medicinename", "medicinetype", "start_date", "end_date", "woftd", "time"}, "mailId = ? "
//                "AND start_date=? AND end_date<=?"
                , new String[]{mailid.toLowerCase()}, null, null, null);

        while (cursor.moveToNext()) {
            //cursor.moveToFirst();
            //use for loop when im not lazy.
            medicationdetailsdup[count][0] = cursor.getString(0);
            medicationdetailsdup[count][1] = cursor.getString(1);
            medicationdetailsdup[count][2] = cursor.getString(2);
            medicationdetailsdup[count][3] = cursor.getString(3);
            medicationdetailsdup[count][4] = cursor.getString(4);
            medicationdetailsdup[count][5] = cursor.getString(5);

            count++;
        }
        try {


            for (int i = 0; i < medicationdetailsdup.length; i++) {
                if (dateFormat.parse(currentdate).before(dateFormat.parse(medicationdetailsdup[i][2])) ||
                        dateFormat.parse(currentdate).after(dateFormat.parse(medicationdetailsdup[i][3]))) {
                    //do nothing
                } else {
                    //add to new array
                    noofrows++;
                }
            }

            String[][] medicationdetails = new String[noofrows][6];

            for (int i = 0; i < medicationdetails.length; i++) {
                if (dateFormat.parse(currentdate).before(dateFormat.parse(medicationdetailsdup[i][2])) &&
                        dateFormat.parse(currentdate).after(dateFormat.parse(medicationdetailsdup[i][3]))) {
                    //do nothing
                } else {
                    //add to new array
                    medicationdetails[i][0] = medicationdetailsdup[i][0];
                    medicationdetails[i][1] = medicationdetailsdup[i][1];
                    medicationdetails[i][2] = medicationdetailsdup[i][2];
                    medicationdetails[i][3] = medicationdetailsdup[i][3];
                    medicationdetails[i][4] = medicationdetailsdup[i][4];
                    medicationdetails[i][5] = medicationdetailsdup[i][5];
                }
            }

            return medicationdetails;

        } catch (Exception ex) {

        }
        return new String[0][0];

    }

    public int getmedicationcount(String mailid) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEDICINE, new String[]{"mailId"}, "mailId = ?",
                new String[]{mailid}, null, null, null);

        return cursor.getCount();
    }


    public boolean checkMail(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REGISTRATION, new String[]{"mailId"}, "mailId = ?",
                new String[]{dbSetterGetter.getMailID()}, null, null, null);

        if (cursor.getCount() > 0) {
            return false;
        }

        return true;
    }

    public String[] getcredentials(DB_Setter_Getter dbSetterGetter) {
        String[] details = new String[3];
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOGIN, new String[]{"mailId", "password"}, "mailId = ?",
                new String[]{dbSetterGetter.getMailID().toLowerCase()}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            details[0] = cursor.getString(0);
            details[1] = cursor.getString(1);
        }

        cursor = db.query(TABLE_REGISTRATION, new String[]{"fname"}, "mailId = ?",
                new String[]{dbSetterGetter.getMailID().toLowerCase()}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            details[2] = cursor.getString(0);
        }
        return details;
    }

    public String[] getRegistrationDetails(DB_Setter_Getter dbSetterGetter) {
        String[] details = new String[6];
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REGISTRATION, new String[]{"fname", "lname", "dob", "gender", "weight", "height"}, "mailId = ?",
                new String[]{dbSetterGetter.getMailID().toLowerCase()}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < details.length; i++) {
                details[i] = cursor.getString(i);
            }
        }

        return details;

    }

    public void SetNewPassword(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("password", dbSetterGetter.getPassword());

        db.update(TABLE_LOGIN, values, "mailId='" + dbSetterGetter.getMailID().toLowerCase() + "'", null);
        db.close();
    }

    public ArrayList<ArrayList<String>> getVitalDetails(String mailId) {
        ArrayList<ArrayList<String>> row = new ArrayList<ArrayList<String>>();
        ArrayList<String> column;
        int count = 0;
        // String[] details = new String[7];
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(TABLE_VITALS,new String[]{"spbloodtype","spcholesterol","spbp","glucose","heartrate","bmi","bodytemp"},"mailid = ?",
//                new String[]{mailId.toLowerCase()},null,null,null);

        Cursor cursor = db.query(TABLE_VITALS, new String[]{"spbloodtype", "glucose", "bmi", "spcholesterol"}, "mailid = ?",
                new String[]{mailId.toLowerCase()}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                column = new ArrayList<>();
                // for(int j=0;j<cursor.getCount();j++) {
                for (int i = 0; i < 4; i++) {
                    column.add(cursor.getString(i));
                }
                //row.get(count).add(column);
                row.add(column);

            }
        }
        return row;

    }


    public void addNotes(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Id", dbSetterGetter.getID());
        values.put("mailid", dbSetterGetter.getMailID().toLowerCase());
        values.put("notename", dbSetterGetter.getNotename());
        values.put("description", dbSetterGetter.getDescription());
        values.put("datetime", dbSetterGetter.getDatetime());
        values.put("bytesarray", dbSetterGetter.getBytesarray());
        // values.put();

        //values.put("haemoglobin",dbSetterGetter.getHaemoglobin());

        db.insert(TABLE_NOTES, null, values);
        db.close();


    }

    public String[] getNote(DB_Setter_Getter db_setter_getter, String noteid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] details = new String[3];

        Cursor cursor = db.query(TABLE_NOTES, new String[]{"notename", "description", "Id"}, "Id = ? AND mailid = ?",
                new String[]{noteid, "krishna@gmail.com"}, null, null, null);

        cursor.moveToFirst();
        details[0] = cursor.getString(0);
        details[1] = cursor.getString(1);
        details[2] = cursor.getString(2);

        return details;
    }

    public byte[] getNoteImage(DB_Setter_Getter db_setter_getter, String noteid) {
        SQLiteDatabase db = this.getReadableDatabase();

        byte[] details;

        Cursor cursor = db.query(TABLE_NOTES, new String[]{"bytesarray"}, "Id = ? AND mailid = ?",
                new String[]{noteid, "krishna@gmail.com"}, null, null, null);

        cursor.moveToFirst();
        details = cursor.getBlob(0);

        return details;
    }


//    public String[] getNote(DB_Setter_Getter db_setter_getter, String noteid)
//    {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String[] details = new String[3];
//
//        Cursor cursor = db.query(TABLE_NOTES,new String[]{"notename","description", "Id"},"Id = ? AND mailid = ?",
//                new String[]{noteid, "krishna@gmail.com"},null,null,null);
//
//        cursor.moveToFirst();
//        details[0] = cursor.getString(0);
//        details[1] = cursor.getString(1);
//        details[2] = cursor.getString(2);
//
//        return details;
//    }

    public ArrayList<String> getAllNotes(String mailId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;


        ArrayList<String> details = new ArrayList<>();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{"notename", "Id"}, "mailid = ?",
                new String[]{"krishna@gmail.com"}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //cursor.moveToFirst();
                details.add(cursor.getString(0));
                details.add(cursor.getString(1));
                //count++;

            }

        }
        return details;
    }

    public void updateNote(DB_Setter_Getter db_setter_getter, String noteid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("notename", db_setter_getter.getNotename());
        values.put("description", db_setter_getter.getDescription());
        values.put("bytesarray", db_setter_getter.getBytesarray());

        db.update(TABLE_NOTES, values, "Id = " + noteid, null);
        db.close();
    }

    public int getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NOTES, new String[]{"Id"}, null,
                null, null, null, null);

        return cursor.getCount();

    }

    public void addCalories(DB_Setter_Getter dbSetterGetter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mailId", dbSetterGetter.getMailId());
        values.put("Date", dbSetterGetter.getAddedDate());
        values.put("itemName", dbSetterGetter.getItemName());
        values.put("nfCalories", dbSetterGetter.getNfCalories());
        values.put("nf_total_fat", dbSetterGetter.getNf_total_fat());
        values.put("nf_cholesterol", dbSetterGetter.getNf_cholesterol());
        values.put("nf_total_carbohydrate", dbSetterGetter.getNf_total_carbohydrate());
        values.put("nf_serving_size_qty", dbSetterGetter.getNf_serving_size_qty());

        db.insert(TABLE_CALORIES, null, values);
        db.close();
    }

    public void delete(int noteid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, "Id=?", new String[]{String.valueOf(noteid)});
        db.close();
    }

    public ArrayList<ArrayList<String>> getDietRecords(String mailId, String date) {

        ArrayList<ArrayList<String>> row = new ArrayList<ArrayList<String>>();
        ArrayList<String> column;
        int count = 0;
        // String[] details = new String[7];
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(TABLE_VITALS,new String[]{"spbloodtype","spcholesterol","spbp","glucose","heartrate","bmi","bodytemp"},"mailid = ?",
//                new String[]{mailId.toLowerCase()},null,null,null);

        Cursor cursor = db.query(TABLE_CALORIES, new String[]{"nfCalories", "nf_total_fat", "nf_cholesterol", "itemName"}, "mailId = ? AND Date = ?",
                new String[]{mailId.toLowerCase(), date.toLowerCase()}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                column = new ArrayList<>();
                // for(int j=0;j<cursor.getCount();j++) {
                for (int i = 0; i < 4; i++) {
                    column.add(cursor.getString(i));
                }
                //row.get(count).add(column);
                row.add(column);

            }
        }
        return row;
    }


}
