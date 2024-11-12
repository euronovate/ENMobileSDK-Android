package com.euronovate.mobile.dossierfesotp

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.euronovate.mobile.dossierfesotp.ui.theme.ENSoftGray
import com.euronovate.mobile.dossierfesotp.ui.theme.LocalColors

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val activity = LocalContext.current as Activity

    val isLoading by mainScreenViewModel.isLoading.collectAsStateWithLifecycle()

    val initializationState by mainScreenViewModel.initializationState.collectAsStateWithLifecycle()
    val dossierName by mainScreenViewModel.dossierName.collectAsStateWithLifecycle()
    val dossierDescription by mainScreenViewModel.dossierDescription.collectAsStateWithLifecycle()
    val documentsGuidAndSignatureComplete by mainScreenViewModel.documentsGuidAndSignatureComplete.collectAsStateWithLifecycle()

    val keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)

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

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(columnMaxWidth)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {

                // init
                Button(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        mainScreenViewModel.initLibrary(context = context)
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

                Spacer(modifier = Modifier.height(16.dp))

                // input dossier guid / dossier find
                var dossierGuid by remember { mutableStateOf("") }
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
                            value = dossierGuid,
                            onValueChange = { dossierGuid = it },
                            placeholder = {
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Dossier GUID"
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
                            mainScreenViewModel.dossierFind(activity = activity, dossierGuid = dossierGuid)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LocalColors.current.secondaryContainer,
                            disabledContainerColor = White.copy(0.1f)
                        ),
                        contentPadding = PaddingValues(horizontal = 3.dp),
                        enabled = (initializationState == InitializationState.Initialized && dossierGuid.isNotBlank()),
                    ) {
                        Text(
                            text = stringResource(id = R.string.dossier_find),
                            color = LocalColors.current.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // dossier data and find documents button
                if (dossierName != null) {
                    Column(
                        modifier = Modifier
                            .border(
                                BorderStroke(2.dp, LocalColors.current.background),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Row {
                            Text(
                                modifier = Modifier,
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.dossier_name))
                                    }
                                    append(" $dossierName\n\n")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(stringResource(R.string.dossier_description))
                                    }
                                    append(" $dossierDescription")
                                },
                                color = LocalColors.current.onSecondaryContainer,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(250.dp),
                                shape = RoundedCornerShape(16.dp),
                                onClick = {
                                    mainScreenViewModel.dossierFindDocuments(activity = activity, dossierGuid = dossierGuid)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LocalColors.current.secondaryContainer,
                                    disabledContainerColor = White.copy(0.1f)
                                ),
                                contentPadding = PaddingValues(horizontal = 3.dp),
                                enabled = (initializationState == InitializationState.Initialized && dossierGuid.isNotBlank()),
                            ) {
                                Text(
                                    text = stringResource(id = R.string.dossier_find_documents),
                                    color = LocalColors.current.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // dossier document list
                Column(
                    modifier = Modifier
                        .border(
                            BorderStroke(2.dp, if (documentsGuidAndSignatureComplete.isEmpty()) Color.Transparent else LocalColors.current.background),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    documentsGuidAndSignatureComplete.keys.forEachIndexed { _, item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            Text(
                                text = item,
                                color = LocalColors.current.onSecondaryContainer,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(250.dp),
                                shape = RoundedCornerShape(16.dp),
                                onClick = {
                                    mainScreenViewModel.openDocumentForSignature(activity = activity, documentGuid = item)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LocalColors.current.secondaryContainer,
                                    disabledContainerColor = White.copy(0.1f)
                                ),
                                contentPadding = PaddingValues(horizontal = 3.dp),
                                enabled = !(documentsGuidAndSignatureComplete[item] ?: true)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.sign),
                                    color = LocalColors.current.onSecondaryContainer
                                )
                            }

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