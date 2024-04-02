package org.psykin.aethervoice.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp

@Composable
fun Action(actionName: String, imageVector: ImageVector, modifier: Modifier, onItemClick: () -> Unit) {
    Box(
        modifier = modifier.clickable(onClick = onItemClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = actionName,
                tint = Color.White
            )

            Text(
                text = actionName,
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }

}