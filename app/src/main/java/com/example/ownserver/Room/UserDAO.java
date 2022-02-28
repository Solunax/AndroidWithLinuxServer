package com.example.ownserver.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUserData(User user);

    @Query("SELECT * FROM user")
    Single<User> getInfo();
}
