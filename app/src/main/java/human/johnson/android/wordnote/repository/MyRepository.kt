package human.johnson.android.wordnote.repository

import androidx.lifecycle.LiveData
import human.johnson.android.wordnote.data.MyDao
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.model.Shelf

class MyRepository(private val MyDao: MyDao) {

    fun readShelfData(is_recent: Boolean, str: String?): LiveData<List<Shelf>> {
        return MyDao.readShelfData(is_recent, str)
    }

    fun readNoteData(id: Int, is_recent: Boolean, str: String?): LiveData<List<Note>> {
        return MyDao.readNoteData(id, is_recent, str)
    }

    fun readNoteStarData(id: Int, is_recent: Boolean, is_star: Boolean, str: String?): LiveData<List<Note>> {
        return MyDao.readNoteStarData(id, is_recent, is_star, str)
    }

    fun readNoteCheckData(id: Int, is_recent: Boolean, is_check: Boolean, str: String?): LiveData<List<Note>> {
        return MyDao.readNoteCheckData(id, is_recent, is_check, str)
    }

    suspend fun addShelf(shelf: Shelf) {
        MyDao.addShelf(shelf)
    }

    suspend fun addNote(note: Note) {
        MyDao.addNote(note)
    }

    suspend fun updateShelf(shelf: Shelf) {
        MyDao.updateShelf(shelf)
    }

    suspend fun updateNote(note: Note) {
        MyDao.updateNote(note)
    }

    suspend fun updateNoteFront(id: Int, is_front: Boolean) {
        MyDao.updateNoteFront(id, is_front)
    }

    suspend fun updateNoteNum(id: Int, num: Int) {
        MyDao.updateNoteNum(id, num)
    }

    suspend fun resetNoteNum(id: Int) {
        MyDao.resetNoteNum(id)
    }

    suspend fun updateNoteCheck(id: Int, is_check: Boolean) {
        MyDao.updateNoteCheck(id, is_check)
    }

    suspend fun deleteShelf(shelf: Shelf) {
        MyDao.deleteShelf(shelf)
    }

    suspend fun deleteNote(note: Note) {
        MyDao.deleteNote(note)
    }

    suspend fun deleteAllShelves() {
        MyDao.deleteALLShelves()
    }

    suspend fun deleteAllNotes(id: Int) {
        MyDao.deleteAllNotes(id)
    }

    suspend fun deleteAllNotesInDb() {
        MyDao.deleteAllNotesInDb()
    }
}