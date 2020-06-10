package com.notesmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    TextView dateToShow;
    EditText noteTitle, noteDetails;
    Button saved;
    NotesManagementDatabase database;
    String todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle(R.string.new_note);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        todayDate = sdf.format(new Date());
        noteTitle = findViewById(R.id.note_title);
        noteDetails = findViewById(R.id.note_details);
        dateToShow = findViewById(R.id.dateadd);
        saved = findViewById(R.id.saveDataButton);
        dateToShow.setText(String.valueOf(todayDate));

        database = new NotesManagementDatabase(this);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noteTitle.getText().toString().isEmpty() && !noteDetails.getText().toString().isEmpty()){
                    addNewNoteInDatabase();
                    gotoMainActivity();
                }  else {
                    showToastMethod("Empty Note can not be Saved");
                }

            }
        });

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) getSupportActionBar().setTitle(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }

        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.delete){
            showToastMethod("Note not save");
            gotoMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onBackPressed() {
        if (!noteTitle.getText().toString().isEmpty() && !noteDetails.getText().toString().isEmpty()){
            addNewNoteInDatabase();
            gotoMainActivity();
        }
        else {
           super.onBackPressed();
        }
    }

    public void addNewNoteInDatabase()
    {
        database.addNoteInDatabase(new Notes(noteTitle.getText().toString(), noteDetails.getText().toString(), todayDate));
        showToastMethod("Note Successfully Saved");
    }
    public void showToastMethod(String message){
        View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.linLay));
        TextView textViewToShowMsg= toastView.findViewById(R.id.textViewMsgForToast);
        textViewToShowMsg.setText(message);
        Toast toast=new Toast(AddActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }
}
