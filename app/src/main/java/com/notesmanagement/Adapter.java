package com.notesmanagement;

import android.content.Context;
import android.content.Intent;
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
        }

        @Override
        public void onClick(View v) {
         mainActivity.prepareSelection(v, getAdapterPosition());
        }
    }

}

