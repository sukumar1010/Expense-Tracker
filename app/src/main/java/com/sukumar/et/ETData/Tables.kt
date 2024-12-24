package com.sukumar.et.ETData

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_budgets")
data class MonthlyBudget(

    val monthName: String ,
    val totalBudget: Double =0.0,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity(
    tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = MonthlyBudget::class,
        parentColumns = ["id"],
        childColumns = ["monthId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Expense(

    val monthId: Int,
    val productName: String = "",
    val productAmount: Double=0.0,
    val dateTime: String ,// store date and time as timestamp
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)


@Entity(tableName = "OcationaBudget")
data class OcationaBudget(

    val OcationName: String ,
    val EstimatedBudget: Double =0.0,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

@Entity(
    tableName = "OExpenses",
    foreignKeys = [ForeignKey(
        entity = OcationaBudget::class,
        parentColumns = ["id"],
        childColumns = ["OId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class OExpenses(
    val OId :Int,
    val productName: String = "",
    val productAmount: Double=0.0,
    val dateTime: String ,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)