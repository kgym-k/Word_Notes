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
    val noteRecentCheck: LiveData<List<Note>>
    val noteFrontCheck: LiveData<List<Note>>
    val noteRecentNotStar: LiveData<List<Note>>
    val noteFrontNotStar: LiveData<List<Note>>
    val noteRecentNotCheck: LiveData<List<Note>>
    val noteFrontNotCheck: LiveData<List<Note>>

    val str = MutableLiveData<String>()
    val note = MediatorLiveData<List<Note>>()

    private val prif = getApplication<Application>()
        .getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    private var is_recent = prif.getBoolean("IS_RECENT_NOTE", true)
    private var is_star = false
    private var is_check = false
    private var not_star = false
    private var not_check = false

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
            return@switchMap repository.readNoteStarData(currentId,
                is_recent = true,
                is_star = true,
                str = it
            )
        }
        noteFrontStar = Transformations.switchMap(str) {
            return@switchMap repository.readNoteStarData(currentId,
                is_recent = false,
                is_star = true,
                str = it
            )
        }
        noteRecentCheck = Transformations.switchMap(str) {
            return@switchMap repository.readNoteCheckData(currentId,
                is_recent = true,
                is_check = true,
                str = it
            )
        }
        noteFrontCheck = Transformations.switchMap(str) {
            return@switchMap repository.readNoteCheckData(currentId,
                is_recent = false,
                is_check = true,
                str = it
            )
        }
        noteRecentNotStar = Transformations.switchMap(str) {
            return@switchMap  repository.readNoteStarData(currentId,
                is_recent = true,
                is_star = false,
                str = it
            )
        }
        noteFrontNotStar = Transformations.switchMap(str) {
            return@switchMap  repository.readNoteStarData(currentId,
                is_recent = false,
                is_star = false,
                str = it
            )
        }
        noteRecentNotCheck = Transformations.switchMap(str) {
            return@switchMap  repository.readNoteCheckData(currentId,
                is_recent = true,
                is_check = false,
                str = it)
        }
        noteFrontNotCheck = Transformations.switchMap(str) {
            return@switchMap  repository.readNoteCheckData(currentId,
                is_recent = false,
                is_check = false,
                str = it)
        }

        note.addSource(noteRecent) { result ->
            if (is_recent && !is_star && !is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFront) { result ->
            if (!is_recent && !is_star && !is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteRecentStar) { result ->
            if (is_recent && is_star && !is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFrontStar) { result ->
            if (!is_recent && is_star && !is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteRecentCheck) { result ->
            if (is_recent && !is_star && is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFrontCheck) { result ->
            if (!is_recent && !is_star && is_check && !not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteRecentNotStar) { result ->
            if (is_recent && !is_star && !is_check && not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFrontNotStar) { result ->
            if (!is_recent && !is_star && !is_check && not_star && !not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteRecentNotCheck) { result ->
            if (is_recent && !is_star && !is_check && !not_star && not_check) {
                result?.let { note.value = it }
            }
        }
        note.addSource(noteFrontNotCheck) { result ->
            if (!is_recent && !is_star && !is_check && !not_star && not_check) {
                result?.let { note.value = it }
            }
        }
    }

    fun rearrangeNote(_is_recent: Boolean, _is_star: Boolean, _is_check: Boolean, _not_star: Boolean, _not_check: Boolean) =
        when (_is_recent) {
            true -> {
                when {
                    _is_star -> noteRecentStar.value?.let { note.value = it }
                    _is_check -> noteRecentCheck.value?.let { note.value = it }
                    _not_star -> noteRecentNotStar.value?.let { note.value = it }
                    _not_check -> noteRecentNotCheck.value?.let { note.value = it }
                    else -> noteRecent.value?.let { note.value = it }
                }
            }
            false -> {
                when {
                    _is_star -> noteFrontStar.value?.let { note.value = it }
                    _is_check -> noteFrontCheck.value?.let { note.value = it }
                    _not_star -> noteFrontNotStar.value?.let { note.value = it }
                    _not_check -> noteFrontNotCheck.value?.let { note.value = it }
                    else -> noteFront.value?.let { note.value = it }
                }
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

    fun setRecent(_is_recent: Boolean) {
        is_recent = _is_recent
    }

    fun setStar(_is_star: Boolean) {
        is_star = _is_star
    }

    fun setCheck(_is_check: Boolean) {
        is_check = _is_check
    }

    fun setNotStar(_not_star: Boolean) {
        not_star = _not_star
    }

    fun setNotCheck(_not_check: Boolean) {
        not_check = _not_check
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

    fun updateNoteCheck(id: Int, is_check: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNoteCheck(id, is_check)
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
}