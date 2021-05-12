package tr.yildiz.edu.sermedkerim;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_NAME + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SURNAME + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PHONE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DATEOFBIRTH + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_PASSWORD + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_AVATAR + " BLOB)";

    private static final String SQL_CREATE_ENTRIES2 =
            "CREATE TABLE " + FeedReaderContract.FeedEntry2.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry2._ID + "INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEONE + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETWO + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_ANSWER + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE + " TEXT," +
                    FeedReaderContract.FeedEntry2.COLUMN_NAME_PERSONEMAIL + " TEXT," +
                    " FOREIGN KEY (" + FeedReaderContract.FeedEntry2.COLUMN_NAME_PERSONEMAIL + ") REFERENCES " + FeedReaderContract.FeedEntry.TABLE_NAME + "(" + FeedReaderContract.FeedEntry.COLUMN_NAME_EMAIL + "));";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES2 =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry2.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES2);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES2);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
