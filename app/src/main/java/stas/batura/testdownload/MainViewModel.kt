package stas.batura.testdownload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder

class MainViewModel: ViewModel() {

    val textValue: MutableLiveData<StringBuilder> = MutableLiveData(StringBuilder(""))

    fun addText(text: String) {
        textValue.value?.append(text)?.append("\n")
    }

    fun clearText(text: String) {
        textValue.value = StringBuilder("")
    }

}