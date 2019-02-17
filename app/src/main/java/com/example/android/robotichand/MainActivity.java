package com.example.android.robotichand;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText user;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText) findViewById(R.id.password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        Button enterButton = (Button) findViewById(R.id.button_enter);
        enterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String pas = password.getText().toString();

                user = (EditText) findViewById(R.id.user);
                String us = user.getText().toString();

                if (pas.equals("1234") && (us.equals("hand"))) {
                    password.setText(null);
                    user.setText(null);
                    password.setHint("Mot de passe");
                    user.setHint("Nom d'utilisateur");
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);

                }
                else{
                    password.setText(null);
                    password.setHint("Mot de passe");
                    user.setText(null);
                    user.setHint("Nom d'utilisateur");
                    Toast.makeText(MainActivity.this, "Wrong password or user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };
}
