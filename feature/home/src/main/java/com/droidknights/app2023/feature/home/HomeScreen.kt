package com.droidknights.app2023.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import java.net.UnknownHostException

@Composable
internal fun HomeRoute(
    padding: PaddingValues,
    onSessionClick: () -> Unit,
    onContributorClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val sponsorsUiState by viewModel.sponsorsUiState.collectAsStateWithLifecycle()
    val localContextResource = LocalContext.current.resources

    LaunchedEffect(true) {
        viewModel.errorStateFlow.collectLatest {
            snackBarHostState.showSnackbar(
                when (it.throwable) {
                    is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
                    else -> localContextResource.getString(R.string.error_message_unknown)
                }
            )
        }
    }

    HomeScreen(
        padding = padding,
        sponsorsUiState = sponsorsUiState,
        onSessionClick = onSessionClick,
        onContributorClick = onContributorClick,
    )
}

@Composable
private fun HomeScreen(
    padding: PaddingValues,
    sponsorsUiState: SponsorsUiState,
    onSessionClick: () -> Unit,
    onContributorClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .padding(padding)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
            .padding(bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SessionCard(onClick = onSessionClick)
        ContributorCard(onClick = onContributorClick)
        SponsorCard(uiState = sponsorsUiState)
    }
}
