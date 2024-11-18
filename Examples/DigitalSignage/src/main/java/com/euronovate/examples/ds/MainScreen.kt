package com.euronovate.examples.ds

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.euronovate.examples.digitalSignage.R
import com.euronovate.examples.ds.theme.LocalColors

@Composable
fun MainScreen(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
) {

    val context = LocalContext.current
    val initializationState by mainScreenViewModel.initializationState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

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

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                // init local DS
                Button(
                    modifier = Modifier
                        .height(80.dp)
                        .width(350.dp),
                    onClick = {
                        mainScreenViewModel.initLibraryForLocalDigitalSignage(context = context)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalColors.current.secondaryContainer,
                        disabledContainerColor = White.copy(0.1f)
                    ),
                    contentPadding = PaddingValues(horizontal = 3.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.init_ds_local),
                        color = LocalColors.current.onSecondaryContainer
                    )
                }

                // start local DS
                Button(
                    modifier = Modifier
                        .height(80.dp)
                        .width(350.dp),
                    onClick = {
                        mainScreenViewModel.startDigitalSignage()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalColors.current.secondaryContainer,
                        disabledContainerColor = White.copy(0.1f)
                    ),
                    contentPadding = PaddingValues(horizontal = 3.dp),
                    enabled = initializationState == InitializationState.LocalDS,
                ) {
                    Text(
                        text = stringResource(id = R.string.start_ds_local),
                        color = LocalColors.current.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // init remote DS
                Button(
                    modifier = Modifier
                        .height(80.dp)
                        .width(350.dp),
                    onClick = {
                        mainScreenViewModel.initLibraryForRemoteDigitalSignage(context = context)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalColors.current.secondaryContainer,
                        disabledContainerColor = White.copy(0.1f)
                    ),
                    contentPadding = PaddingValues(horizontal = 3.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.init_ds_remote),
                        color = LocalColors.current.onSecondaryContainer
                    )
                }

                // start remote DS
                Button(
                    modifier = Modifier
                        .height(80.dp)
                        .width(350.dp),
                    onClick = {
                        mainScreenViewModel.startDigitalSignage()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LocalColors.current.secondaryContainer,
                        disabledContainerColor = White.copy(0.1f)
                    ),
                    contentPadding = PaddingValues(horizontal = 3.dp),
                    enabled = initializationState == InitializationState.RemoteDS,
                ) {
                    Text(
                        text = stringResource(id = R.string.start_ds_remote),
                        color = LocalColors.current.onSecondaryContainer
                    )
                }
            }
        }
    }
}