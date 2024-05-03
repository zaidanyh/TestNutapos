package com.nutapos.testandroid2.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nutapos.testandroid2.datasource.local.ItemIncomePos

@Database(entities = [ItemIncomePos::class], version = 1, exportSchema = false)
abstract class TestDatabase: RoomDatabase() {
    abstract fun mutationDao(): MutationDao
}