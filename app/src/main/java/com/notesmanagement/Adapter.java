package com.notesmanagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.preference.MultiSelectListPreference;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Notes> notes = new ArrayList<>();
    private List<Notes> notelistAll;
    private ActionMode mactionMode;
    MainActivity mainActivity;
    Context context;

    private boolean multiSelect = false;

    private List<Notes> selectedItems = new ArrayList<>();

   /* private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.delete_hidden, menu);
            mode.setTitle("0 Notes Selected");
            return  true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_hidden:
                    Log.d("delete","pressed");
                   // Toast.makeText("this","Pressed",Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
          mactionMode = null;
        }
    };

*/


    public Adapter(Context context, List<Notes> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.notelistAll = new ArrayList<>(notes);
        mainActivity = (MainActivity) context;

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view, mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {

        holder.nTitle1.setText(notes.get(position).get_title());
        holder.nDate1.setText(notes.get(position).get_dateOfCreation());
        holder.nTime1.setText(notes.get(position).get_time());

        if (!mainActivity.is_in_action_mode) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
        }

        //holder.update(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Notes> filteredlist = new ArrayList<>();

            if ((constraint.toString().isEmpty())) {
                filteredlist.addAll(notelistAll);
            } else {
                Notes n = new Notes();

                for (Notes n1 : notelistAll) {
                    if (n1.get_title().toLowerCase().contains(constraint.toString().toLowerCase())
                            || n1.get_content().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredlist.add(n1);
                    }

                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((Collection<? extends Notes>) results.values);
            notifyDataSetChanged();
        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nTitle1, nDate1, nTime1;
        LinearLayout layout;
        CheckBox checkBox;
        MainActivity mainActivity;
        CardView cardView;


        public ViewHolder(View itemView, MainActivity mainActivity) {
            super(itemView);
            nTitle1 = (TextView) itemView.findViewById(R.id.nTitle);
            nDate1 = (TextView) itemView.findViewById(R.id.nDate);
            nTime1 = (TextView) itemView.findViewById(R.id.nTime);
            layout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            checkBox = (CheckBox) itemView.findViewById(R.id.chk);
            this.mainActivity = mainActivity;
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardView.setOnLongClickListener(mainActivity);
            checkBox.setOnClickListener(this);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Details.class);
                    intent.putExtra("ID", notes.get(getAdapterPosition()).get_id());
                    v.getContext().startActivity(intent);

                }
            });
           /* itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mactionMode != null) {
                        return false;
                    }
                    mactionMode = v.startActionMode(actionModeCallbacks);
                    v.setSelected(true);
                    return true;

                }
            });
        }*/
       /* void selectItem(Notes n){
            if (multiSelect)
            {
                if (selectedItems.contains(n))
                {
                    selectedItems.remove(n);
                    layout.setBackgroundColor(Color.BLUE);
                }else {
                    selectedItems.add(n);
                    layout.setBackgroundColor(Color.CYAN);
                }
            }
        }*/


       /* void update(final Notes notes){

            if (selectedItems.contains(notes)){
              layout.setBackgroundColor(Color.GREEN);
            }else {
                layout.setBackgroundColor(Color.GRAY);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity) v.getContext()).startSupportActionMode((androidx.appcompat.view.ActionMode.Callback) actionModeCallbacks);

                    selectItem(notes);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(notes);
                }
            });
*/
        }

        @Override
        public void onClick(View v) {
         mainActivity.prepareSelection(v, getAdapterPosition());
        }
    }

}

