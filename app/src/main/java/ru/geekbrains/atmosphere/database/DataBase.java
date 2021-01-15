package ru.geekbrains.atmosphere.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {History.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract DAO getDAO();

}
