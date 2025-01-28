package com.kevinschildhorn.fantasypuppybowl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fantasypuppybowl.composeapp.generated.resources.Res
import fantasypuppybowl.composeapp.generated.resources.save_24dp
import fantasypuppybowl.composeapp.generated.resources.visibility_24dp
import fantasypuppybowl.composeapp.generated.resources.visibility_off_24dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun DogScreen(dogViewModel: DogViewModel) {
    val dogState = dogViewModel.uiState.collectAsStateWithLifecycle()
    var indexForBottomSheet by remember { mutableStateOf(-1) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        topBar = {
            Row {
                TextField(
                    value = dogState.value.filterText,
                    onValueChange = {
                        dogViewModel.filter(it)
                    },
                    label = { Text("Search") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    dogViewModel.toggleTaken()
                }) {
                    Icon(
                        painterResource(
                            if (dogState.value.hideTakenDogs) Res.drawable.visibility_off_24dp
                            else Res.drawable.visibility_24dp
                        ), null
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton({
                dogViewModel.writeCSV()
            }) {
                Icon(painterResource(Res.drawable.save_24dp), null)
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(getPlatform().rowSize)
        ) {
            itemsIndexed(dogState.value.dogs) { index, photo ->
                DogItem(
                    photo, modifier = Modifier
                        .padding(4.dp),
                    onClick = {
                        indexForBottomSheet = index
                    }
                )
            }
        }

        if (indexForBottomSheet != -1) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(red = 0, green = 0, blue = 0, alpha = 125))
                    .zIndex(2f)
                    .fillMaxSize()
            ) {
                Row {
                    DogSummary(
                        dogState.value.dogs[indexForBottomSheet],
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                    Card(modifier = Modifier.zIndex(3f).fillMaxWidth().padding(20.dp)) {
                        Column {
                            IconButton({
                                dogViewModel.toggleDogAtIndex(indexForBottomSheet, Team.RED)
                                indexForBottomSheet = -1
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Red Team")
                            }
                            IconButton({
                                dogViewModel.toggleDogAtIndex(indexForBottomSheet, Team.GREEN)
                                indexForBottomSheet = -1
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Green Team")
                            }
                            IconButton({
                                dogViewModel.toggleDogAtIndex(indexForBottomSheet, Team.BLUE)
                                indexForBottomSheet = -1
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Blue Team")
                            }
                            IconButton({
                                dogViewModel.toggleDogAtIndex(indexForBottomSheet, Team.YELLOW)
                                indexForBottomSheet = -1
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Yellow Team")
                            }
                            Button({
                                dogViewModel.toggleDogAtIndex(indexForBottomSheet, Team.NONE)
                                indexForBottomSheet = -1
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("None")
                            }
                        }
                    }
                }
            }
        }
    }
}