package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    RecyclerView recycleview;
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        String SharedEmail = sharedPreferences.getString("email", null);

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                //BaseColumns._ID,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEONE,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETWO,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_ANSWER,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE,
                FeedReaderContract.FeedEntry2.COLUMN_NAME_PERSONEMAIL
        };

        String selection = FeedReaderContract.FeedEntry2.COLUMN_NAME_PERSONEMAIL + " = ?";
        String[] selectionArgs = { SharedEmail };

        String sortOrder =
                FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry2.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List itemIds = new ArrayList<>();
        //ArrayList<String> names = new ArrayList<>();
        ArrayList<Question> dbquestions = new ArrayList<>();
        String dbQuestion;
        String dbChoice1;
        String dbChoice2;
        String dbChoice3;
        String dbChoice4;
        String dbChoice5;
        String dbAnswer;
        String dbAttachment;
        String dbAttacmentType;
        while(cursor.moveToNext()) {
            /*long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));*/
            //names.add(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME)));
            //itemIds.add(itemId);
            dbQuestion = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION));
            dbChoice1 = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEONE));
            dbChoice2 = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETWO));
            dbChoice3 = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE));
            dbChoice4 = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR));
            dbChoice5 = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE));
            dbAnswer = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_ANSWER));
            dbAttachment = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT));
            dbAttacmentType = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE));

            ArrayList<String> dbchoices = new ArrayList<>();

            dbchoices.add(dbChoice1);
            dbchoices.add(dbChoice2);

            if(!dbChoice3.matches("")){
                dbchoices.add(dbChoice3);
            }

            if(!dbChoice4.matches("")){
                dbchoices.add(dbChoice4);
            }

            if(!dbChoice5.matches("")){
                dbchoices.add(dbChoice5);
            }

            Question question = new Question(dbQuestion,dbchoices,dbAnswer,dbAttachment,dbAttacmentType);

            dbquestions.add(question);
        }

        cursor.close();

        recycleview = findViewById(R.id.recyclerView);
        recycleview.setLayoutManager(new LinearLayoutManager(QuestionListActivity.this));
        questionAdapter = new QuestionAdapter(dbquestions,QuestionListActivity.this);
        recycleview.setAdapter(questionAdapter);
    }


}