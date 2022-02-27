package com.controom.contactroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.controom.contactroom.R;
import com.controom.contactroom.data.ContactRepository;
import com.controom.contactroom.model.Contact;
import com.controom.contactroom.model.ContactViewModel;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Contact> contacList;
    private Context context;
    private ContactViewModel repository;

    public RecyclerViewAdapter(List<Contact> contacList, Context context) {
        this.contacList = contacList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = Objects.requireNonNull(contacList.get(position));
        holder.txtName.setText(contact.getName());
        holder.txtOccupation.setText(contact.getOccupation());
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(contacList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtOccupation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtOccupation = itemView.findViewById(R.id.txtOccupation);
        }
    }
}
