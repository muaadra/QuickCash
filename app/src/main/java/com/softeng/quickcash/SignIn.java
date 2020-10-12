package com.softeng.quickcash;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    EditText email, password;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        Button btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_check(db, email.getText().toString(), password.getText().toString());
            }
        });
    }

    private void clearWarningLabels() {
        ((TextView) findViewById(R.id.err_msg_email)).setText("");
        ((TextView) findViewById(R.id.err_msg_pw)).setText("");
    }

    public void login_check(DatabaseReference db, final String useremail, final String pw) {
        clearWarningLabels();
        db.addValueEventListener(new ValueEventListener() {
            Map<String, HashMap<String, HashMap<String, HashMap<String, String>>>> info;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = useremail.replace(".", ";");
                System.out.println(email);
                info = (Map<String, HashMap<String, HashMap<String, HashMap<String, String>>>>) dataSnapshot.getValue();
                try {
                    String password = info.get("users").get(email).get("SignUpInfo").get("password").toString();
                    if (password.equals(pw)) {
                        ((TextView) findViewById(R.id.msg_confirm))
                                .setText("Login successful");
                    } else {
                        ((TextView) findViewById(R.id.err_msg_pw)).setText("Passwords do not match.");
                    }
                } catch (Exception e) {
                    Toast.makeText(SignIn.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}