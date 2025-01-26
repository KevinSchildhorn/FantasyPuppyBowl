package com.kevinschildhorn.fantasypuppybowl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImage


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DogItem(listItem: DogListItem, modifier: Modifier = Modifier, onClick: () -> Unit) {


    val teamBackgroundColor = if (listItem.isTeamFluff) Color.Blue else Color.Red
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        backgroundColor = if(listItem.isTaken) listItem.team.color() else teamBackgroundColor,
        contentColor = Color.White,
    ) {
        Column {
            AsyncImage(
                model = listItem.image,
                contentDescription = null,
                alpha = if(listItem.isTaken) 0.25f else 1.0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = listItem.name,
                style = MaterialTheme.typography.caption,
                softWrap = false,
            )
        }
    }
}