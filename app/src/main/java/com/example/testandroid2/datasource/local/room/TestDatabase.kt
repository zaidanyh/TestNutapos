package com.example.testandroid2.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testandroid2.datasource.local.ItemIncomePos

@Database(entities = [ItemIncomePos::class], version = 1)
abstract class TestDatabase: RoomDatabase() {
    abstract fun mutationDao(): MutationDao
}