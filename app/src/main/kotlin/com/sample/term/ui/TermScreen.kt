package com.sample.term.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.term.R
import com.sample.term.ui.theme.TermTheme

@Composable
internal fun TermScreen(
    navigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    val enabled = remember { mutableStateOf(value = false) }
    val endOfListReached = remember {
        derivedStateOf { scrollState.isScrolledToEnd() }
    }

    LaunchedEffect(endOfListReached.value) {
        if (endOfListReached.value) {
            enabled.value = endOfListReached.value
        }
    }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomBar(
                enabled = enabled.value,
                onAgreeClick = navigateToNextScreen,
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                )
                .padding(all = 16.dp),
        ) {
            item {
                // 1.7.0+ (BOM: 2024.09.00+)
                // https://android-developers.googleblog.com/2024/05/whats-new-in-jetpack-compose-at-io-24.html#:~:text=AnnotatedString.fromHtml()
                Text(
                    text = AnnotatedString.fromHtml(
                        htmlString = stringResource(id = R.string.term_content),
                    ),
                    fontSize = 14.sp,
                )
            }
            item {
                // リスト下端に到達したことを検知させるため、余分に item を置いている
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text(text = "アプリ利用規約") },
        modifier = modifier,
    )
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onAgreeClick: () -> Unit,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        Button(
            onClick = onAgreeClick,
            enabled = enabled,
            content = { Text(text = "同意する") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// リスト下端を検知する拡張関数
private fun LazyListState.isScrolledToEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@PreviewLightDark
@Composable
private fun TermScreenPreview() {
    TermTheme {
        TermScreen(navigateToNextScreen = {})
    }
}
