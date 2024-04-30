package com.example.testandroid2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testandroid2.datasource.MainRepository
import com.example.testandroid2.datasource.local.ItemIncomePos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
): ViewModel() {

    private val _incomes = MutableStateFlow<List<ItemIncomePos>?>(null)
    val incomes: StateFlow<List<ItemIncomePos>?> = _incomes

    fun onTriggerEvents(event: IncomeEvent) {
        when(event) {
            is IncomeEvent.InsertIncome -> insertIncome(event.itemIncome)
            is IncomeEvent.GetIncomes -> getIncomes()
            is IncomeEvent.UpdateIncome -> updateIncome(event.itemIncome)
            is IncomeEvent.DeleteIncome -> deleteIncome(event.incomeId)
        }
    }

    private fun getIncomes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getIncomes().collect {
                _incomes.value = it
            }
        }
    }

    private fun insertIncome(itemIncome: ItemIncomePos) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIncome(itemIncome)
        }
    }

    private fun updateIncome(itemIncome: ItemIncomePos) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateIncome(itemIncome)
        }
    }

    private fun deleteIncome(incomeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteIncome(incomeId)
        }
    }
}