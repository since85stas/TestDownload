package stas.batura.testdownload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val textValue: MutableLiveData<String> = MutableLiveData("")

}