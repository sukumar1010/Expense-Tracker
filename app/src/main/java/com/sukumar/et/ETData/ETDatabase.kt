package com.sukumar.et.ETData

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [MonthlyBudget::class, Expense::class,OcationaBudget::class,OExpenses::class],
    version = 1,
    exportSchema = false
)
abstract class ETDatabase : RoomDatabase() {
    abstract val budgetDao : ETDoa
}
