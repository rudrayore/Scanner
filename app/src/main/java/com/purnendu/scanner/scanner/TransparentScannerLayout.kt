package com.purnendu.scanner.scanner

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
fun TransparentScannerLayout(
    modifier: Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp
) {
    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }

    Canvas(modifier = modifier) {

        val canvasWidth = size.width

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // For blur area
            drawRect(color = Color(0xFF545d7c), alpha = 0.7f)

            // For transparent area
            drawRoundRect(
                topLeft = Offset(
                    x = (canvasWidth - widthInPx) / 2,
                    y = offsetInPx
                ),
                size = Size(widthInPx, heightInPx),
                cornerRadius = CornerRadius(50f, 50f),
                color = Color.Transparent,
                blendMode = BlendMode.Clear
            )

            //4 dots at corner
            drawCircle(
                color = Color(0xFF1b79e6), radius = 15f, center = Offset(
                    x = ((canvasWidth - widthInPx) / 2) + 50f,
                    y = offsetInPx + 50f
                )
            )

            drawCircle(
                color = Color(0xFFFF4077), radius = 15f, center = Offset(
                    x = ((canvasWidth - widthInPx) / 2) + widthInPx - 50f,
                    y = offsetInPx + 50f
                )
            )

            drawCircle(
                color = Color(0xFF37d8cf), radius = 15f, center = Offset(
                    x = ((canvasWidth - widthInPx) / 2) + widthInPx - 50f,
                    y = offsetInPx + heightInPx - 50f
                )
            )

            drawCircle(
                color = Color(0xFFf6cc00), radius = 15f, center = Offset(
                    x = ((canvasWidth - widthInPx) / 2) + 50f,
                    y = offsetInPx + heightInPx - 50f
                )
            )
            restoreToCount(checkPoint)
        }
    }
}