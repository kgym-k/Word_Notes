package human.johnson.android.wordnote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.model.Shelf

@Database(entities = [Shelf::class, Note::class], version = 4, exportSchema = false)
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
                )
                    .fallbackToDestructiveMigrationFrom(2)
                    .addMigrations(MIGRATION_1_4, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

val MIGRATION_1_4 = object: Migration(1, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, shelfId INTEGER NOT NULL, word TEXT NOT NULL, meaning TEXT NOT NULL, memo TEXT NOT NULL, front INTEGER NOT NULL, star INTEGER NOT NULL, color INTEGER NOT NULL, FOREIGN KEY (shelfId) REFERENCES shelf_table(id) ON UPDATE NO ACTION ON DELETE CASCADE) ")
        database.execSQL("INSERT INTO new (id, shelfId, word, meaning, memo, front, star, color) SELECT id, shelfId, word, meaning, memo, front, star, color FROM note_table")
        database.execSQL("DROP TABLE note_table")
        database.execSQL("ALTER TABLE new RENAME TO note_table")
        database.execSQL("ALTER TABLE note_table ADD COLUMN checked INTEGER DEFAULT 0 NOT NULL")
    }
}

val MIGRATION_3_4 = object: Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE note_table ADD COLUMN checked INTEGER DEFAULT 0 NOT NULL")
    }
}

