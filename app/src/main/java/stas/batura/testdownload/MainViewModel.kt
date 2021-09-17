package stas.batura.testdownload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.StringBuilder

class MainViewModel: ViewModel() {

    val textValue: MutableLiveData<String> = MutableLiveData((""))

    fun addText(text: String) {
        val lastv = textValue.value
        textValue.value = lastv.plus("\n").plus(text).plus("\n")
    }

    fun clearText(text: String) {
        textValue.value = ("")
    }

}