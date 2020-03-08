package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddActivity extends AppCompatActivity {

    TextView dateshow;
    EditText noteTitle;
    EditText noteDetails;
    String todaysDate;
    Button saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        todaysDate = sdf.format(new Date());
        noteTitle = findViewById(R.id.note_title);
        noteDetails = findViewById(R.id.note_details);
        dateshow = findViewById(R.id.dateadd);
        saved = findViewById(R.id.saveDataButton);



        dateshow.setText(String.valueOf(todaysDate));


        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isEmpty(noteTitle)) || (!isEmpty(noteDetails))) {
                    NotesManagementDatabase db = new NotesManagementDatabase(v.getContext());
                     db.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate));
                    Toast.makeText(v.getContext(), "Note Successfully Saved " , Toast.LENGTH_SHORT).show();
                    gotoMain();
                } else {
                    Toast.makeText(v.getContext(), " Empty Note can not be Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }



    private String pad(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {

            Toast.makeText(this, "Note not save", Toast.LENGTH_SHORT).show();
            gotoMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        if ((!isEmpty(noteTitle)) || (!isEmpty(noteDetails))) {
            NotesManagementDatabase db = new NotesManagementDatabase(this);
            db.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate));
            Toast.makeText(getApplicationContext(), "Note Successfully Saved", Toast.LENGTH_SHORT).show();
        }

        gotoMain();
        super.onBackPressed();

    }
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
}
