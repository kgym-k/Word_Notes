package human.johnson.android.wordnote.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.model.Shelf

@Dao
interface MyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addShelf(shelf: Shelf)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateShelf(shelf: Shelf)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteShelf(shelf: Shelf)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("DELETE FROM shelf_table")
    suspend fun deleteALLShelves()

    @Query("UPDATE note_table SET front = :is_front WHERE shelfId = :id")
    suspend fun updateNoteFront(id: Int, is_front: Boolean)

    @Query("UPDATE shelf_table SET noteNum = noteNum + :num WHERE id = :id")
    suspend fun updateNoteNum(id: Int, num: Int)

    @Query("UPDATE shelf_table SET noteNum = 0 WHERE id = :id")
    suspend fun resetNoteNum(id: Int)

    @Query("DELETE FROM note_table WHERE shelfId = :id")
    suspend fun deleteAllNotes(id: Int)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotesInDb()

    @Query("SELECT * FROM shelf_table WHERE subject LIKE :str ORDER BY " +
            "CASE WHEN :is_recent = 1 THEN id END DESC, CASE WHEN :is_recent = 0 THEN subject END")
    fun readShelfData(is_recent: Boolean, str: String?): LiveData<List<Shelf>>

    @Query("SELECT * FROM (SELECT * FROM note_table WHERE shelfId = :id) " +
            "WHERE word LIKE :str OR meaning LIKE :str OR memo LIKE :str " +
            "ORDER BY CASE WHEN :is_recent = 1 THEN id END DESC, CASE WHEN :is_recent = 0 THEN word END")
    fun readNoteData(id: Int, is_recent: Boolean, str: String?): LiveData<List<Note>>

    @Query("SELECT * FROM (SELECT * FROM note_table WHERE shelfId = :id AND star = :is_star) " +
            "WHERE word LIKE :str OR meaning LIKE :str OR memo LIKE :str " +
            "ORDER BY CASE WHEN :is_recent = 1 THEN id END DESC, CASE WHEN :is_recent = 0 THEN word END")
    fun readNoteStarData(id: Int, is_recent: Boolean, is_star: Boolean, str: String?): LiveData<List<Note>>
}