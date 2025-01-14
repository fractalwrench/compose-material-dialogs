package com.vanpra.composematerialdialogs.color

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog

val itemSizeDp = 55.dp
val tickSize = 35.dp

/**
 * @brief Adds a color chooser to the dialog
 *
 * @param colors a list of colors for the user to choose. See [ColorPalette] for predefined colors
 * @param subColors a list of subsets of [colors] for the user to choose from once a main color from
 * colors has been chosen. See [ColorPalette] for predefined sub-colors colors
 * @param initialSelection the index of the color which is selected initially
 * @param allowCustomArgb if true this will allow the user to choose a custom color using
 * ARGB sliders
 * @param waitForPositiveButton if true the [onColorSelected] callback will only be called when the
 * positive button is pressed, otherwise it will be called when the a new color is selected
 * @param onColorSelected a function which is called with a [Color]. The timing of this call is
 * dictated by [waitForPositiveButton]
 */
@Composable
fun MaterialDialog.colorChooser(
    colors: List<Color>,
    subColors: List<List<Color>> = listOf(),
    initialSelection: Int = 0,
    allowCustomArgb: Boolean = false,
    waitForPositiveButton: Boolean = false,
    onColorSelected: (Color) -> Unit = {}
) {
    BoxWithConstraints {
        val selectedColor = remember { mutableStateOf(colors[initialSelection]) }

        val anchors = mapOf(0f to "ColorPicker", constraints.maxWidth.toFloat() to "ARGBPicker")
        val swipeState = rememberSwipeableState("ColorPicker")

        SideEffect {
            if (waitForPositiveButton) {
                callbacks.add {
                    onColorSelected(selectedColor.value)
                }
            }
        }

        Column(
            Modifier.padding(bottom = 8.dp).swipeable(
                swipeState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                reverseDirection = true,
                resistance = null,
                enabled = allowCustomArgb
            )
        ) {

            if (allowCustomArgb) {
                PageIndicator(swipeState, this@BoxWithConstraints.constraints)
            }

            Layout(
                content = {
                    ColorGridLayout(
                        Modifier.width(this@BoxWithConstraints.maxWidth),
                        colors = colors,
                        selectedColor = selectedColor,
                        subColors = subColors,
                        waitForPositiveButton = waitForPositiveButton,
                        onColorSelected = onColorSelected
                    )

                    Box(Modifier.width(this@BoxWithConstraints.maxWidth)) {
                        CustomARGB(selectedColor)
                    }
                }
            ) { measurables, constraints ->
                val placeables = measurables.map { it.measure(constraints) }
                val height = placeables.maxByOrNull { it.height }?.height ?: 0

                layout(constraints.maxWidth, height) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.place(
                            x = -swipeState.offset.value.toInt() + index * constraints.maxWidth,
                            y = 0
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PageIndicator(swipeState: SwipeableState<String>, constraints: Constraints) {
    Row(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        val ratio = remember(constraints.maxWidth, swipeState.offset.value) {
            swipeState.offset.value / constraints.maxWidth.toFloat()
        }
        val color = MaterialTheme.colors.onBackground
        Canvas(modifier = Modifier) {
            val offset = Offset(30f, 0f)
            drawCircle(
                color.copy(0.7f + 0.3f * (1 - ratio)),
                radius = 8f + 7f * (1 - ratio),
                center = center - offset
            )
            drawCircle(
                color.copy(0.7f + 0.3f * ratio),
                radius = 8f + 7f * ratio,
                center = center + offset
            )
        }
    }
}

@Composable
private fun CustomARGB(selectedColor: MutableState<Color>) {
    Column(Modifier.padding(start = 24.dp, end = 24.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(selectedColor.value),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "#${Integer.toHexString(selectedColor.value.toArgb())}",
                color = selectedColor.value.foreground(),
                style = TextStyle(fontWeight = FontWeight.Bold),
                textDecoration = TextDecoration.Underline,
                fontSize = 18.sp
            )
        }
        SliderLayout(selectedColor)
    }
}

@Composable
private fun SliderLayout(selectedColor: MutableState<Color>) {
    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        label = "A",
        value = selectedColor.value.alpha * 255,
        sliderColor = Color.DarkGray
    ) {
        selectedColor.value = selectedColor.value.copy(alpha = it / 255f)
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        label = "R",
        value = selectedColor.value.red * 255,
        sliderColor = Color.Red
    ) {
        selectedColor.value = selectedColor.value.copy(red = it / 255f)
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        label = "G",
        value = selectedColor.value.green * 255,
        sliderColor = Color.Green
    ) {
        selectedColor.value = selectedColor.value.copy(green = it / 255f)
    }

    LabelSlider(
        modifier = Modifier.padding(top = 16.dp),
        label = "B",
        value = selectedColor.value.blue * 255,
        sliderColor = Color.Blue
    ) {
        selectedColor.value = selectedColor.value.copy(blue = it / 255f)
    }
}

@Composable
private fun LabelSlider(
    modifier: Modifier = Modifier,
    label: String,
    value: Float,
    sliderColor: Color,
    onSliderChange: (Float) -> Unit
) {
    BoxWithConstraints {
        Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                style = MaterialTheme.typography.h6,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.width(10.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Slider(
                value = value,
                onValueChange = onSliderChange,
                valueRange = 0f..255f,
                steps = 255,
                modifier = Modifier.width(this@BoxWithConstraints.maxWidth - 56.dp),
                colors = SliderDefaults.colors(
                    activeTickColor = Color.Unspecified,
                    activeTrackColor = sliderColor,
                    thumbColor = sliderColor,
                    inactiveTickColor = Color.Unspecified
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                Modifier
                    .width(30.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    value.toInt().toString(),
                    color = Color.White,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Composable
private fun ColorGridLayout(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: MutableState<Color>,
    subColors: List<List<Color>> = listOf(),
    waitForPositiveButton: Boolean = false,
    onColorSelected: (Color) -> Unit = {}
) {
    var mainSelectedIndex by remember { mutableStateOf(0) }
    var showSubColors by remember { mutableStateOf(false) }

    val itemSize = with(LocalDensity.current) { itemSizeDp.toPx().toInt() }

    GridView(modifier, itemSize = itemSize) {
        if (!showSubColors) {
            colors.forEachIndexed { index, item ->
                ColorView(color = item, selected = index == mainSelectedIndex) {
                    if (mainSelectedIndex != index) {
                        mainSelectedIndex = index
                        if (!waitForPositiveButton && subColors.isNotEmpty()) {
                            selectedColor.value = item
                            onColorSelected(item)
                        }
                    }
                    if (subColors.isNotEmpty()) {
                        showSubColors = true
                    }
                }
            }
        } else {
            // TODO: Remove indication
            Box(
                Modifier
                    .size(itemSizeDp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            showSubColors = false
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    Icons.Default.ArrowBack,
                    contentDescription = "Go back to main color page",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(tickSize)
                )
            }

            subColors[mainSelectedIndex].forEachIndexed { _, item ->
                ColorView(color = item, selected = selectedColor.value == item) {
                    selectedColor.value = item
                    if (!waitForPositiveButton) {
                        onColorSelected(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorView(color: Color, selected: Boolean, onClick: () -> Unit) {
    Box(
        // TODO: Remove indication
        Modifier
            .size(itemSizeDp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colors.onBackground, CircleShape)
            .clickable(
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Image(
                Icons.Default.Done,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color.foreground()),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(tickSize)
            )
        }
    }
}

@Composable
private fun GridView(
    modifier: Modifier = Modifier,
    itemsInRow: Int = 4,
    itemSize: Int,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier) {
        LazyColumn(modifier = Modifier.heightIn(max = (maxHeight * 0.7f))) {
            item {
                Layout(
                    { content() },
                    Modifier
                        .padding(top = 8.dp, start = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) { measurables, constraints ->
                    val spacing =
                        (constraints.maxWidth - (itemSize * itemsInRow)) / (itemsInRow - 1)
                    val rows = (measurables.size / itemsInRow) + 1

                    val layoutHeight = (rows * itemSize) + ((rows - 1) * spacing)

                    layout(constraints.maxWidth, layoutHeight) {
                        measurables
                            .map {
                                it.measure(
                                    Constraints(
                                        maxHeight = itemSize,
                                        maxWidth = itemSize
                                    )
                                )
                            }
                            .forEachIndexed { index, it ->
                                it.place(
                                    x = (index % itemsInRow) * (itemSize + spacing),
                                    y = (index / itemsInRow) * (itemSize + spacing)
                                )
                            }
                    }
                }
            }
        }
    }
}

private fun Color.foreground(): Color =
    if (this.luminance() > 0.5f) Color.Black else Color.White
