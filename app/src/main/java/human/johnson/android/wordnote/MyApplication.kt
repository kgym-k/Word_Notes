package human.johnson.android.wordnote

import android.app.Application

class MyApplication: Application() {
    companion object {
        var currentId: Int = -1
        var star_flag: Boolean = false
        var check_flag: Boolean = false
    }
}