package com.notesmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Edit extends AppCompatActivity {
    EditText noteTitle, noteDetails;
    String todayDate;
    NotesManagementDatabase database;
    Button edit;
    Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }
        Intent intent = getIntent();
        long id1 = intent.getLongExtra("ID", 0);

        database = new NotesManagementDatabase(this);
        note = database.getOneNote(id1);

        noteTitle = findViewById(R.id.note_title);
        noteDetails = findViewById(R.id.note_details);

        edit = findViewById(R.id.saveEditButton);
        getSupportActionBar().setTitle(note.get_title());
        noteTitle.setText(note.get_title());
        noteDetails.setText(note.get_content());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noteTitle.getText().toString().isEmpty() && !noteDetails.getText().toString().isEmpty()){
                    if(noteTitle.getText().toString().equals(note.get_title()) && noteDetails.getText().toString().equals(note.get_content())){
                        showToastMethod("Please make the changes to Update Note Successfully");
                    }else {
                        note.set_title(noteTitle.getText().toString());
                        note.set_content(noteDetails.getText().toString());
                        note.set_dateOfCreation(todayDate);
                        database.editNote(note);
                        showToastMethod("Note Update Successfully");
                        gotoMain();
                    }
                } else {
                    showToastMethod("Empty Note can not be Save Successfully");
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
    private void gotoMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void showToastMethod(String message){
        View toastView = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.linLay));
        TextView textViewToShowMsg= toastView.findViewById(R.id.textViewMsgForToast);
        textViewToShowMsg.setText(message);
        Toast toast=new Toast(Edit.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastView);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }



}

