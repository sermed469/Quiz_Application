package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
                    //choosenFile = choosenFile + 1;
                    String filename = email + "File" + choosenFile + ".txt";

                    FileInputStream fis = null;
                    try {
                        fis = getApplicationContext().openFileInput(filename);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    StringBuilder stringBuilder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(inputStreamReader)){
                        String line = reader.readLine();
                        while (line != null){
                            stringBuilder.append(line).append("\n");
                            line = reader.readLine();
                        }
                    }catch (IOException e){

                    }finally {
                        String contents = stringBuilder.toString();
                        quiz.setText(contents);
                    }
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_SEND);

// Always use string resources for UI text.
// This says something like "Share this photo with"
                String title = getResources().getString(R.string.chooser_title);
// Create intent to show chooser
                Intent chooser = Intent.createChooser(intent, title);

// Try to invoke the intent.
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                }*/

                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/

                    String filename = email + "File" + choosenFile + ".txt";
                    File file = new File(getApplicationContext().getFilesDir(), filename);
                    //File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), email + "File" + "" + ".txt");
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("/data/data/tr.yildiz.edu.sermedkerim/files/" + filename));
                    startActivity(Intent.createChooser(intent,"Share"));
                /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
                startActivity(Intent.createChooser(sharingIntent, "share file with"));*/

                }
            });
        }
    }
}