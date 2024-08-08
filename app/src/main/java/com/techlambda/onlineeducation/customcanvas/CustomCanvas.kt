package com.techlambda.onlineeducation.customcanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Rectangle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.io.path.Path
import kotlin.io.path.moveTo

data class Shape(
    val type: ShapeType,
    val offset: com.techlambda.onlineeducation.customcanvas.Offset,
    val width: Float,
    val height: Float,
    val rotationDegree: Float,
)

data class Offset(
    val x: Float,
    val y: Float
)

fun com.techlambda.onlineeducation.customcanvas.Offset.toComposeOffset(): Offset {
    return Offset(this.x, this.y)
}

fun com.techlambda.onlineeducation.customcanvas.Offset.toIntOffset(): IntOffset {
    return IntOffset(this.x.toInt(), this.y.toInt())
}

enum class ShapeType {
    ROOM,
    DOOR,
    WINDOW,
    STAIR,
}


@Composable
fun BlueprintDrawer(modifier: Modifier = Modifier.fillMaxSize()) {
    val shapesList = remember {
        mutableListOf<Shape>()
    }
    var selectedShape by remember(key1 = shapesList) {
        mutableStateOf(shapesList.lastOrNull())
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                val shape = Shape(
                    type = ShapeType.ROOM,
                    offset = com.techlambda.onlineeducation.customcanvas.Offset(100f, 100f),
                    width = 100f,
                    height = 100f,
                    rotationDegree = 0f,
                )
                shapesList.add(shape)
            }) {
                Icon(imageVector = Icons.Default.Rectangle, contentDescription = "room")
            }
            IconButton(onClick = {
            }) {
                Icon(imageVector = Icons.Default.LineWeight, contentDescription = "line")
            }
            IconButton(onClick = {
            }) {
                Icon(imageVector = Icons.Default.Rectangle, contentDescription = "room")
            }


        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 4.dp, color = Color.Black)
        ) {
            val tempShapeList = shapesList.toMutableList()
            tempShapeList.removeIf { it == selectedShape }
            tempShapeList.forEach { shape ->
                Canvas(
                    modifier = Modifier
                        .offset {
                            shape.offset.toIntOffset()
                        }
                ) {
                    drawShape(shape)
                }
            }

            selectedShape?.let {
                Canvas(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                },
                                onDrag = { _, dragAmount ->
                                    selectedShape = selectedShape?.copy(
                                        offset = com.techlambda.onlineeducation.customcanvas.Offset(
                                            it.offset.x + dragAmount.x,
                                            it.offset.y + dragAmount.y
                                        )
                                    )
                                },
                                onDragCancel = {
                                },
                                onDragEnd = {
                                }
                            )
                        }
                ) {
                    drawShape(it)
                }
            }
        }
    }
}

fun DrawScope.drawShape(shape: Shape) {
    rotate(shape.rotationDegree, pivot = shape.offset.toComposeOffset()) {
        when (shape.type) {
            ShapeType.ROOM -> {
                drawRect(
                    color = Color.Blue,
                    size = Size(shape.width, shape.height),
                    style = Stroke(
                        width = 4.dp.toPx(),
                    )
                )
            }

            ShapeType.DOOR -> {
                drawRect(
                    color = Color.Red,
                    topLeft = shape.offset.toComposeOffset(),
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

