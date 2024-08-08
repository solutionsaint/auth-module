package com.techlambda.onlineeducation.customcanvas

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Rectangle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.io.path.Path
import kotlin.io.path.moveTo

data class Shape(
    val id: Int = 0,
    val type: ShapeType,
    val offset: Coordinates,
    val width: Float,
    val height: Float,
    val rotationDegree: Float,
    val color: Color = Color.Black
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
    var selectedShape by remember {
        mutableStateOf<Shape?>(null)
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

    LaunchedEffect(shapesList.size) {
        selectedShape = shapesList.lastOrNull()
        Log.d(TAG, "offset: size $selectedShape")
    }

    LaunchedEffect(key1 = offset, key2 = height, key3 = width) {
        selectedShape =
            selectedShape?.copy(offset = offset.toCoordinates(), height = height, width = width)
        Log.d(TAG, "offset: " + selectedShape?.offset)
        Log.d(TAG, "offset: " + offset)
        shapesList.replaceAll {
            if (it.id == selectedShape?.id) {
                selectedShape!!
            } else {
                it
            }
        }
    }
    LaunchedEffect(key1 = rotation) {
        selectedShape = selectedShape?.copy(rotationDegree = rotation)
        Log.d(TAG, "offset: " + selectedShape?.offset)
        Log.d(TAG, "offset: " + offset)
        shapesList.replaceAll {
            if (it.id == selectedShape?.id) {
                selectedShape!!
            } else {
                it
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                val shape = Shape(
                    id = shapesList.size,
                    type = ShapeType.ROOM,
                    offset = Coordinates(0f, 0f),
                    width = 100f,
                    height = 100f,
                    rotationDegree = 0f,
                )
                offset = shape.offset.toComposeOffset()
                height = shape.height
                width = shape.width
                rotation = shape.rotationDegree
                shapesList.add(shape)
            }) {
                Icon(
                    imageVector = Icons.Default.Rectangle,
                    contentDescription = "room",
                    tint = Color.Black
                )
            }
            IconButton(onClick = {
                val shape = Shape(
                    type = ShapeType.DOOR,
                    offset = Coordinates(0f, 0f),
                    width = 100f,
                    height = 100f,
                    rotationDegree = 0f,
                )
                shapesList.add(shape)
            }) {
                Icon(imageVector = Icons.Default.LineWeight, contentDescription = "line")
            }
            IconButton(onClick = {
            }) {
                Icon(imageVector = Icons.Default.Rectangle, contentDescription = "room")
            }


        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Width: ${width.toInt()}")
            Slider(
                value = width,
                onValueChange = {
                    width = it
                },
                valueRange = 50f..300f, // Adjust the range as needed
                modifier = Modifier.weight(1f)
            )
        }

        // Height Slider
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Height: ${height.toInt()}")
            Slider(
                value = height,
                onValueChange = {
                    height = it
                },
                valueRange = 50f..300f, // Adjust the range as needed
                modifier = Modifier.weight(1f)
            )
        }

        // Rotation Slider
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Rotation: ${rotation.toInt()}°")
            Slider(
                value = rotation,
                onValueChange = {
                    rotation = it
                },
                valueRange = 0f..360f, // 0 to 360 degrees
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 4.dp, color = Color.Black)
        ) {
            shapesList.forEach {
                if (it.id != selectedShape?.id) {
                    Canvas(
                        modifier = Modifier
                            .size(size = it.toDpSize())
                            .offset {
                                IntOffset(it.offset.x.toInt(), it.offset.y.toInt())
                            }

                    ) {
                        drawShape(it)
                    }
                }

            }
            selectedShape?.let {
                Canvas(
                    modifier = Modifier
                        .size(size = it.toDpSize())
                        .offset {
                            IntOffset(offset.x.toInt(), offset.y.toInt())
                        }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                offset = offset.plus(dragAmount)
                            }
                            detectTransformGestures { _, pan, _, _ ->
                                //   offset = offset.plus(pan)
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

