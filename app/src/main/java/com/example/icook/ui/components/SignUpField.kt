package com.example.icook.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SignUpField(
    imageVector: ImageVector = Icons.Default.Warning,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    isError: Boolean = false,
    labelText: String = "",
    placeholderText: String = "",
    showHideValue: Boolean = false,
    onShowHideValueChange: (String, String) -> Unit = {value: String, newValue: String -> },
    isTextVisible: Boolean = true,
    onShowHideButtonClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier.padding(15.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            TextField(
                value = if (isTextVisible) value else "*".repeat(value.length),
                label = { Text(text = labelText)},
                placeholder = { Text(text = placeholderText)},
                onValueChange = if (showHideValue) {newValue -> onShowHideValueChange(value, newValue)} else onValueChange ,
                singleLine = true,
                isError = isError,
                leadingIcon = { Icon(imageVector, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(if (showHideValue) .7f else 0.9f)
            )
            if (showHideValue) {
               IconButton(onClick = onShowHideButtonClicked) {
                   Icon(
                       if (isTextVisible) Icons.Sharp.Check  else Icons.Sharp.CheckCircle,
                       contentDescription = null)
               }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpFieldPreview(){
    SignUpField(showHideValue = true, isTextVisible = false)
}