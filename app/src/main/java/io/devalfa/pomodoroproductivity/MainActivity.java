package io.devalfa.pomodoroproductivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText taskName = findViewById(R.id.TaskName);

        final ImageButton create = findViewById(R.id.createBtn);
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageButton createRef = findViewById(R.id.createBtn);
                EditText taskNameRef = findViewById(R.id.TaskName);
                if(taskNameRef.getText().toString().length() != 0) createRef.setImageDrawable(getResources().getDrawable(R.drawable.send_focus));
                else createRef.setImageDrawable(getResources().getDrawable(R.drawable.send));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText taskNameRef = findViewById(R.id.TaskName);
                if(taskNameRef.getText().toString().length()==0)
                {
                    Toast.makeText(MainActivity.this, R.string.toast_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent timer = new Intent(MainActivity.this, Timer.class);
                timer.putExtra("Session", taskNameRef.getText().toString());
                startActivity(timer);
                finish();
            }
        });
        taskName.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    create.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}
