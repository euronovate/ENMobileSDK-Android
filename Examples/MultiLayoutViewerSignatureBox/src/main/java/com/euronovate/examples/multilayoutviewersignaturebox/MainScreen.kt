package com.euronovate.examples.multilayoutviewersignaturebox

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.euronovate.examples.multilayoutviewersignaturebox.theme.LocalColors
import com.euronovate.signaturebox.model.enums.ENSignatureBoxType
import com.euronovate.viewer.model.enums.ENViewerBarType
import com.euronovate.viewer.model.enums.ENViewerType

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
) {

    val activity = LocalContext.current as Activity

    val isLoading by mainScreenViewModel.isLoading.collectAsStateWithLifecycle()
    val initializationState by mainScreenViewModel.initializationState.collectAsStateWithLifecycle()
    val selectedViewerType by mainScreenViewModel.selectedViewerType.collectAsStateWithLifecycle()
    val selectedSignatureBoxType by mainScreenViewModel.selectedSignatureBoxType.collectAsStateWithLifecycle()

    val viewerThemes = listOf(
        Triple(stringResource(id = R.string.viewer_simple), ENViewerType.simple, ENViewerBarType.simple),
        Triple(stringResource(id = R.string.viewer_theme1), ENViewerType.theme1, ENViewerBarType.theme1),
    )

    val signatureBoxThemes = listOf(
        Pair(stringResource(id = R.string.signaturebox_simple), ENSignatureBoxType.simple),
        Pair(stringResource(id = R.string.signaturebox_simplerightconfirm), ENSignatureBoxType.simpleRightConfirm),
        Pair(stringResource(id = R.string.signaturebox_theme1), ENSignatureBoxType.theme1),
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val isPortrait = maxWidth < maxHeight
        val columnMaxWidth = if (isPortrait) {
            maxWidth * 0.9f
        } else {
            maxWidth * 0.7f
        }

        // Background Image
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            alpha = 0.5F,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                color = LocalColors.current.secondary,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(columnMaxWidth)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
            ) {

                // init
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        mainScreenViewModel.initLibrary(context = activity)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalColors.current.secondaryContainer,
                        disabledContainerColor = White.copy(0.1f)
                    ),
                    contentPadding = PaddingValues(horizontal = 3.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.init),
                        color = LocalColors.current.onSecondaryContainer
                    )
                }

                if (initializationState == InitializationState.Initialized) {

                    // viewer theme
                    Text(
                        text = stringResource(id = R.string.viewer_theme),
                        color = LocalColors.current.secondary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        viewerThemes.forEach { (label, type, barType) ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                            ) {

                                Text(
                                    text = label,
                                    color = LocalColors.current.onPrimary,
                                )

                                RadioButton(
                                    modifier = Modifier.scale(scale = 1.2f),
                                    selected = type == selectedViewerType,
                                    onClick = {
                                        mainScreenViewModel.onViewerTypeSelected(type = Triple(label, type, barType))
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = LocalColors.current.secondary
                                    )
                                )
                            }
                        }
                    }

                    // SignatureBox theme
                    Text(
                        text = stringResource(id = R.string.signaturebox_theme),
                        color = LocalColors.current.secondary,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        signatureBoxThemes.forEach { (label, type) ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                            ) {

                                Text(
                                    text = label,
                                    color = LocalColors.current.onPrimary,
                                )

                                RadioButton(
                                    modifier = Modifier.scale(scale = 1.2f),
                                    selected = type == selectedSignatureBoxType,
                                    onClick = {
                                        mainScreenViewModel.onSignatureBoxTypeSelected(type = type)
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = LocalColors.current.secondary
                                    )
                                )
                            }
                        }
                    }

                    // open document
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            mainScreenViewModel.openDocument(activity = activity)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalColors.current.secondaryContainer,
                            disabledContainerColor = White.copy(0.1f)
                        ),
                        contentPadding = PaddingValues(horizontal = 3.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.open_document),
                            color = LocalColors.current.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }

    OverlaidCircularLoading(
        isVisible = isLoading,
        dimen = 100.dp,
        strokeWidth = 5.dp,
        color = LocalColors.current.secondary,
    )
}