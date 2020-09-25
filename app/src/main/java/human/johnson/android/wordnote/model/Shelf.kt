package human.johnson.android.wordnote.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "shelf_table")
data class Shelf(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val subject: String,
    val color: Int,
    val noteNum: Int = 0
): Parcelable