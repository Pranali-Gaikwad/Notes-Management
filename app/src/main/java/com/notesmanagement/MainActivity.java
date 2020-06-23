package com.notesmanagement;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout layout;
    Adapter adapter;
    List<Notes> notes = new ArrayList<>();
    List<Notes> multiSelectList = new ArrayList<>();
    Button addition;
    long id;
    Notes recover;
    boolean isMultiSelect = false;
    private ActionMode actionMode;
    List<Notes>  updatedListToPassSearch;
    NotesManagementDatabase database;
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_hidden, menu);
            mode.setTitle("0 Notes Selected");
            return true;
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.delete_hidden) {
                Log.d("delete", "pressed");
                if (multiSelectList.size() > 0) {
                    final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                    for (int i = 0; i < multiSelectList.size(); i++) {
                        long idToDelete = multiSelectList.get(i).get_id();
                         notes.remove(multiSelectList.get(i));
                        long a = db.deleteNote(idToDelete);
                        Log.d("delete", "  " + a);
                        adapter.notifyItemRemoved(i);
                        updatedListToPassSearch = db.getListOfNotes();
                        adapter.setListOfAllNotes(updatedListToPassSearch);
                    }
                    mode.finish();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelectList.clear();
            isMultiSelect = false;
            adapter.notifyDataSetChanged();
            addition.setVisibility(View.VISIBLE);
            actionMode = null;

        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resource, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        }

        layout = findViewById(R.id.main);
        recyclerView = findViewById(R.id.review);
        database = new NotesManagementDatabase(this);
        notes = database.getListOfNotes();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);

        MySwipeHelper swipeHelper=new MySwipeHelper(this, recyclerView,200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MySwipeHelper.MyButton> buffer) {

                buffer.add(new MyButton(MainActivity.this,
                        "delete",
                        30,
                     R.drawable.ic_delete_black_24dp,
                        Color.parseColor("#ff3c30"),
                        new  MyButtonClickListner(){
                            @Override
                            public void onClick(final int pos) {
                                final long idToDelete = notes.get(pos).get_id();
                                Log.d("id to delete", "id "+idToDelete);
                                recover = database.getOneNote(idToDelete);
                                database.deleteNote(idToDelete);
                                notes.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                updatedListToPassSearch=database.getListOfNotes();
                                adapter.setListOfAllNotes(updatedListToPassSearch);


                              final   Snackbar snackbar = Snackbar.make(layout, "Removed from list", Snackbar.LENGTH_LONG);
                              snackbar.setAction("UNDO", new View.OnClickListener() {
                                    public void onClick(View view) {
                                    view.setEnabled(false);
                                       view.setClickable(false);
                                        if (recover!=null) {
                                            id = database.addNoteInDatabaseWhenSwiped(new Notes(recover.get_title(), recover.get_content(), recover.get_dateOfCreation()));
                                            Log.d("id to delete", "data " + recover.get_id() + " " + recover.get_title() + " " + recover.get_content() + " " + recover.get_dateOfCreation() );
                                            recover = database.getOneNote(id);
                                            notes.add(pos, recover);
                                            adapter.notifyItemInserted(pos);
                                            updatedListToPassSearch=database.getListOfNotes();
                                            adapter.setListOfAllNotes(updatedListToPassSearch);
                                        }

                                    }

                                });
                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        }));

                buffer.add(new MyButton(MainActivity.this,
                        "edit",
                        30,
                        R.drawable.ic_mode_edit_black_24dp,
                        Color.GREEN,
                        new  MyButtonClickListner(){
                            @Override
                            public void onClick(int pos) {
                                Intent intent = new Intent(MainActivity.this, Edit.class);
                                intent.putExtra("ID", notes.get(pos).get_id());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }));
            }
        };



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    addition.setVisibility(View.GONE);
                    multi_select(position);
                } else {
                    Intent intent = new Intent(view.getContext(), Details.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("ID", notes.get(position).get_id());
                    view.getContext().startActivity(intent);

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (!isMultiSelect) {
                    multiSelectList = new ArrayList<>();
                    isMultiSelect = true;
                    addition.setVisibility(View.GONE);
                    if (actionMode == null) {
                        actionMode = startActionMode(actionModeCallbacks);
                    }
                }
                multi_select(position);

            }

        }));

        addition = findViewById(R.id.additionButton);

        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivity(intent);
            }
        });

    }

    public void multi_select(int position) {


        if (actionMode != null) {
            if (multiSelectList.contains(notes.get(position))) {
                multiSelectList.remove((notes.get(position)));

            } else {
                {
                    multiSelectList.add(notes.get(position));
                }
            }
            if (multiSelectList.size() > 0) {
                actionMode.setTitle(multiSelectList.size() + " Notes Selected");
            } else {
                actionMode.setTitle("Select Notes");
            }
            adapter.setSelectedIds(multiSelectList);

        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.folder) {
            Intent intent = new Intent(this, Folders.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onBackPressed() {
        if (isMultiSelect)
        {
            multiSelectList.clear();
            actionMode = null;
            adapter.notifyDataSetChanged();
            addition.setVisibility(View.VISIBLE);
        }
        else {
            finish();
        }


    }
}
