package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ShareQuiz extends AppCompatActivity {

    EditText quiz;
    Button share,get;
    NumberPicker picker;
    int choosenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_quiz);

        quiz = findViewById(R.id.editTextTextMultiLineQuiz);
        share = findViewById(R.id.shareQuizButton);
        get = findViewById(R.id.getQuizButton);
        picker = findViewById(R.id.filePicker);

        quiz.setFocusable(false);

        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);

        String email = sharedPreferences.getString("email","");
        int fileNumber = sharedPreferences.getInt(email,0);

        picker.setMinValue(0);
        if(fileNumber > 0){
            picker.setMaxValue(fileNumber-1);
        }

        ArrayList<String> files = new ArrayList<>();

        int i;
        for(i = 1; i < fileNumber+1; i++){
            files.add("Quiz " + i);
        }

        String[] stringArray = files.toArray(new String[fileNumber]);
        if(stringArray.length > 0){
            picker.setDisplayedValues(stringArray);

            picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    choosenFile = picker.getValue();
                }
            });

            get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiz.setText("");
                    String filename = email + "File" + choosenFile + ".txt";

                    FileReader fr = null;
                    File file = new File(getExternalFilesDir("files"),filename);
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        fr = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fr);
                        String line = bufferedReader.readLine();
                        while (line != null){
                            stringBuilder.append(line).append("\n");
                            line = bufferedReader.readLine();
                        }
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        String contents = stringBuilder.toString();
                        quiz.setText(contents);
                    }

                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filename = email + "File" + choosenFile + ".txt";
                    File file = new File(getApplicationContext().getFilesDir(), filename);

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("/storage/1BEC-080A/Android/data/tr.yildiz.edu.sermedkerim/files/files/" + filename));
                    startActivity(Intent.createChooser(intent,"Share"));
                }
            });
        }
    }
}