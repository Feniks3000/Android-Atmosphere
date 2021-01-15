package ru.geekbrains.atmosphere.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    @Update
    void update(History history);

    @Delete
    void delete(History history);

    @Query("SELECT * FROM HISTORY ORDER BY DATE DESC")
    History[] selectAll();

    @Query("SELECT ID FROM HISTORY WHERE date = :date AND city = :city")
    long selectByDateAndCity(long date, String city);

    @Query("SELECT COUNT() FROM history")
    long getCount();
}
