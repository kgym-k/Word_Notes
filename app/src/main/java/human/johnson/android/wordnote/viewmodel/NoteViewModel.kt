package human.johnson.android.wordnote.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import human.johnson.android.wordnote.data.MyDatabase
import human.johnson.android.wordnote.repository.MyRepository
import human.johnson.android.wordnote.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import human.johnson.android.wordnote.MyApplication.Companion.currentId

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MyRepository

    val noteRecent: LiveData<List<Note>>
    val noteFront:  LiveData<List<Note>>
    val noteRecentStar: LiveData<List<Note>>
    val noteFrontStar: LiveData<List<Note>>

    val str = MutableLiveData<String>()
    val note = MediatorLiveData<List<Note>>()

    private val prif = getApplication<Application>()
        .getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    private var is_recent = prif.getBoolean("IS_RECENT_NOTE", true)
    private var is_star = false

    init {
        val shelfDao = MyDatabase.getDatabase(
            application
        ).shelfDao()
        repository = MyRepository(shelfDao)
        str.value = "%"
        noteRecent = Transformations.switchMap(str) {
            return@switchMap repository.readNoteData(currentId, true, it)
        }
        noteFront = Transformations.switchMap(str) {
            return@switchMap repository.readNoteData(currentId, false, it)
        }
        noteRecentStar = Transformations.switchMap(str) {
            return@switchMap repository.readNoteStarData(currentId, true, true, it)
        }
        noteFrontStar = Transformations.switchMap(str) {
            return@switchMap repository.readNoteStarData(currentId, false, true, it)
        }

        note.addSource(noteRecent) { result ->
            if (is_recent && !is_star) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFront) { result ->
            if (!is_recent && !is_star) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteRecentStar) { result ->
            if (is_recent && is_star) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFrontStar) { result ->
            if (!is_recent && is_star) {
                result?.let { note.value = it }
            }
        }
    }

    fun rearrangeNote(_is_recent: Boolean, _is_star: Boolean) = when (_is_recent) {
        true -> when (_is_star) {
            true -> noteRecentStar.value?.let { note.value = it }
            false -> noteRecent.value?.let { note.value = it }
        }
        false -> when (_is_star) {
            true -> noteFrontStar.value?.let { note.value = it }
            false -> noteFront.value?.let { note.value = it }
        }
    }.also {
        is_recent = _is_recent
        prif.edit().putBoolean("IS_RECENT_NOTE", is_recent).commit()
    }

    fun getIsRecent(): Boolean {
        return is_recent
    }

    fun setStr(_str: String?) {
        str.value = _str
    }

    fun setStar(_is_star: Boolean) {
        is_star = _is_star
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
        }
    }

    fun deleteAllNotes(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes(id)
        }
    }

    fun updateNoteFront(id: Int, is_front: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNoteFront(id, is_front)
        }
    }

    fun updateNoteNum(id: Int, num: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNoteNum(id, num)
        }
    }

    fun resetNoteNum(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.resetNoteNum(id)
        }
    }
}