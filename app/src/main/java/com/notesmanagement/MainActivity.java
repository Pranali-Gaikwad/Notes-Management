package com.notesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    List<Notes> notes;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        return true;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.review);
        NotesManagementDatabase db1=new NotesManagementDatabase(this);

        notes = db1.getListOfNotes();

       recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new Adapter(this, notes);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


       // ArrayList<ModelNAme> list = getList();
       // List<Notes> no;
        Collections.reverse(notes);

    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addButton) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}

