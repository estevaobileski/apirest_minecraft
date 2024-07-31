package com.example.apirest_minecraft.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apirest_minecraft.data.Item
import com.example.apirest_minecraft.network.MinecraftApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

sealed class ItemsUiState {
    object Loading : ItemsUiState()
    data class Success(val items: List<Item>) : ItemsUiState()
    object Error : ItemsUiState()
}

class ItemsViewModel : ViewModel() {

    private var _uiState: MutableStateFlow<ItemsUiState> = MutableStateFlow(ItemsUiState.Loading)
    val uiState: StateFlow<ItemsUiState> = _uiState.asStateFlow()

    init {
        getItems()
    }

    private fun getItems() {
        viewModelScope.launch {
            try {
                val items: List<Item> = MinecraftApi.retrofitService.getItems()
                _uiState.value = ItemsUiState.Success(items)
            } catch (e: IOException) {
                _uiState.value = ItemsUiState.Error
                Log.e("ItemsViewModel", "Failure IOException: ${e.message}")
            } catch (e: HttpException) {
                _uiState.value = ItemsUiState.Error
                Log.e("ItemsViewModel", "Failure HttpException: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = ItemsUiState.Error
                Log.e("ItemsViewModel", "An unexpected error occurred: ${e.message}")
            }
        }
    }
}
