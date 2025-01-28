package com.kevinschildhorn.fantasypuppybowl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun DogSummary(listItem: DogListItem, modifier: Modifier = Modifier) {
    val teamBackgroundColor = if (listItem.isTeamFluff) Color.Blue else Color.Red
    Card(
        modifier = modifier.padding(20.dp),
        backgroundColor = teamBackgroundColor,
        contentColor = Color.White,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
            AsyncImage(
                model = listItem.image,
                contentDescription = null,
                alpha = 1.0f,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(vertical = 10.dp)
                    .aspectRatio(1f)
            )
            Text(
                text = listItem.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = listItem.description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}