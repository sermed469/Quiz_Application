package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateQuiz extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int fileCount;
    EditText time2, score2;
    TextView updateTime, updateScore;
    Spinner difficulty2;
    SeekBar timeSeekbar, scoreSeekbar;
    Button updateSettings;
    RecyclerView chooseRecycler;
    SelectQuestionAdapter selectQuestionAdapter;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Question> dbquestions = new ArrayList<>();
    ArrayList<Question> questions1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        getReferences();

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        String SharedEmail = sharedPreferences.getString("email", null);
        fileCount = sharedPreferences.getInt(SharedEmail,0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty2.setAdapter(adapter);
        difficulty2.setOnItemSelectedListener(this);

        updateTime.setText(sharedPreferences.getString("time","0"));
        updateScore.setText(sharedPreferences.getString("score","0"));
        String dif = sharedPreferences.getString("difficulty","2");

        scoreSeekbar.setProgress(Integer.parseInt(sharedPreferences.getString("score","5")));
        timeSeekbar.setProgress(Integer.parseInt(sharedPreferences.getString("time","5")));

        switch (dif){
            case "Two":
                difficulty2.setSelection(0);
                break;
            case "Three":
                difficulty2.setSelection(1);
                break;
            case "Four":
                difficulty2.setSelection(2);
                break;
            case "Five":
                difficulty2.setSelection(3);
                break;
            default:
                break;
        }

        /*for(Question q : Question.getQuestions()){
            if(q.getChoices().size() == difficulty2.getSelectedItemPosition() + 2){
                questions.add(q);
            }
        }*/



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

            Question question = new Question(dbQuestion,dbchoices,dbAnswer, dbAttachment,dbAttacmentType);

            dbquestions.add(question);
        }

        cursor.close();

        for(Question q : dbquestions){
            questions1.add(q);
        }

        chooseRecycler = findViewById(R.id.ChooseRecylerView);
        chooseRecycler.setLayoutManager(new LinearLayoutManager(CreateQuiz.this));
        selectQuestionAdapter = new SelectQuestionAdapter(dbquestions,CreateQuiz.this);
        chooseRecycler.setAdapter(selectQuestionAdapter);

        timeSeekbar.setMin(15);
        timeSeekbar.setMax(120);
        timeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTime.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scoreSeekbar.setMin(1);
        scoreSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateScore.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("time",updateTime.getText().toString());
                editor.putString("score",updateScore.getText().toString());
                editor.putString("difficulty",difficulty2.getItemAtPosition(difficulty2.getSelectedItemPosition()).toString());
                //editor.putInt("file",fileCount + 1);
                editor.putInt(SharedEmail,fileCount + 1);
                editor.putInt("file",1);
                editor.apply();

                File file  = new File(getApplicationContext().getFilesDir(), "File");
                //File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), SharedEmail + "File" + "" + ".txt");
                String filename = SharedEmail + "File" + fileCount + ".txt";
                String FileContents = createQuizFile(updateTime.getText().toString(),updateScore.getText().toString(),difficulty2.getItemAtPosition(difficulty2.getSelectedItemPosition()).toString());
                try {
                    FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
                    //FileOutputStream fos = new FileOutputStream(file);
                    fos.write(FileContents.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CreateQuiz.this,ShowScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getReferences(){

        difficulty2 = findViewById(R.id.spinner2);
        updateSettings = findViewById(R.id.updateSettingsButton);
        updateTime = findViewById(R.id.TimeProgressUpdateText);
        updateScore = findViewById(R.id.ScoreProgressUpdateText);
        timeSeekbar = findViewById(R.id.seekBarTimeUpdate);
        scoreSeekbar = findViewById(R.id.seekBarScoreUpdate);
    }

    public String createQuizFile(String time,String score,String difficulty){

        String file = "";

        file += "Quiz time : " + time + " minute\n";
        file += "Question point : " + score + "\n";
        file += "Quiz difficulty : " + difficulty + "\n\n";

        for(Question q : SelectQuestionAdapter.selectedQuestions){
            file += "Question : " + q.getQuestion() + "\n";
            file += "A)" + q.getChoices().get(0) + "\n";
            file += "B)" + q.getChoices().get(1) + "\n";
            if(q.getChoices().size() == 3){
                file += "C)" + q.getChoices().get(2) + "\n";
            }
            else if(q.getChoices().size() == 4){
                file += "D)" + q.getChoices().get(3) + "\n";
            }
            else if(q.getChoices().size() == 5){
                file += "E)" + q.getChoices().get(4) + "\n";
            }

            if(!q.getAttachment().matches("")){
                file += "Attachment : " + Uri.parse(q.getAttachment()).getLastPathSegment() + "\n";
            }

            file += "Answer : " + q.getAnswer() + "\n\n";
        }

        return file;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        SelectQuestionAdapter.selectedQuestions.clear();

        dbquestions.clear();

        for(Question q : questions1){
            if(q.getChoices().size() == position + 2){
                dbquestions.add(q);
            }
        }

        selectQuestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}