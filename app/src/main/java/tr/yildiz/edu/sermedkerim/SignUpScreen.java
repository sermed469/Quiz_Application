package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SignUpScreen extends AppCompatActivity {

    EditText name, surname, email, password1, password2;
    TextView dateofbirth;
    Button signup;
    ArrayList<EditText> editTexts = new ArrayList<>();
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        getReferences();

        editTexts.add(name);
        editTexts.add(surname);
        editTexts.add(email);
        editTexts.add(password1);
        editTexts.add(password2);

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignUpScreen.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                dateofbirth.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNotEmptyField()){
                    if(isEmailValid()){
                        if (isSameUser()){
                            if (password1.getText().toString().matches(password2.getText().toString())){

                                String hashedPassword = hashPassword(password1.getText().toString());

                                Person person = new Person(name.getText().toString(),surname.getText().toString(),dateofbirth.getText().toString(),email.getText().toString(),hashedPassword,R.drawable.sermet);
                                Person.personList.add(person);
                                Person.setPersonList(Person.personList);

                                Intent intent = new Intent(SignUpScreen.this,ShowScreen.class);
                                intent.putExtra("user",email.getText().toString());
                                intent.putExtra("avatarId",R.drawable.sermet);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Passwords don't match",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"This user is already signed up",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Email is not valid",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"All input fields must be non empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getReferences(){

        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSurname);
        email = findViewById(R.id.editTextEmailSignUp);
        dateofbirth = findViewById(R.id.textView);
        password1 = findViewById(R.id.editTextPasswordSignUp);
        password2 = findViewById(R.id.editTextPasswordSignUp2);
        signup = findViewById(R.id.SignUpButton);
    }

    public boolean isNotEmptyField(){

        for(EditText editText : editTexts){
            if (editText.getText().toString().matches("")){
                return  false;
            }
        }

        if(dateofbirth.getText().toString().matches("dd/mm/yy")){
            return false;
        }

        return true;
    }

    public boolean isSameUser(){

        for(Person p : Person.getPersonList()){
            if(p.email.matches(email.getText().toString())){
                return false;
            }
        }
        return true;
    }

    public boolean isEmailValid(){
        String Email = email.getText().toString();
        if(Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            return true;
        }
        else{
            return false;
        }
    }

    public static String hashPassword(String password){

        StringBuffer buffer = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            for (int i = 0;i<bytes.length;i++) {
                buffer.append(Integer.toHexString(0xFF & bytes[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}