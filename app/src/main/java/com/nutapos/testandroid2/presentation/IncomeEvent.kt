package com.nutapos.testandroid2.presentation

import com.nutapos.testandroid2.datasource.local.ItemIncomePos

sealed class IncomeEvent {
    data class InsertIncome(val itemIncome: ItemIncomePos): IncomeEvent()
    data class SetIncomePos(val incomePos: ItemIncomePos): IncomeEvent()
    data object GetIncomes: IncomeEvent()
    data class UpdateIncome(val itemIncome: ItemIncomePos): IncomeEvent()
    data class DeleteIncome(val incomeId: Int): IncomeEvent()
}