package com.sukumar.et.ETData

import kotlinx.coroutines.flow.Flow

class ETRepos(
private val db : ETDatabase
) {

    suspend fun insertBudget(ib :MonthlyBudget){
    db.budgetDao.insertMonthlyBudget(ib)
    }

    suspend fun insertExpenses(iex : Expense){
        db.budgetDao.insertExpense(iex)
    }

    suspend fun deleteMonthlyBudget(monthName: String){
        db.budgetDao.deleteMonthlyBudget(monthName)
    }

    suspend fun deleteIndividualExpenses(monthId : Int,productId:Int,productName :String){
        db.budgetDao.deleteExpenseByMonthIdAndProductName(monthId,productId, productName)
    }


    fun getMonthlyBudgets(mName : String): Flow<List<MonthlyBudget>> {
        return db.budgetDao.getMonthlyBudgets(mName)
    }

    fun getAllMonthlyBudgets(): Flow<List<MonthlyBudget>> {
        return db.budgetDao.getAllMonthlyBudgets()
    }

    fun getMonthlyExpenses(id:Int): Flow<List<Expense>> {
        return db.budgetDao.getExpensesForMonth(id)
    }

    fun getAllMonthsExpenses(): Flow<List<Expense>>{
        return db.budgetDao.getAllMonthExpenses()
    }
    suspend fun updateBudget(newBudget:Double,mId:Int){
        db.budgetDao.updateBudget(newBudget,mId)
    }
suspend fun getMonthlyBudgetid(mName:String):Int{
    return db.budgetDao.getMonthlyBudgetId(mName)
}


//    repo for Ocations


    suspend fun insertOcation(OcationData :OcationaBudget )
    {
        db.budgetDao.insertOcation(OcationData)
    }


    suspend fun DeleteOcation(OName:String , OId:Int){
        db.budgetDao.DeleteOcation(OName,OId)
    }


    suspend fun updateOBudget(newOBudgetValue: Double, Id: Int){
        db.budgetDao.updateOBudget(newOBudgetValue,Id)
    }


    fun getAllOcations():Flow<List<OcationaBudget>>{
        return db.budgetDao.getAllOcations()
    }




    suspend fun insertOExpenses(OctExpenses:OExpenses){
        db.budgetDao.insertOExpenses(OctExpenses)
    }


    suspend fun deleteOExpenseByOIdAndProductName(Oid: Int,productId:Int, productName: String){
        db.budgetDao.deleteOExpenseByOIdAndProductName(Oid,productId,productName)
    }


    fun getAllOExpenses():Flow<List<OExpenses>>{
        return db.budgetDao.getAllOExpenses()
    }


    fun getIndOExpenses(Oid:Int):Flow<List<OExpenses>>{
        return db.budgetDao.getIndOExpenses(Oid)
    }
}