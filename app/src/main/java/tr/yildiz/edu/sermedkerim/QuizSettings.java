package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class QuizSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText time, score;
    Spinner difficultySpinner;
    Button set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_settings);

        getReferences();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setOnItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        time.setText(sharedPreferences.getString("time","60"));
        score.setText(sharedPreferences.getString("score","5"));
        String difficulty = sharedPreferences.getString("difficulty","Two");

        switch (difficulty){

            case "Two":
                difficultySpinner.setSelection(0);
                break;
            case "Three":
                difficultySpinner.setSelection(1);
                break;
            case "Four":
                difficultySpinner.setSelection(2);
                break;
            case "Five":
                difficultySpinner.setSelection(3);
                break;
        }

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("time",time.getText().toString());
                editor.putString("score",score.getText().toString());
                editor.putString("difficulty",difficultySpinner.getItemAtPosition(difficultySpinner.getSelectedItemPosition()).toString());
                editor.apply();

                Intent intent = new Intent(QuizSettings.this,ShowScreen.class);
                startActivity(intent);
            }
        });
    }

    public void getReferences(){
        time = findViewById(R.id.QuizTimeEditText);
        score = findViewById(R.id.QuestionScoreEditText);
        difficultySpinner = findViewById(R.id.spinner3);
        set = findViewById(R.id.SetButton);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}