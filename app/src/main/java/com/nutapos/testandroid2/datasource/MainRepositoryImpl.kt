package com.nutapos.testandroid2.datasource

import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import com.nutapos.testandroid2.datasource.local.room.MutationDao
import com.nutapos.testandroid2.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mutationDao: MutationDao
): MainRepository {
    override fun insertIncome(itemIncome: ItemIncomePos) {
        mutationDao.insertIncome(itemIncome)
    }

    override fun getIncomes(): Flow<List<ItemIncomePos>> = mutationDao.getIncomes()

    override fun updateIncome(itemIncome: ItemIncomePos) {
        mutationDao.updateIncome(itemIncome)
    }

    override fun deleteIncome(incomeId: Int) {
        mutationDao.deleteIncome(incomeId)
    }
}
