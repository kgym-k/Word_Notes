package human.johnson.android.wordnote.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "note_table",
        foreignKeys = arrayOf(ForeignKey(
                entity = Shelf::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("shelfId"),
                onDelete = ForeignKey.CASCADE)))
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val shelfId: Int,
    val word: String,
    val meaning: String,
    val memo: String,
    val front: Boolean,
    val star: Boolean,
    val checked: Boolean,
    val color: Int
): Parcelable