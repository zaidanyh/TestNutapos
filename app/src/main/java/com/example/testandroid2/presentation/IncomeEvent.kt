package com.example.testandroid2.presentation

import com.example.testandroid2.datasource.local.ItemIncomePos

sealed class IncomeEvent {
    data class InsertIncome(val itemIncome: ItemIncomePos): IncomeEvent()
    data object GetIncomes: IncomeEvent()
    data class UpdateIncome(val itemIncome: ItemIncomePos): IncomeEvent()
    data class DeleteIncome(val incomeId: Int): IncomeEvent()
}