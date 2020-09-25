package human.johnson.android.wordnote.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

class ShelfAndNote {
    @Embedded
    lateinit var shelf: Shelf

    @Relation(parentColumn = "id", entityColumn = "shelfId")
    lateinit var note: List<Note>
}