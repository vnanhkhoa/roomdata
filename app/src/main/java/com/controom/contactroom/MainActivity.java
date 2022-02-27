package com.controom.contactroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.controom.contactroom.adapter.RecyclerViewAdapter;
import com.controom.contactroom.model.Contact;
import com.controom.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contacList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);
        contactViewModel.getAllContacts().observe(this, contacts -> {

            //Khoi tao adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this);
            recyclerView.setAdapter(recyclerViewAdapter);
//            }
        });


        FloatingActionButton fab = findViewById(R.id.addContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewContact.class);
                startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION);

            assert name != null;
            Contact contact  = new Contact(name,occupation);

            ContactViewModel.insert(contact);
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            Contact contact = contactViewModel.getAllContacts().getValue().get(position);
            contactViewModel.delete(contact);
            recyclerViewAdapter.notifyDataSetChanged();
            Snackbar.make(recyclerView,"Đã Xóa", BaseTransientBottomBar.LENGTH_SHORT).setAction("Hủy", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contactViewModel.insert(contact);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_dark))
                    .setActionIconTint(Color.WHITE)
                    .setSwipeLeftLabelColor(Color.WHITE)
                    .setSwipeLeftLabelTextSize(1,18)
                    .addSwipeLeftLabel("Xóa")
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}