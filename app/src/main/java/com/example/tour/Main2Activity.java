package com.example.tour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tour.Data.MainData;

public class Main2Activity extends AppCompatActivity {
    private Button singUpBtn;
    private EditText nameET, emailET, passwordET, repasswordET, cellNumberET;
    private String name, email, password, reTypePassword,cellNumber;
    private Double cellNumberIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        singUpBtn=findViewById(R.id.singUpBtn);
        nameET=findViewById(R.id.nameSingUpET);
        emailET=findViewById(R.id.emailSingUpET);
        passwordET=findViewById(R.id.passwordSingUpET);
        repasswordET=findViewById(R.id.reTypepasswordSingUpET);
        cellNumberET=findViewById(R.id.cellnumberSingUpET);

        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            name=nameET.getText().toString();
            email=emailET.getText().toString();
            password=passwordET.getText().toString().trim();
            reTypePassword=repasswordET.getText().toString().trim();
            cellNumber=cellNumberET.getText().toString();

            if (name.isEmpty()){
                Toast.makeText(Main2Activity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            }else if(email.isEmpty()|| !email.contains("@")){
                Toast.makeText(Main2Activity.this, "Please enter a valied email addrss.", Toast.LENGTH_SHORT).show();
            }else if(password.isEmpty()){
                Toast.makeText(Main2Activity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
            }else if(!password.contains(reTypePassword)){
                Toast.makeText(Main2Activity.this, "Password do not match.", Toast.LENGTH_SHORT).show();
            }else if(password.length()<6){
                Toast.makeText(Main2Activity.this, "Please enter 6 digit password.", Toast.LENGTH_SHORT).show();
            }
            else if(cellNumber.isEmpty()||cellNumber.length()<11){
                Toast.makeText(Main2Activity.this, "Please enter 11 digit number with out contry code.", Toast.LENGTH_SHORT).show();
            }else {
                createAccount(new MainData(name,email,password,Double.valueOf(cellNumber)));
                Toast.makeText(Main2Activity.this, "You enter :"+name+" "+email+" "+password+" "+cellNumber, Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void createAccount(MainData mainData) {

    }
}
