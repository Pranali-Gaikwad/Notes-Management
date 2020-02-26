package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
    EditText noteTitle;
    EditText noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;
    NotesManagementDatabase db;
    Button edit;
    Notes n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

        Intent intent = getIntent();
        Long id1 = intent.getLongExtra("ID", 0);
        db = new NotesManagementDatabase(this);
        n = db.getOneNote(id1);

        noteTitle = findViewById(R.id.note_title);
        noteDetails = findViewById(R.id.note_details);

        edit = findViewById(R.id.saveEditButton);
        getSupportActionBar().setTitle(n.get_title());
        noteTitle.setText(n.get_title());
        noteDetails.setText(n.get_content());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isEmpty(noteTitle)) || (!isEmpty(noteDetails))) {
                    n.set_title(noteTitle.getText().toString());
                    n.set_content(noteDetails.getText().toString());
                    n.set_dateOfCreation(todaysDate);
                    n.set_time(currentTime);
                    int i = db.editNote(n);
                    if (i == n.get_id()) {
                    } else {
                        Toast.makeText(v.getContext(), "Note Update Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Empty Note can not be Save Successfully", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("ID", n.get_id());
                startActivity(intent);


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


        calendar = Calendar.getInstance();
        todaysDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE)) +" "+ amAndPm();
        Log.d("calender", " Date and Time " + todaysDate + " and " + currentTime);

    }
    private String amAndPm() {
        if(calendar.get(Calendar.AM_PM) == Calendar.AM){
            return "AM";

        }else{
            return "PM";
        }
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

            Toast.makeText(this, "Note not Update Successfully", Toast.LENGTH_SHORT).show();
            gotoMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        super.onBackPressed();

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);

    }
}

