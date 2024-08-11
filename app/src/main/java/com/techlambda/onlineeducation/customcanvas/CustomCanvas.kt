package com.techlambda.onlineeducation.customcanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

data class Shape(
    val id: Int = 0,
    val type: ShapeType,
    val offset: Coordinates,
    val width: Float,
    val height: Float,
    val rotationDegree: Float,
    val color: Color = Color.Black,
    val isSelected: Boolean,
    val name: String
)

fun Shape.toDpSize(): DpSize {
    return DpSize(width.dp, height.dp)
}

data class Coordinates(
    val x: Float,
    val y: Float
)

fun Coordinates.toComposeOffset(): Offset {
    return Offset(this.x, this.y)
}

fun Coordinates.toIntOffset(): IntOffset {
    return IntOffset(this.x.toInt(), this.y.toInt())
}

fun IntOffset.toComposeOffset(): Offset {
    return Offset(this.x.toFloat(), this.y.toFloat())
}

fun Offset.toCoordinates(): Coordinates {
    return Coordinates(this.x, this.y)
}

enum class ShapeType {
    ROOM,
    DOOR,
    WINDOW,
    STAIR,
}

val TAG = "BlueprintDrawer"

@Composable
fun BlueprintDrawer(modifier: Modifier = Modifier.fillMaxSize()) {
    val shapesList = remember {
        mutableStateListOf<Shape>()
    }

    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    var height by remember {
        mutableStateOf(100f)
    }
    var width by remember {
        mutableStateOf(100f)
    }
    var rotation by remember {
        mutableFloatStateOf(0f)
    }

    val mainBoxSize = remember { mutableStateOf(Size(0f, 0f)) }

    var showAddRoomDialog by remember { mutableStateOf(false) }
    var showEditRoomDialog by remember { mutableStateOf(false) }

    if (showAddRoomDialog) {
        AddItemDialog(
            addButtonLabel = "Add",
            onAddClick = { itemName ->
                if (shapesList.isNotEmpty()) {
                    shapesList.replaceAll {
                        if (it.isSelected) {
                            it.copy(isSelected = false, color = Color.Black)
                        } else it
                    }
                }
                val shape = Shape(
                    id = shapesList.size,
                    type = ShapeType.ROOM,
                    offset = Coordinates(0f, 0f),
                    width = 100f,
                    height = 100f,
                    rotationDegree = 0f,
                    isSelected = true,
                    color = Color.Green,
                    name = itemName
                )
                offset = shape.offset.toComposeOffset()
                height = shape.height
                width = shape.width
                rotation = shape.rotationDegree
                shapesList.add(shape)
                showAddRoomDialog = false
            },
            onCancelClick = {
                showAddRoomDialog = false
            }
        )
    }

    if (showEditRoomDialog) {
        AddItemDialog(
            addButtonLabel = "Edit",
            editTextValue = shapesList.find { it.isSelected }?.name ?: "",
            onAddClick = { itemName ->
                shapesList.replaceAll {
                    if (it.isSelected) {
                        it.copy(name = itemName)
                    } else it
                }
                showAddRoomDialog = false
            },
            onCancelClick = {
                showAddRoomDialog = false
            }
        )
    }


    LaunchedEffect(key1 = offset, key2 = height, key3 = width) {
        shapesList.replaceAll {
            if (it.isSelected) {
                it.copy(offset = offset.toCoordinates(), height = height, width = width)
            } else {
                it
            }
        }
    }
    LaunchedEffect(key1 = rotation) {
        shapesList.replaceAll {
            if (it.isSelected) {
                it.copy(rotationDegree = rotation)
            } else {
                it
            }
        }
    }

    LaunchedEffect(shapesList.toList()) {
        val selectShape = shapesList.find { it.isSelected }
        width = selectShape?.width ?: 100f
        height = selectShape?.height ?: 100f
        rotation = selectShape?.rotationDegree ?: 0f
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                showAddRoomDialog = true
            }) {
                Text(text = "Add Room", color = Color.Black)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Width ", color = Color.Black, modifier = Modifier.padding(0.dp))
            Slider(
                value = width,
                onValueChange = {
                    width = it
                },
                valueRange = 10f..400f, // Adjust the range as needed
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(0.dp)
            )
        }

        // Height Slider
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Height ", color = Color.Black)
            Slider(
                value = height,
                onValueChange = {
                    height = it
                },
                valueRange = 10f..700f, // Adjust the range as needed
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }

        // Rotation Slider
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Deg: ${rotation.toInt()}°", color = Color.Black)
            Slider(
                value = rotation,
                onValueChange = {
                    rotation = it
                },
                valueRange = 0f..360f, // 0 to 360 degrees
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                if (shapesList.isNotEmpty()) {
                    shapesList.removeIf {
                        it.isSelected
                    }
                }
            }) {
                Text(text = "Delete Room", color = Color.Black)
            }

            Button(onClick = {
                showEditRoomDialog = true
            }) {
                Text(text = "Edit Name", color = Color.Black)
            }
        }

        Text(
            text = shapesList.find { it.isSelected }?.name ?: "",
            modifier = Modifier.padding(10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 4.dp, color = Color.Black)
                .onGloballyPositioned { coordinates ->
                    mainBoxSize.value = Size(
                        coordinates.size.width.toFloat(),
                        coordinates.size.height.toFloat()
                    )
                }
        ) {
            shapesList.forEach {
                Canvas(
                    modifier = Modifier
                        .size(size = it.toDpSize())
                        .offset {
                            if (it.isSelected) {
                                IntOffset(offset.x.toInt(), offset.y.toInt())
                            } else {
                                IntOffset(it.offset.x.toInt(), it.offset.y.toInt())
                            }
                        }
                        .pointerInput(Unit) {
                            if (it.isSelected) {
                                detectDragGestures(
                                ) { change, dragAmount ->
                                    change.position
                                    val newOffset = offset + dragAmount


                                    // Boundary Check
                                    val clampedOffset = Offset(
                                        x = newOffset.x.coerceIn(
                                            0f,
                                            mainBoxSize.value.width - width.dp.toPx()
                                        ),
                                        y = newOffset.y.coerceIn(
                                            0f,
                                            mainBoxSize.value.height - height.dp.toPx()
                                        )
                                    )
                                    offset = clampedOffset
                                }
                            }
                        }
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) {
                            if (shapesList.isNotEmpty()) {
                                shapesList.replaceAll { shape ->
                                    shape.copy(isSelected = false, color = Color.Black)
                                }

                                shapesList.replaceAll { shape ->
                                    if (it.id == shape.id) {
                                        shape.copy(isSelected = true, color = Color.Green)
                                    } else {
                                        shape
                                    }
                                }
                                offset = it.offset.toComposeOffset()
                                height = it.height
                                width = it.width
                                rotation = it.rotationDegree
                            }
                        }
                ) {
                    drawShape(it)
                }
            }
        }
    }
}


fun DrawScope.drawShape(shape: Shape) {
    rotate(shape.rotationDegree) {
        when (shape.type) {
            ShapeType.ROOM -> {
                drawRect(
                    color = shape.color,
                    size = Size(size.width, size.height),
                    style = Stroke(
                        width = 4.dp.toPx(),
                    )
                )
            }

            ShapeType.DOOR -> {
                drawRect(
                    color = Color.Red,
                    size = Size(shape.width, shape.height)
                )
            }

            ShapeType.WINDOW -> {
                drawRect(
                    color = Color.Cyan,
                    topLeft = shape.offset.toComposeOffset(),
                    size = Size(shape.width, shape.height)
                )
            }

            ShapeType.STAIR -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(shape.offset.x, shape.offset.y)
                    lineTo(shape.offset.x + shape.width, shape.offset.y)
                    lineTo(shape.offset.x + shape.width, shape.offset.y + shape.height)
                    lineTo(shape.offset.x, shape.offset.y + shape.height)
                    close()
                }
                drawPath(path, color = Color.Gray)
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BlueprintPrev(modifier: Modifier = Modifier) {

    BlueprintDrawer()

}

