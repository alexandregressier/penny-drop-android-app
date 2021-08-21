package dev.gressier.pennydrop.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.gressier.pennydrop.types.NewPlayer

class PickPlayersViewModel : ViewModel() {

    val players = MutableLiveData<List<NewPlayer>>().apply {
        value = (1..6).map {
            NewPlayer(
                canBeRemoved = it > 2,
                canBeToggled = it > 1,
            )
        }
    }
}