package com.controom.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.controom.contactroom.model.ContactViewModel;

public class NewContact extends AppCompatActivity {
    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION = "occupation";
    private EditText edtName;
    private EditText edtOccupation;
    private Button btnSave;

    private ContactViewModel contactViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        edtName = findViewById(R.id.edtName);
        edtOccupation = findViewById(R.id.edtOccupation);
        btnSave = findViewById(R.id.btnSave);


        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this
                .getApplication())
                .create(ContactViewModel.class);

        btnSave.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if(!TextUtils.isEmpty(edtName.getText())
                    && !TextUtils.isEmpty(edtOccupation.getText())){
                String name = edtName.getText().toString();
                String occupation = edtOccupation.getText().toString();
//              Contact contact = new Contact(edtName.getText().toString(),
//                        edtOccupation.getText().toString());
                replyIntent.putExtra(NAME_REPLY,name);
                replyIntent.putExtra(OCCUPATION,occupation);
                setResult(RESULT_OK,replyIntent);
                //  ContactViewModel.insert(contact);
            } else {
                setResult(RESULT_CANCELED,replyIntent);
            }
            finish();
        });
    }
}