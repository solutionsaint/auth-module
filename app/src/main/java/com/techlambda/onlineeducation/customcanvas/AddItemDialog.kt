package com.techlambda.onlineeducation.customcanvas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddItemDialog(
    addButtonLabel: String = "Add",
    editTextValue: String = "",
    onAddClick: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    var text by remember { mutableStateOf(editTextValue) }

    AlertDialog(
        onDismissRequest = { onCancelClick() },
        confirmButton = {
            TextButton(onClick = {
                onAddClick(text)
            }) {
                Text(text = addButtonLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancelClick() }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "$addButtonLabel Room")
        },
        text = {
            Column {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    )
}

