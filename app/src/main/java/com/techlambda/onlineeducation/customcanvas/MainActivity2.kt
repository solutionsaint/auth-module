package com.techlambda.onlineeducation.customcanvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techlambda.onlineeducation.ui.theme.VMTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VMTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PhysicsDemo()
                }
            }
        }
    }
}

@Composable
fun PhysicsDemo() {
    var position by remember { mutableStateOf(Offset(100f, 100f)) }
    var velocity by remember { mutableStateOf(Offset(0f, 0f)) }
    val size = 400f
    val boundaryPadding = 10f // Padding for the boundaries

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                position = Offset(
                    position.x + pan.x,
                    position.y + pan.y
                )
                // Apply boundary constraints
                position = Offset(
                    position.x.coerceIn(boundaryPadding, size - boundaryPadding),
                    position.y.coerceIn(boundaryPadding, size - boundaryPadding)
                )
            }
        }
    ) {

        Canvas(
            modifier = Modifier
                .size(size.dp)
                .align(Alignment.Center)
                .border(2.dp, Color.Black)
        ) {
            drawBall(position, size)
        }
    }
}

fun DrawScope.drawBall(position: Offset, size: Float) {
    drawCircle(
        color = Color.Red,
        radius = size / 4,
        center = position
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VMTheme {
        PhysicsDemo()
    }
}