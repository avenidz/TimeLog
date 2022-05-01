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
    fun updateUser(logDataUser: LogDataUser)




    @Query("SELECT * FROM userTimeLog WHERE logId=:loggedId ORDER BY timeId ASC")
    fun getUserTimeLog(loggedId: Int): List<UserTimeLog>

    @Query("SELECT * FROM userTimeLog WHERE logId=:loggedId AND logTimeDate=:loggedDate ORDER BY timeId ASC")
    fun getUserTimeLogByDate(loggedId: Int, loggedDate: String): List<UserTimeLog>

    @Query("SELECT * FROM userTimeLog WHERE timeId=:logDetails")
    fun getLogDetailsById(logDetails: Int): List<UserTimeLog>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveTimeLog(userTimeLog: UserTimeLog)

    @Update
    fun updateLogPending(userTimeLog: UserTimeLog)

}