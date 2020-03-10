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
    Adapter adapter;
    List<Notes> notes = new ArrayList<>();
    List<Notes> multiselect_list = new ArrayList<>();
    Button addition;
    RelativeLayout layout;
    long id;
    Notes recover;
    private List<Notes> notelistAll=notes;
    boolean isMultiSelect = false;
    private ActionMode mactionMode;
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

            switch (item.getItemId()) {
                case R.id.delete_hidden:
                    Log.d("delete", "pressed");
                    if (multiselect_list.size() > 0) {
                        final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                        for (int i = 0; i < multiselect_list.size(); i++) {
                            Long idToDelete = multiselect_list.get(i).get_id();
                            notes.remove(multiselect_list.get(i));
                            Long a = db.deleteNote(idToDelete);
                            Log.d("delete", "  " + a);
                            adapter.notifyItemRemoved(i);
                            refreshList();

                        }
                        mode.finish();
                    }
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiselect_list.clear();
            isMultiSelect = false;
            adapter.notifyDataSetChanged();
            addition.setVisibility(View.VISIBLE);
            mactionMode = null;

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
        NotesManagementDatabase database = new NotesManagementDatabase(this);
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

                                final NotesManagementDatabase db = new NotesManagementDatabase(getApplicationContext());
                                final long idToDelete = notes.get(pos).get_id();
                                Log.d("id to delete", "id "+idToDelete);
                                recover = db.getOneNote(idToDelete);
                                db.deleteNote(idToDelete);
                                notes.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                List<Notes>  notelist=db.getListOfNotes();
                                adapter.setListOfAllNotes(notelist);


                               // refreshList();

                              final   Snackbar snackbar = Snackbar.make(layout, "Removed from list", Snackbar.LENGTH_LONG);

                              snackbar.setAction("UNDO", new View.OnClickListener() {
                                    public void onClick(View view) {
                                    view.setEnabled(false);
                                       view.setClickable(false);
                                        if (recover!=null) {
                                            id = db.addNoteInDatabaseWhenSwiped(new Notes(recover.get_title(), recover.get_content(), recover.get_dateOfCreation()));
                                            Log.d("id to delete", "data " + recover.get_id() + " " + recover.get_title() + " " + recover.get_content() + " " + recover.get_dateOfCreation() );
                                            recover = db.getOneNote(id);
                                            notes.add(pos, recover);
                                            adapter.notifyItemInserted(pos);
                                            List<Notes>  notelist=db.getListOfNotes();
                                            adapter.setListOfAllNotes(notelist);


                                        }
                                       // refreshList();
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID", notes.get(position).get_id());
                    view.getContext().startActivity(intent);

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Notes>();
                    isMultiSelect = true;
                    addition.setVisibility(View.GONE);
                    if (mactionMode == null) {
                        mactionMode = startActionMode(actionModeCallbacks);
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


        if (mactionMode != null) {
            if (multiselect_list.contains(notes.get(position))) {
                multiselect_list.remove((notes.get(position)));

            } else {
                {
                    multiselect_list.add(notes.get(position));

                }
            }
            if (multiselect_list.size() > 0) {
                mactionMode.setTitle(multiselect_list.size() + " Notes Selected");


            } else {
                mactionMode.setTitle("Select Notes");

            }
            adapter.setSelectedIds(multiselect_list);
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
        multiselect_list.clear();
        mactionMode = null;
        adapter.notifyDataSetChanged();
        addition.setVisibility(View.VISIBLE);

    }

    private void refreshList(){
        NotesManagementDatabase database = new NotesManagementDatabase(this);
        notes = database.getListOfNotes();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);

    }

}
