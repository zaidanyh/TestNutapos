package com.nutapos.testandroid2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import com.nutapos.testandroid2.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _dataIncome = MutableStateFlow<ItemIncomePos?>(null)
    val dataIncome: StateFlow<ItemIncomePos?> get() = _dataIncome
    private val _incomes = MutableStateFlow<List<ItemIncomePos>?>(null)
    val incomes: StateFlow<List<ItemIncomePos>?> get() = _incomes

    fun onTriggerEvents(event: IncomeEvent) {
        when(event) {
            is IncomeEvent.InsertIncome -> insertIncome(event.itemIncome)
            is IncomeEvent.GetIncomes -> getIncomes()
            is IncomeEvent.UpdateIncome -> updateIncome(event.itemIncome)
            is IncomeEvent.DeleteIncome -> deleteIncome(event.incomeId)
            is IncomeEvent.SetIncomePos -> setIncome(event.incomePos)
        }
    }

    private fun setIncome(incomePos: ItemIncomePos) {
        _dataIncome.value = incomePos
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