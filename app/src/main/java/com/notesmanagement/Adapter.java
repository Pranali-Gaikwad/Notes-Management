package com.notesmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private LayoutInflater inflater;
    private List<Notes> notelistAll;

    Context context;
    private List<Notes> notes = new ArrayList<>();
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
    private List<Notes> selectedItems = new ArrayList<>();

    public Adapter(Context context, List<Notes> notes, List<Notes> selectedItems) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
        this.selectedItems = selectedItems;
        this.notelistAll = new ArrayList<>(notes);



    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter.ViewHolder holder, int position) {

        holder.nTitle1.setText(notes.get(position).get_title());
        holder.nDate1.setText(notes.get(position).get_dateOfCreation());
        holder.nTime1.setText(notes.get(position).get_time());


    }


    @Override
    public int getItemCount() {
        return notes.size();
    }
    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nTitle1, nDate1, nTime1;
        LinearLayout layout;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            nTitle1 = itemView.findViewById(R.id.nTitle);
            nDate1 = itemView.findViewById(R.id.nDate);
            nTime1 = itemView.findViewById(R.id.nTime);
            layout = itemView.findViewById(R.id.linearLayout);
            cardView= itemView.findViewById(R.id.cardView);
        }
    }

}

