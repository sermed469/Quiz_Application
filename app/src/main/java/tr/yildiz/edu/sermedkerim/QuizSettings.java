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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class QuizSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView progressText, timeText;
    Spinner difficultySpinner;
    SeekBar seekBarTime;
    SeekBar seekBarScore;
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

        timeText.setText(sharedPreferences.getString("time","60"));
        progressText.setText(sharedPreferences.getString("score","5"));
        String difficulty = sharedPreferences.getString("difficulty","Two");

        seekBarScore.setProgress(Integer.parseInt(sharedPreferences.getString("score","5")));
        seekBarTime.setProgress(Integer.parseInt(sharedPreferences.getString("time","5")));

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
        seekBarScore.setMin(1);
        seekBarScore.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressText.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTime.setMin(15);
        seekBarTime.setMax(120);
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeText.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("time",timeText.getText().toString());
                editor.putString("score",progressText.getText().toString());
                editor.putString("difficulty",difficultySpinner.getItemAtPosition(difficultySpinner.getSelectedItemPosition()).toString());
                editor.apply();

                Intent intent = new Intent(QuizSettings.this,ShowScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getReferences(){

        difficultySpinner = findViewById(R.id.spinner3);
        progressText = findViewById(R.id.ScoreProgressText);
        seekBarScore = findViewById(R.id.seekBarScore);
        seekBarTime = findViewById(R.id.seekBarTime);
        timeText = findViewById(R.id.timeProgressText);
        set = findViewById(R.id.SetButton);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}