package com.euronovate.examples.localandremotesignature

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

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
    val activity = LocalContext.current as Activity

    val isLoading by mainScreenViewModel.isLoading.collectAsStateWithLifecycle()

    val initializationState by mainScreenViewModel.initializationState.collectAsStateWithLifecycle()

    var documentGuid by remember { mutableStateOf("") }

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

                    // open local document
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            mainScreenViewModel.openDialogForLocalDocument(activity = activity)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalColors.current.secondaryContainer,
                            disabledContainerColor = White.copy(0.1f)
                        ),
                        contentPadding = PaddingValues(horizontal = 3.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.open_local),
                            color = LocalColors.current.onSecondaryContainer
                        )
                    }

                    // open remote document with SDK styled popup
                    Button(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            mainScreenViewModel.openDialogForRemoteDocument(activity = activity)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalColors.current.secondaryContainer,
                            disabledContainerColor = White.copy(0.1f)
                        ),
                        contentPadding = PaddingValues(horizontal = 3.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.open_remote_with_dialog),
                            color = LocalColors.current.onSecondaryContainer
                        )
                    }

                    // open remote document with GUID only
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(60.dp)
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(ENSoftGray.copy(alpha = 0.5f)),
                                value = documentGuid,
                                onValueChange = { documentGuid = it },
                                placeholder = {
                                    Text(
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Document GUID"
                                    )
                                },
                                keyboardOptions = keyboardOptions,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        keyboardController?.hide()
                                    }
                                ),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                            )
                        }

                        Button(
                            modifier = Modifier
                                .height(60.dp)
                                .width(250.dp),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                mainScreenViewModel.openRemoteDocumentForSignature(activity = activity, documentGuid = documentGuid)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LocalColors.current.secondaryContainer,
                                disabledContainerColor = White.copy(0.1f)
                            ),
                            contentPadding = PaddingValues(horizontal = 3.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.open_remote),
                                color = LocalColors.current.onSecondaryContainer
                            )
                        }
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