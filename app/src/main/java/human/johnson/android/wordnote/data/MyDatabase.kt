package human.johnson.android.wordnote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.model.Shelf

@Database(entities = [Shelf::class, Note::class], version = 1, exportSchema = false)
abstract class MyDatabase: RoomDatabase() {

    abstract fun shelfDao(): MyDao

    companion object{
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return  tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "shelf_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

