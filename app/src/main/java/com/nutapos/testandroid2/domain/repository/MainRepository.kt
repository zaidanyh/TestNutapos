package com.nutapos.testandroid2.domain.repository

import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun insertIncome(itemIncome: ItemIncomePos)
    fun getIncomes(): Flow<List<ItemIncomePos>>
    fun updateIncome(itemIncome: ItemIncomePos)
    fun deleteIncome(incomeId: Int)
}
