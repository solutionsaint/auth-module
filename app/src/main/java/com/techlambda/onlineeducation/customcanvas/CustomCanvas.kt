package com.techlambda.onlineeducation.customcanvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

enum class ShapeType {
    ROOM,
    DOOR,
    WINDOW,
    STAIR,
}


@Composable
fun BlueprintDrawer(modifier: Modifier = Modifier) {
    var shapesList = remember {
        mutableListOf<Shape>()
    }
    var selectedShape by remember {
        mutableStateOf<Any?>(null)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                val shape = Shape(
                    type = ShapeType.ROOM,
                    offset = com.techlambda.onlineeducation.customcanvas.Offset(0f, 0f),
                    width = 100f,
                    height = 100f,
                    rotationDegree = 0f,
                )
                selectedShape = shape
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
        Column(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .border(width = 4.dp, color = Color.Black)
                    .padding(4.dp)
            ) {
                shapesList.forEach {
                  //  drawShape(it)
                }
                if (selectedShape!= null) {
                    drawShape(selectedShape as Shape)
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
                    topLeft = shape.offset.toComposeOffset(),
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

