package com.controom.contactroom.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.controom.contactroom.data.ContactDao;
import com.controom.contactroom.model.Contact;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Database(entities = {Contact.class},version = 1,exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public static final int NUMBER_OF_THREADS = 4;
    
    private static volatile ContactRoomDatabase INSTANCES;
    public static final ExecutorService databaseWriteExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final Context context){
        if (INSTANCES == null) {
            synchronized (ContactRoomDatabase.class){
                if(INSTANCES == null){
                    INSTANCES = Room.databaseBuilder(context.getApplicationContext(),
                            ContactRoomDatabase.class,"contact_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCES;
    }
    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    databaseWriteExecutor.execute(() ->{
                        ContactDao contactDao = INSTANCES.contactDao();
                        contactDao.deleteAll();


                    });
                }
            };

}
