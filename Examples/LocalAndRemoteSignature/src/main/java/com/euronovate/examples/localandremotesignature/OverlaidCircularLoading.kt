package com.euronovate.examples.localandremotesignature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * OverlaidCircularLoading a [Box] with a [CircularProgressIndicator]
 *
 * @param dimen: Width and Height of the indicator
 * @param strokeWidth: Thickness of the indicator
 * @param centerMode: Centering mode of the indicator in the parent box. Needed only if parent
 *                    composable not handle the internal box dimen
 *
 */
@Composable
public fun OverlaidCircularLoading(
    isVisible: Boolean,
    dimen: Dp,
    strokeWidth: Dp = 2.dp,
    centerMode: OverlaidCircularLoadingCenterMode = OverlaidCircularLoadingCenterMode.CENTER,
    color: Color,
    backgroundColor: Color = Color(0x77000000),
) {

    val interactionSource = remember { MutableInteractionSource() }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .wrapContentHeight(Alignment.Bottom)
    ) {
        val boxModifier = when (centerMode) {
            OverlaidCircularLoadingCenterMode.VERTICAL_CENTER -> Modifier.fillMaxHeight()
            OverlaidCircularLoadingCenterMode.HORIZONTAL_CENTER -> Modifier.fillMaxWidth()
            OverlaidCircularLoadingCenterMode.CENTER -> Modifier.fillMaxSize()
        }

        Box(
            modifier = boxModifier
                .background(backgroundColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {}
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(dimen)
                    .height(dimen)
                    .align(Alignment.Center),
                strokeWidth = strokeWidth,
                color = color,
            )
        }
    }
}

public enum class OverlaidCircularLoadingCenterMode {
    VERTICAL_CENTER, HORIZONTAL_CENTER, CENTER
}