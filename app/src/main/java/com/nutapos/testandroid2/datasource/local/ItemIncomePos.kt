package com.nutapos.testandroid2.datasource.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "incomes")
@Parcelize
data class ItemIncomePos(
    @PrimaryKey(autoGenerate = true)
    val incomeId: Int?,

    @field:SerializedName("destination")
    val destination: String,

    @field:SerializedName("sourceIncome")
    val sourceIncome: String,

    @field:SerializedName("nominal")
    val nominal: Int,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("time")
    val time: String
): Parcelable
