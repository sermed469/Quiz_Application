package tr.yildiz.edu.sermedkerim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText question, choice1, choice2, choice3, choice4, choice5, answer;
    TextView datasource;
    Button addFile, upload;
    Spinner spinner;
    Spinner choiceFile;
    static final int REQUEST_IMAGE_OPEN = 2;
    Uri URI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_screen);

        Intent intent = getIntent();
        String IntentQuestion = intent.getStringExtra("questiontitle");
        Question UpdateQuestion = (Question) intent.getSerializableExtra("UpdateQuestion");

        getReferences();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.file, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choiceFile.setAdapter(adapter2);
        choiceFile.setOnItemSelectedListener(this);

        question.setText(UpdateQuestion.getQuestion());
        choice1.setText(UpdateQuestion.getChoices().get(0));
        choice2.setText(UpdateQuestion.getChoices().get(1));
        answer.setText(UpdateQuestion.getAnswer());

        if(UpdateQuestion.getChoices().size() == 3){
            choice3.setText(UpdateQuestion.getChoices().get(2));
            spinner.setSelection(1);
        }
        else if(UpdateQuestion.getChoices().size() == 4){
            choice3.setText(UpdateQuestion.getChoices().get(2));
            choice4.setText(UpdateQuestion.getChoices().get(3));
            spinner.setSelection(2);
        }
        else if(UpdateQuestion.getChoices().size() == 5){
            choice3.setText(UpdateQuestion.getChoices().get(2));
            choice4.setText(UpdateQuestion.getChoices().get(3));
            choice5.setText(UpdateQuestion.getChoices().get(4));
            spinner.setSelection(3);
        }

        if(UpdateQuestion.getAttachment() != null){
            URI = Uri.parse(UpdateQuestion.getAttachment());
            datasource.setText(Uri.parse(UpdateQuestion.getAttachment()).getLastPathSegment());
            if(UpdateQuestion.getAttachmentType().matches("image")){
                choiceFile.setSelection(0);
            }
            else if(UpdateQuestion.getAttachmentType().matches("video")){
                choiceFile.setSelection(1);
            }
            else if(UpdateQuestion.getAttachmentType().matches("audio")){
                choiceFile.setSelection(2);
            }
        }

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileType = (String) choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition());
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType(fileType+"/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_IMAGE_OPEN);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmpty()){
                    if(isChoicesNotSame()){
                        if(isAnswerTypeCorrect()) {

                            FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            String title = "MyNewTitle";
                            ContentValues values = new ContentValues();
                            values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION, question.getText().toString());
                            values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEONE, choice1.getText().toString());
                            values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETWO, choice2.getText().toString());

                            ArrayList<String> choices = new ArrayList<>();
                            choices.add(choice1.getText().toString());
                            choices.add(choice2.getText().toString());
                            if (choice3.isEnabled()) {
                                choices.add(choice3.getText().toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE, choice3.getText().toString());
                            } else {
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICETHREE, "");
                            }
                            if (choice4.isEnabled()) {
                                choices.add(choice4.getText().toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR, choice4.getText().toString());
                            } else {
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFOUR, "");
                            }
                            if (choice5.isEnabled()) {
                                choices.add(choice5.getText().toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE, choice5.getText().toString());
                            } else {
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_CHOICEFIVE, "");
                            }

                            values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ANSWER, answer.getText().toString());
                            if (URI != null) {
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT, URI.toString());
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE, choiceFile.getItemAtPosition(choiceFile.getSelectedItemPosition()).toString());
                            } else {
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENT, "");
                                values.put(FeedReaderContract.FeedEntry2.COLUMN_NAME_ATTACHMENTTYPE, "");
                            }

                            String selection = FeedReaderContract.FeedEntry2.COLUMN_NAME_QUESTION + " LIKE ?";
                            String[] selectionArgs = {IntentQuestion};

                            int count = db.update(
                                    FeedReaderContract.FeedEntry2.TABLE_NAME,
                                    values,
                                    selection,
                                    selectionArgs);

                            Intent intent1 = new Intent(UpdateScreen.this, QuestionListActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                        else{
                                Toast.makeText(getApplicationContext(),"Answer does not match any of the choices",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                            Toast.makeText(getApplicationContext(),"Choices must be different",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                        Toast.makeText(getApplicationContext(),"All fields must be non empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getReferences(){
        question = findViewById(R.id.editTextQuestionUpdate);
        choice1 = findViewById(R.id.editTextChoice1Update);
        choice2 = findViewById(R.id.editTextChoice2Update);
        choice3 = findViewById(R.id.editTextChoice3Update);
        choice4 = findViewById(R.id.editTextChoice4Update);
        choice5 = findViewById(R.id.editTextChoice5Update);
        answer = findViewById(R.id.editTextAnswerUpdate);
        addFile = findViewById(R.id.AddFileButtonUpdate);
        upload = findViewById(R.id.UploadQuestionButtonUpdate);
        datasource = findViewById(R.id.DataSourceTextUpdate);
        spinner = findViewById(R.id.spinnerUpdate);
        choiceFile = findViewById(R.id.spinnerFileTypeUpdate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK){
            URI = data.getData();
            getContentResolver().takePersistableUriPermission(URI,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            datasource.setText(URI.getLastPathSegment());
            datasource.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        switch (text){
            case "Two":
                choice3.setEnabled(false);
                choice4.setEnabled(false);
                choice5.setEnabled(false);
                break;
            case "Three":
                choice3.setEnabled(true);
                choice4.setEnabled(false);
                choice5.setEnabled(false);
                break;
            case "Four":
                choice3.setEnabled(true);
                choice4.setEnabled(true);
                choice5.setEnabled(false);
                break;
            case "Five":
                choice3.setEnabled(true);
                choice4.setEnabled(true);
                choice5.setEnabled(true);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isEmpty(){
        if(question.getText().toString().matches("")){
            return false;
        }
        if(choice1.getText().toString().matches("")){
            return false;
        }
        if(choice2.getText().toString().matches("")){
            return false;
        }
        if(choice3.isEnabled()){
            if(choice3.getText().toString().matches("")){
                return false;
            }
        }
        if(choice4.isEnabled()){
            if(choice4.getText().toString().matches("")){
                return false;
            }
        }
        if(choice5.isEnabled()){
            if(choice5.getText().toString().matches("")){
                return false;
            }
        }
        if(answer.getText().toString().matches("")){
            return false;
        }

        return true;
    }

    public boolean isAnswerTypeCorrect(){
        if(answer.getText().toString().matches(choice1.getText().toString())){
            return true;
        }
        if(answer.getText().toString().matches(choice2.getText().toString())){
            return true;
        }
        if(choice3.isEnabled()){
            if(answer.getText().toString().matches(choice3.getText().toString())){
                return true;
            }
        }
        if(choice4.isEnabled()){
            if(answer.getText().toString().matches(choice4.getText().toString())){
                return true;
            }
        }
        if(choice5.isEnabled()){
            if(answer.getText().toString().matches(choice5.getText().toString())){
                return true;
            }
        }
        return false;
    }

    public boolean isChoicesNotSame(){

        ArrayList<EditText> editTexts = new ArrayList<>();
        editTexts.add(choice1);
        editTexts.add(choice2);
        editTexts.add(choice3);
        editTexts.add(choice4);
        editTexts.add(choice5);

        ArrayList<String> choices = new ArrayList<>();

        for(EditText editText : editTexts){
            if(editText.isEnabled()){
                choices.add(editText.getText().toString());
            }
        }

        int i, j;

        for (i = 0; i < choices.size(); i++){
            for (j = i+1; j < choices.size(); j++){
                if(choices.get(i).matches(choices.get(j))){
                    return false;
                }
            }
        }

        return true;
    }
}