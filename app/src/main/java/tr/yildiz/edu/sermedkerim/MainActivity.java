package tr.yildiz.edu.sermedkerim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signin,signup;
    public ArrayList<Person> personList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getReferences();

        personList = Person.getPersonList();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int avatarId = ControlUser(email.getText().toString(),password.getText().toString());
                if(avatarId != 0){
                    Intent intent = new Intent(MainActivity.this,ShowScreen.class);
                    intent.putExtra("user",email.getText().toString());
                    intent.putExtra("avatarId",avatarId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"There is no user with this email",Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpScreen.class);
                startActivity(intent);
            }
        });
    }

    public void getReferences(){

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        signin = findViewById(R.id.buttonSignIn);
        signup = findViewById(R.id.buttonSignUp);
    }

    public int ControlUser(String email,String password){

        if(personList != null){
            for (Person p: personList) {
                if (p.email.matches(email) && p.password.matches(SignUpScreen.hashPassword(password))){
                    return p.avatarId;
                }
            }
        }
        return 0;
    }
}