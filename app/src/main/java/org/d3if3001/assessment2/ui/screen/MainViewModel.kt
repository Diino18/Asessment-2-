package org.d3if3001.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3001.assessment2.database.SepatuDao
import org.d3if3001.assessment2.model.Sepatu

class MainViewModel(dao: SepatuDao) : ViewModel() {
    val data: StateFlow<List<Sepatu>> = dao.getSepatu().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}