package com.kevinschildhorn.fantasypuppybowl


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DogUiState(
    val dogs: List<DogListItem>,
    val filterText: String,
    val hideTakenDogs: Boolean = true,
)

enum class Team {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    NONE;

    fun color() = when (this) {
        RED -> Color.Red
        GREEN -> Color.Green
        BLUE -> Color.Blue
        YELLOW -> Color.Yellow
        NONE -> Color.LightGray
    }
}

data class DogListItem(
    val name: String,
    val description: String,
    val image: String,
    val isTeamFluff: Boolean,
    var team: Team,
) {
    fun setTeam(team: Team) = this.copy(team = team)
    val isTaken = team != Team.NONE
}

class DogViewModel : ViewModel() {

    private var originalDogList: MutableList<DogListItem> = mutableListOf()

    private val _uiState = MutableStateFlow(DogUiState(originalDogList, "", true))
    val uiState: StateFlow<DogUiState> = _uiState.asStateFlow()

    private val parser = HTMLParser()
    private val csvWriter = CSVWriter()


    init {
        viewModelScope.launch(Dispatchers.Default) {
            val dogs = parser.getDogInfo(
                "https://www.discovery.com/shows/puppy-bowl/see/2025/pb-xxi-gallery"
            ).map {
                val isTeamFluffName = it.name.lowercase().contains("team fluff")
                val isTeamFluffDescription = it.description.lowercase().contains("team fluff")
                val isTeamFluff = isTeamFluffName || isTeamFluffDescription
                DogListItem(
                    name = it.name
                        .removePrefix("Team Ruff - ")
                        .removePrefix("Team Fluff - "),
                    description = it.description,
                    image = it.image,
                    isTeamFluff = isTeamFluff,
                    team = Team.NONE,
                )
            }
            originalDogList = dogs.toMutableList()
            _uiState.update { DogUiState(originalDogList, it.filterText, it.hideTakenDogs) }
        }
    }

    fun toggleDogAtIndex(index: Int, team: Team) {
        val selectedDog = _uiState.value.dogs[index]
        val originalIndex = originalDogList.indexOfFirst { it.name == selectedDog.name }
        originalDogList[originalIndex] = originalDogList[originalIndex].setTeam(team)
        updateState()
    }

    fun filter(filterText: String) {
        _uiState.update {
            it.copy(filterText = filterText)
        }
        updateState()
    }

    fun toggleTaken() {
        _uiState.update {
            it.copy(hideTakenDogs = !it.hideTakenDogs)
        }
        updateState()
    }

    fun writeCSV(){
        csvWriter.writeCSV("../../puppyBowl.csv", originalDogList)
    }

    private fun updateState() {
        _uiState.update { state ->
            state.copy(
                dogs = originalDogList.filter {
                    it.name.lowercase().contains(state.filterText.lowercase()) &&
                            (if (state.hideTakenDogs) it.team == Team.NONE else true)
                }
            )
        }
    }
}