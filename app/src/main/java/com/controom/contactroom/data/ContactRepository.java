package com.controom.contactroom.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.controom.contactroom.model.Contact;
import com.controom.contactroom.util.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> allContacs;

    public ContactRepository(Application application){
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);
        contactDao = db.contactDao();

        allContacs = contactDao.getAllContact();

    }

    public LiveData<List<Contact>> getAllData(){
        return allContacs;
    }
    public void insert(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() ->{
            contactDao.inser(contact);
        });
    }
    public LiveData<Contact> get(int id){
        return contactDao.get(id);
    }
    public void update(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(()->
                contactDao.update(contact));
    }
    public void delete(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(() ->{
                contactDao.delete(contact);
        });
    }


}
