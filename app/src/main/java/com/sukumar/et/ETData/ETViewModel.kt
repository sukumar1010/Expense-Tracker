package com.sukumar.et.ETData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ETViewModel(
    private val rep : ETRepos
) :ViewModel(){

    private val _monthlyBudgets = MutableStateFlow<List<MonthlyBudget>>(emptyList())
    private val _StoreAllO = MutableStateFlow<List<OcationaBudget>>(emptyList())
    val monthlyBudgetsVM: StateFlow<List<MonthlyBudget>> = _monthlyBudgets
    val AllOct : StateFlow<List<OcationaBudget>> = _StoreAllO

    init {

        viewModelScope.launch {
            rep.getAllOcations().collect { oct ->
                _StoreAllO.value = oct
            }
        }

        viewModelScope.launch {
            rep.getAllMonthlyBudgets().collect { budgets ->
                _monthlyBudgets.value = budgets
            }
        }
    }


        suspend fun insertBudget(ib: MonthlyBudget) {
            rep.insertBudget(ib)
        }

        suspend fun insertExpenses(iex: Expense) {
            rep.insertExpenses(iex)
        }

        suspend fun deleteMonthlyBudget(monthName: String) {
            rep.deleteMonthlyBudget(monthName)
        }

        suspend fun deleteIndividualExpenses(monthId: Int, productId: Int, productName: String) {
            rep.deleteIndividualExpenses(monthId, productId, productName)
        }


        fun getMonthlyBudgets(mName: String): Flow<List<MonthlyBudget>> {
            return rep.getMonthlyBudgets(mName)
        }

        fun getAllMonthlyBudgets(): Flow<List<MonthlyBudget>> {
            return rep.getAllMonthlyBudgets()
        }

        fun getMonthlyExpenses(id: Int): Flow<List<Expense>> {
            return rep.getMonthlyExpenses(id)
        }

        fun getAllMonthsExpenses(): Flow<List<Expense>> {
            return rep.getAllMonthsExpenses()
        }

        suspend fun updateBudget(newBudget: Double, mId: Int) {
            rep.updateBudget(newBudget, mId)
        }

        suspend fun getMonthlyBudgetid(mName: String): Int {
            return rep.getMonthlyBudgetid(mName)
        }

    // for Ocations

    suspend fun insertOcation(OcationData :OcationaBudget )
    {
        rep.insertOcation(OcationData)
    }


    suspend fun DeleteOcation(OName:String , OId:Int){
        rep.DeleteOcation(OName,OId)
    }


    suspend fun updateOBudget(newOBudgetValue: Double, Id: Int){
        rep.updateOBudget(newOBudgetValue,Id)
    }


    fun getAllOcations():Flow<List<OcationaBudget>>{
        return rep.getAllOcations()
    }




    suspend fun insertOExpenses(OctExpenses:OExpenses){
        rep.insertOExpenses(OctExpenses)
    }


    suspend fun deleteOExpenseByOIdAndProductName(Oid: Int,productId:Int, productName: String){
        rep.deleteOExpenseByOIdAndProductName(Oid,productId,productName)
    }


    fun getAllOExpenses():Flow<List<OExpenses>>{
        return rep.getAllOExpenses()
    }


    fun getIndOExpenses(Oid:Int):Flow<List<OExpenses>>{
        return rep.getIndOExpenses(Oid)
    }

}