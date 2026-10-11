package com.generated.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zichron_veracha.db";
    private static final int DATABASE_VERSION = 1;

    // Table Members (Family & Birthdays)
    public static final String TABLE_MEMBERS = "members";
    public static final String COL_MEM_ID = "id";
    public static final String COL_MEM_NAME = "name";
    public static final String COL_MEM_RELATION = "relation";
    public static final String COL_MEM_BDAY = "birth_day";
    public static final String COL_MEM_BMONTH = "birth_month";
    public static final String COL_MEM_BYEAR = "birth_year";
    public static final String COL_MEM_HEB_BDAY = "hebrew_birth_date";
    public static final String COL_MEM_NOTES = "notes";

    // Table Yahrzeits
    public static final String TABLE_YAHRZEITS = "yahrzeits";
    public static final String COL_YAH_ID = "id";
    public static final String COL_YAH_FIRSTNAME = "first_name";
    public static final String COL_YAH_LASTNAME = "last_name";
    public static final String COL_YAH_PARENTNAME = "parent_name";
    public static final String COL_YAH_ISFATHER = "is_father"; // 1 father, 0 mother
    public static final String COL_YAH_DDAY = "death_day";
    public static final String COL_YAH_DMONTH = "death_month";
    public static final String COL_YAH_DYEAR = "death_year";
    public static final String COL_YAH_HEB_DAY = "hebrew_day";
    public static final String COL_YAH_HEB_MONTH = "hebrew_month";
    public static final String COL_YAH_HEB_YEAR = "hebrew_year";
    public static final String COL_YAH_NOTES = "notes";

    // Table Tehillim Prayer List
    public static final String TABLE_PRAYERS = "prayers";
    public static final String COL_PRAY_ID = "id";
    public static final String COL_PRAY_NAME = "person_name";
    public static final String COL_PRAY_MOTHER = "mother_name";
    public static final String COL_PRAY_TYPE = "pray_type"; // לרפואה / לעילוי נשמת

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMembers = "CREATE TABLE " + TABLE_MEMBERS + " ("
                + COL_MEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MEM_NAME + " TEXT, "
                + COL_MEM_RELATION + " TEXT, "
                + COL_MEM_BDAY + " INTEGER, "
                + COL_MEM_BMONTH + " INTEGER, "
                + COL_MEM_BYEAR + " INTEGER, "
                + COL_MEM_HEB_BDAY + " TEXT, "
                + COL_MEM_NOTES + " TEXT);";

        String createYahrzeits = "CREATE TABLE " + TABLE_YAHRZEITS + " ("
                + COL_YAH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_YAH_FIRSTNAME + " TEXT, "
                + COL_YAH_LASTNAME + " TEXT, "
                + COL_YAH_PARENTNAME + " TEXT, "
                + COL_YAH_ISFATHER + " INTEGER, "
                + COL_YAH_DDAY + " INTEGER, "
                + COL_YAH_DMONTH + " INTEGER, "
                + COL_YAH_DYEAR + " INTEGER, "
                + COL_YAH_HEB_DAY + " INTEGER, "
                + COL_YAH_HEB_MONTH + " INTEGER, "
                + COL_YAH_HEB_YEAR + " INTEGER, "
                + COL_YAH_NOTES + " TEXT);";

        String createPrayers = "CREATE TABLE " + TABLE_PRAYERS + " ("
                + COL_PRAY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PRAY_NAME + " TEXT, "
                + COL_PRAY_MOTHER + " TEXT, "
                + COL_PRAY_TYPE + " TEXT);";

        db.execSQL(createMembers);
        db.execSQL(createYahrzeits);
        db.execSQL(createPrayers);

        seedSampleData(db);
    }

    private void seedSampleData(SQLiteDatabase db) {
        // Sample family member
        ContentValues cvM = new ContentValues();
        cvM.put(COL_MEM_NAME, "דוד כהן");
        cvM.put(COL_MEM_RELATION, "אבא");
        cvM.put(COL_MEM_BDAY, 15);
        cvM.put(COL_MEM_BMONTH, 4);
        cvM.put(COL_MEM_BYEAR, 1960);
        cvM.put(COL_MEM_HEB_BDAY, "י\"ח בניסן");
        cvM.put(COL_MEM_NOTES, "אוהב שירי קודש וספרי מוסר");
        db.insert(TABLE_MEMBERS, null, cvM);

        ContentValues cvM2 = new ContentValues();
        cvM2.put(COL_MEM_NAME, "רחל לוי");
        cvM2.put(COL_MEM_RELATION, "אמא");
        cvM2.put(COL_MEM_BDAY, 20);
        cvM2.put(COL_MEM_BMONTH, 8);
        cvM2.put(COL_MEM_BYEAR, 1964);
        cvM2.put(COL_MEM_HEB_BDAY, "י\"ב באלול");
        cvM2.put(COL_MEM_NOTES, "יום הולדת משפחתי");
        db.insert(TABLE_MEMBERS, null, cvM2);

        // Sample Yahrzeit
        ContentValues cvY = new ContentValues();
        cvY.put(COL_YAH_FIRSTNAME, "אברהם");
        cvY.put(COL_YAH_LASTNAME, "כהן");
        cvY.put(COL_YAH_PARENTNAME, "יצחק ז\"ל");
        cvY.put(COL_YAH_ISFATHER, 1);
        cvY.put(COL_YAH_DDAY, 10);
        cvY.put(COL_YAH_DMONTH, 2);
        cvY.put(COL_YAH_DYEAR, 2018);
        cvY.put(COL_YAH_HEB_DAY, 25);
        cvY.put(COL_YAH_HEB_MONTH, 11); // Shevat
        cvY.put(COL_YAH_HEB_YEAR, 5778);
        cvY.put(COL_YAH_NOTES, "להדליק נר נשמה, ללמוד משניות, להגיד קדיש");
        db.insert(TABLE_YAHRZEITS, null, cvY);

        // Sample Prayer
        ContentValues cvP = new ContentValues();
        cvP.put(COL_PRAY_NAME, "משה");
        cvP.put(COL_PRAY_MOTHER, "שרה");
        cvP.put(COL_PRAY_TYPE, "לרפואה שלמה");
        db.insert(TABLE_PRAYERS, null, cvP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YAHRZEITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRAYERS);
        onCreate(db);
    }

    // Member model
    public static class Member {
        public long id;
        public String name;
        public String relation;
        public int day, month, year;
        public String hebrewDate;
        public String notes;

        public int getAge() {
            int currentYear = 2025;
            if (year > 1900 && year <= currentYear) {
                return currentYear - year;
            }
            return 30; // fallback
        }

        public int getPersonalTehillim() {
            return getAge() + 1;
        }
    }

    // Yahrzeit model
    public static class Yahrzeit {
        public long id;
        public String firstName;
        public String lastName;
        public String parentName;
        public boolean isFather;
        public int gDay, gMonth, gYear;
        public int hDay, hMonth, hYear;
        public String notes;

        public String getFullName() {
            return firstName + " " + lastName;
        }

        public String getMemorialTitle() {
            return (isFather ? "בן ר' " : "בן מרת ") + parentName;
        }
    }

    // Prayer model
    public static class PrayerPerson {
        public long id;
        public String name;
        public String motherName;
        public String type; // לרפואה / לעילוי נשמת
    }

    // Operations for Members
    public long insertMember(String name, String relation, int d, int m, int y, String heb, String notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MEM_NAME, name);
        cv.put(COL_MEM_RELATION, relation);
        cv.put(COL_MEM_BDAY, d);
        cv.put(COL_MEM_BMONTH, m);
        cv.put(COL_MEM_BYEAR, y);
        cv.put(COL_MEM_HEB_BDAY, heb);
        cv.put(COL_MEM_NOTES, notes);
        return db.insert(TABLE_MEMBERS, null, cv);
    }

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<Member>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_MEMBERS, null, null, null, null, null, COL_MEM_NAME + " ASC");
        if (c != null && c.moveToFirst()) {
            do {
                Member m = new Member();
                m.id = c.getLong(c.getColumnIndexOrThrow(COL_MEM_ID));
                m.name = c.getString(c.getColumnIndexOrThrow(COL_MEM_NAME));
                m.relation = c.getString(c.getColumnIndexOrThrow(COL_MEM_RELATION));
                m.day = c.getInt(c.getColumnIndexOrThrow(COL_MEM_BDAY));
                m.month = c.getInt(c.getColumnIndexOrThrow(COL_MEM_BMONTH));
                m.year = c.getInt(c.getColumnIndexOrThrow(COL_MEM_BYEAR));
                m.hebrewDate = c.getString(c.getColumnIndexOrThrow(COL_MEM_HEB_BDAY));
                m.notes = c.getString(c.getColumnIndexOrThrow(COL_MEM_NOTES));
                list.add(m);
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public void deleteMember(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MEMBERS, COL_MEM_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Operations for Yahrzeits
    public long insertYahrzeit(String fn, String ln, String parent, boolean isFather, int gd, int gm, int gy, int hd, int hm, int hy, String notes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_YAH_FIRSTNAME, fn);
        cv.put(COL_YAH_LASTNAME, ln);
        cv.put(COL_YAH_PARENTNAME, parent);
        cv.put(COL_YAH_ISFATHER, isFather ? 1 : 0);
        cv.put(COL_YAH_DDAY, gd);
        cv.put(COL_YAH_DMONTH, gm);
        cv.put(COL_YAH_DYEAR, gy);
        cv.put(COL_YAH_HEB_DAY, hd);
        cv.put(COL_YAH_HEB_MONTH, hm);
        cv.put(COL_YAH_HEB_YEAR, hy);
        cv.put(COL_YAH_NOTES, notes);
        return db.insert(TABLE_YAHRZEITS, null, cv);
    }

    public List<Yahrzeit> getAllYahrzeits() {
        List<Yahrzeit> list = new ArrayList<Yahrzeit>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_YAHRZEITS, null, null, null, null, null, COL_YAH_HEB_MONTH + " ASC, " + COL_YAH_HEB_DAY + " ASC");
        if (c != null && c.moveToFirst()) {
            do {
                Yahrzeit y = new Yahrzeit();
                y.id = c.getLong(c.getColumnIndexOrThrow(COL_YAH_ID));
                y.firstName = c.getString(c.getColumnIndexOrThrow(COL_YAH_FIRSTNAME));
                y.lastName = c.getString(c.getColumnIndexOrThrow(COL_YAH_LASTNAME));
                y.parentName = c.getString(c.getColumnIndexOrThrow(COL_YAH_PARENTNAME));
                y.isFather = c.getInt(c.getColumnIndexOrThrow(COL_YAH_ISFATHER)) == 1;
                y.gDay = c.getInt(c.getColumnIndexOrThrow(COL_YAH_DDAY));
                y.gMonth = c.getInt(c.getColumnIndexOrThrow(COL_YAH_DMONTH));
                y.gYear = c.getInt(c.getColumnIndexOrThrow(COL_YAH_DYEAR));
                y.hDay = c.getInt(c.getColumnIndexOrThrow(COL_YAH_HEB_DAY));
                y.hMonth = c.getInt(c.getColumnIndexOrThrow(COL_YAH_HEB_MONTH));
                y.hYear = c.getInt(c.getColumnIndexOrThrow(COL_YAH_HEB_YEAR));
                y.notes = c.getString(c.getColumnIndexOrThrow(COL_YAH_NOTES));
                list.add(y);
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public void deleteYahrzeit(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_YAHRZEITS, COL_YAH_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Operations for Prayers
    public long insertPrayer(String name, String mother, String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PRAY_NAME, name);
        cv.put(COL_PRAY_MOTHER, mother);
        cv.put(COL_PRAY_TYPE, type);
        return db.insert(TABLE_PRAYERS, null, cv);
    }

    public List<PrayerPerson> getAllPrayers() {
        List<PrayerPerson> list = new ArrayList<PrayerPerson>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PRAYERS, null, null, null, null, null, COL_PRAY_ID + " DESC");
        if (c != null && c.moveToFirst()) {
            do {
                PrayerPerson p = new PrayerPerson();
                p.id = c.getLong(c.getColumnIndexOrThrow(COL_PRAY_ID));
                p.name = c.getString(c.getColumnIndexOrThrow(COL_PRAY_NAME));
                p.motherName = c.getString(c.getColumnIndexOrThrow(COL_PRAY_MOTHER));
                p.type = c.getString(c.getColumnIndexOrThrow(COL_PRAY_TYPE));
                list.add(p);
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public void deletePrayer(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PRAYERS, COL_PRAY_ID + "=?", new String[]{String.valueOf(id)});
    }
}
