package com.example.apirest_minecraft.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apirest_minecraft.R
import com.example.apirest_minecraft.data.Item
import com.example.apirest_minecraft.viewmodels.ItemsUiState
import com.example.apirest_minecraft.viewmodels.ItemsViewModel

@Composable
fun ItemsScreen(
    itemsViewModel: ItemsViewModel = viewModel(),
    navController: NavController
) {
    val uiState by itemsViewModel.uiState.collectAsState()
    when (uiState) {
        is ItemsUiState.Loading -> LoadingScreen()
        is ItemsUiState.Success -> ItemsList(
            items = (uiState as ItemsUiState.Success).items,
            onDetailsItem = { itemId ->
                Log.d("ItemsScreen", "Item clicked: $itemId")
            }
        )
        is ItemsUiState.Error -> ErrorScreen()
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    Image(
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = "",
        modifier = modifier.size(200.dp)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ItemsList(
    items: List<Item>,
    onDetailsItem: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        columns = GridCells.Fixed(2)
    ) {
        items(items) { item ->
            ItemEntry(item = item, onDetailsItem = onDetailsItem)
        }
    }
}

@Composable
fun ItemEntry(
    item: Item,
    onDetailsItem: (String) -> Unit
) {
    val density = LocalDensity.current.density
    val width = remember { mutableStateOf(0F) }
    val height = remember { mutableStateOf(0F) }
    Card(
        modifier = Modifier.padding(6.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.image ?: "")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(10.dp, 150.dp)
                    .clip(RectangleShape)
                    .onGloballyPositioned {
                        width.value = it.size.width / density
                        height.value = it.size.height / density
                    }
                    .clickable {
                        item.id?.let {
                            Log.d("ItemsScreen", "Item clicked: $it")
                            onDetailsItem(it)
                        }
                    }
            )
            Box(
                modifier = Modifier
                    .size(
                        width = width.value.dp,
                        height = height.value.dp
                    )
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black),
                            startY = 100F,
                            endY = 1000F
                        )
                    )
            )
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = item.name ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
