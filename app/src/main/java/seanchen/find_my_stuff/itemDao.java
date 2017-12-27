package seanchen.find_my_stuff;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by John- on 12/25/2017.
 */
@Dao
public interface itemDao {
    @Insert
    void insertAll(antiLossItem... item);

    @Delete
    void delete(antiLossItem item);

    // Gets all people in the database
    @Query("SELECT * FROM antiLossItem")
    List<antiLossItem> getAllPeople();

    // Gets all people in the database with a favorite color
//    @Query("SELECT * FROM antiLossItem WHERE Date LIKE :color")
//    List<antiLossItem> getAllPeopleWithFavoriteColor(String color);

}
