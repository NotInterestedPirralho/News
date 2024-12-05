package com.example.news.ui.favorites

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.database.AppDatabase
import com.example.news.models.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ArticlesState (
    val articles: List<Article> = arrayListOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class  FavotritesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ArticlesState())
    val uiState : StateFlow<ArticlesState> = _uiState.asStateFlow()

    fun fetchArticles(context: Context) {
        _uiState.value = ArticlesState(
            isLoading = true,
            error = null)
        viewModelScope.launch (Dispatchers.IO){
            val articles = AppDatabase.getInstance(context)
                ?.articleDao()
                ?.getAll()
            viewModelScope.launch (Dispatchers.Main) {
                _uiState.value = ArticlesState(
                    articles = articles?: emptyList(),
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}