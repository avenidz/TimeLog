package com.example.timelog.logdata

import android.content.Context
import androidx.room.*
import java.util.*

@Entity(tableName = "logDataUser")
data class LogDataUser(
    @PrimaryKey(autoGenerate = true) var logId: Int,
    @ColumnInfo(name = "logUsername") var logUser: String = "",
    @ColumnInfo(name = "logPassword") var logPass: String = "",
    @ColumnInfo(name = "logEmail") var logEmail: String = "",
    @ColumnInfo(name = "logAlready") var loggedUser: Boolean = false
)

@Entity(tableName = "userTimeLog")
data class UserTimeLog(
    @PrimaryKey(autoGenerate = true) var timeId: Int,
    @ColumnInfo(name = "logId") var logUserId: Int,
    @ColumnInfo(name = "logTitle") var timeLogTitle: String = "",
    @ColumnInfo(name = "logDescription") var timeLogDescription: String = "",
    @ColumnInfo(name = "logTimeIn") var timeLogIn: String = "",
    @ColumnInfo(name = "logTimeOut") var timeLogOut: String = "",
    @ColumnInfo(name = "logTimeDate") var timeLogDate: String = ""
)

@Database(entities = [LogDataUser::class, UserTimeLog::class], version = 1, exportSchema = false)
abstract class LogDataUserDatabase: RoomDatabase(){

    abstract fun logUserDao(): LogDataDao
    companion object{
        private val databaseName= "timeLogDatabase"
        private var INSTANCE: LogDataUserDatabase? = null

        fun getDatabaseInstance(context: Context): LogDataUserDatabase{
            var instance = INSTANCE
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LogDataUserDatabase::class.java,
                    databaseName
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }

}