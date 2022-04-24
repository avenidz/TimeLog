package com.example.timelog.logdata

import androidx.room.*

@Dao
interface LogDataDao {
    @Query("SELECT * FROM logDataUser")
    fun getUserList(): List<LogDataUser>

    @Query("SELECT * FROM logDataUser WHERE logUsername=:logUser")
    fun checkMatchUser(logUser: String): List<LogDataUser>

    @Query("SELECT * FROM logDataUser WHERE logAlready=:loggedUser")
    fun checkLoggedUser(loggedUser: Boolean = true): List<LogDataUser>

    @Query("SELECT * FROM logDataUser WHERE logUsername=:loggedUser AND logPassword=:loggedPassword")
    fun matchUsernamePassword(loggedUser: String, loggedPassword: String): List<LogDataUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUser(logDataUser: LogDataUser)

    @Update
    fun updateUser(LogDataUser: LogDataUser)
}