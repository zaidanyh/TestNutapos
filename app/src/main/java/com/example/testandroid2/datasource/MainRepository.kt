package com.example.testandroid2.datasource

import com.example.testandroid2.datasource.local.ItemIncomePos
import com.example.testandroid2.datasource.local.room.MutationDao

class MainRepository(
    private val mutationDao: MutationDao
) {
    fun insertIncome(itemIncome: ItemIncomePos) = mutationDao.insertIncome(itemIncome = itemIncome)
    fun getIncomes() = mutationDao.getIncomes()
    fun updateIncome(itemIncome: ItemIncomePos) = mutationDao.updateIncome(itemIncome = itemIncome)
    fun deleteIncome(incomeId: Int) = mutationDao.deleteIncome(incomeId = incomeId)
}