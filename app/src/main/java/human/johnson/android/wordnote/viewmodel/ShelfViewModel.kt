package human.johnson.android.wordnote.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import human.johnson.android.wordnote.MyApplication
import human.johnson.android.wordnote.data.MyDatabase
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.repository.MyRepository
import human.johnson.android.wordnote.model.Shelf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShelfViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MyRepository

    val shelfRecent: LiveData<List<Shelf>>
    val shelfName:  LiveData<List<Shelf>>

    val str = MutableLiveData<String>()
    val shelf = MediatorLiveData<List<Shelf>>()

    private val prif = getApplication<Application>()
        .getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    private var is_recent = prif.getBoolean("IS_RECENT_SHELF", true)

    init {
        val shelfDao = MyDatabase.getDatabase(
            application
        ).shelfDao()
        repository = MyRepository(shelfDao)
        str.value = "%"
        shelfRecent = Transformations.switchMap(str) {
            return@switchMap repository.readShelfData(true, it)
        }
        shelfName = Transformations.switchMap(str) {
            return@switchMap repository.readShelfData(false, it)
        }

        shelf.addSource(shelfRecent) { result ->
            if (is_recent) {
                result?.let { shelf.value = it }
            }
        }
        shelf.addSource(shelfName) { result ->
            if (!is_recent) {
                result?.let { shelf.value = it }
            }
        }
    }

    fun rearrangeNote(_is_recent: Boolean) = when (_is_recent) {
        true -> shelfRecent.value?.let { shelf.value = it }
        false -> shelfName.value?.let { shelf.value = it }
    }.also {
        is_recent = _is_recent
        prif.edit().putBoolean("IS_RECENT_SHELF", is_recent).commit()
    }

    fun getIsRecent(): Boolean {
        return is_recent
    }

    fun setStr(_str: String?) {
        str.value = _str
    }

    fun addShelf(shelf: Shelf) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addShelf(shelf)
        }
    }

    fun updateShelf(shelf: Shelf) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShelf(shelf)
        }
    }


    fun deleteShelf(shelf: Shelf) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShelf(shelf)
        }
    }

    fun deleteAllShelves() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllShelves()
        }
    }

    fun deleteAllNotes(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotes(id)
        }
    }

    fun deleteAllNotesInDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNotesInDb()
        }
    }
}