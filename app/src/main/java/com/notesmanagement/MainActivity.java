package com.notesmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    List<Notes> notes;
    Button addition;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

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

        Collections.reverse(notes);


        addition=(Button)findViewById(R.id.additionButton);

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AddActivity.class);
                startActivity(intent);
            }
        });

    }


   /* public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addButton) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }*/
}

