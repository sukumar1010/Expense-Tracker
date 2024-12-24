package com.sukumar.et.ETData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ETDoa {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMonthlyBudget(monthlyBudget: MonthlyBudget)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExpense(expense: Expense)


    @Query("DELETE FROM monthly_budgets  where monthName =:monthlyBudget")
    suspend fun deleteMonthlyBudget(monthlyBudget: String)

    @Query("DELETE FROM expenses WHERE monthId = :monthId AND id =:productId  AND productName = :productName")
    suspend fun deleteExpenseByMonthIdAndProductName(monthId: Int,productId:Int, productName: String)


    @Transaction
    @Query("SELECT * FROM monthly_budgets WHERE monthName = :mName")
    fun getMonthlyBudgets(mName : String): Flow<List<MonthlyBudget>>

    @Transaction
    @Query("SELECT * FROM monthly_budgets ")
    fun getAllMonthlyBudgets(): Flow<List<MonthlyBudget>>

    @Query("SELECT * FROM expenses WHERE monthId = :monthId")
    fun getExpensesForMonth(monthId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ")
    fun getAllMonthExpenses(): Flow<List<Expense>>

    @Query("UPDATE monthly_budgets SET totalBudget = :newBudgetValue WHERE id = :targetMonthId")
    suspend fun updateBudget(newBudgetValue: Double, targetMonthId: Int)

    @Query("SELECT id FROM monthly_budgets WHERE monthName = :monthName LIMIT 1")
    suspend fun getMonthlyBudgetId(monthName: String): Int

//    queries for ocational budget


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOcation(OcationData :OcationaBudget )

    @Query("DELETE FROM OcationaBudget WHERE OcationName = :OName AND id =:OId")
    suspend fun DeleteOcation(OName:String , OId:Int)

    @Query("UPDATE OcationaBudget SET EstimatedBudget = :newOBudgetValue WHERE id = :Id")
    suspend fun updateOBudget(newOBudgetValue: Double, Id: Int)

    @Transaction
    @Query("SELECT * FROM OcationaBudget")
    fun getAllOcations():Flow<List<OcationaBudget>>



    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOExpenses(OctExpenses:OExpenses)

    @Query("DELETE FROM OExpenses WHERE OId = :Oid AND id =:productId  AND productName = :productName")
    suspend fun deleteOExpenseByOIdAndProductName(Oid: Int,productId:Int, productName: String)

    @Transaction
    @Query("SELECT * FROM OExpenses")
    fun getAllOExpenses():Flow<List<OExpenses>>

    @Query("SELECT * FROM OExpenses where OId =:Oid")
    fun getIndOExpenses(Oid:Int):Flow<List<OExpenses>>


}

