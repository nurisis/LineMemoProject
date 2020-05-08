package com.hinuri.linememoproject.data.local

import android.content.Context
import androidx.room.*
import com.hinuri.linememoproject.data.entity.Memo
import com.hinuri.linememoproject.data.util.RoomConverters

@Database(entities = [Memo::class], version = 7, exportSchema = false)
@TypeConverters(RoomConverters::class)

abstract class LineDatabase : RoomDatabase() {

    abstract fun memoDao() : MemoDao

    /**
     * Double-check Locking 적용
     * */
    companion object{
        private var INSTANCE : LineDatabase? = null // Double checked

        fun getDatabase(context: Context) : LineDatabase {
            if(INSTANCE == null) {  // Single Checked

                synchronized(LineDatabase::class.java) {
                    if(INSTANCE == null) // Double checked
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            LineDatabase::class.java,
                            "line_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                }
                
            }

            return INSTANCE!!
        }
    }

    /**
     * 기존 코드
     * */
//    companion object{
//
//        @Volatile
//        private var INSTANCE : LineDatabase? = null
//
//        fun getDatabase(context: Context) : LineDatabase {
//
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    LineDatabase::class.java,
//                    "line_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//
//                INSTANCE = instance
//                instance
//            }
//
//        }
//    }
}