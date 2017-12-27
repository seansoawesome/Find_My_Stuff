package seanchen.find_my_stuff;
/**
 * Created by John- on 12/26/2017.
 */

/**

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
@Database(entities = {antiLossItem.class}, version = 1)
//@TypeConverters(antiLossItem.class)
public abstract class AppDataBase extends RoomDatabase
{
    private static AppDataBase INSTANCE;
    public abstract itemDao daoAccess();

    public AppDataBase()
    {

    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2)
    {
        @Override
        public void migrate(SupportSQLiteDatabase database)
        {
         // Since we didn't alter the table, there's nothing else to do here.
        }
    };


}
 */
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

/**
 * The Room database that contains the Users table
 */
@Database(entities = {antiLossItem.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase INSTANCE;

    public abstract itemDao userDao();

    private static final Object sLock = new Object();

    /**
     * Migrate from:
     * version 1 - using the SQLiteDatabase API
     * to
     * version 2 - using Room
     */
    @VisibleForTesting
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Room uses an own database hash to uniquely identify the database
            // Since version 1 does not use Room, it doesn't have the database hash associated.
            // By implementing a Migration class, we're telling Room that it should use the data
            // from version 1 to version 2.
            // If no migration is provided, then the tables will be dropped and recreated.
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    public static AppDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, "Sample.db")
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

}