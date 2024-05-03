package com.nutapos.testandroid2.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nutapos.testandroid2.datasource.local.ItemIncomePos
import kotlinx.coroutines.flow.Flow

@Dao
interface MutationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIncome(itemIncome: ItemIncomePos)

    @Query("SELECT * FROM incomes")
    fun getIncomes(): Flow<List<ItemIncomePos>>

    @Update
    fun updateIncome(itemIncome: ItemIncomePos)

    @Query("DELETE FROM incomes WHERE incomeId = :incomeId")
    fun deleteIncome(incomeId: Int)
}