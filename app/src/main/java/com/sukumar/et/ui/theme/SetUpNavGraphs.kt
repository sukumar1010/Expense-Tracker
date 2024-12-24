package com.sukumar.et.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sukumar.et.ETData.ETViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetUpNavGraphs(
    navController : NavHostController,
    viewm : ETViewModel
){

    NavHost(
        navController = navController,
        startDestination = Screens.SplashSrn.route
    ){
        composable(route=Screens.SplashSrn.route){
            SplashScreen(navController = navController)
        }

        composable(route = Screens.EnterExp.route){
            BudgetTracker(vm = viewm,navController = navController)
        }

        composable(route = Screens.ListHistory.route){
            TotalHistory(vm = viewm , navController = navController)
        }

        composable(
            route = Screens.IndividualHistory.route,
            arguments = listOf(navArgument("MonthName") {
                type= NavType.StringType
            })

        ){

            val MonthName =it.arguments?.getString("MonthName")
            IndHistory(mName = MonthName!! ,vm =viewm)
        }

        composable(
            route = Screens.Ocations.route
        ){
            OcationBudgets(vm=viewm, navController=navController)
        }

        composable(
            route = Screens.OcExpenses.route,
            arguments = listOf(
                navArgument("OID"){ type = NavType.StringType},
                navArgument("OName"){ type = NavType.StringType},
                navArgument("EB"){ type = NavType.StringType}
            )
        ){

            OcationExpens(
                vm = viewm,
                Oid =   it.arguments?.getString("OID")!!.toInt(),
                OName = it.arguments?.getString("OName")!!,
                EstB = it.arguments?.getString("EB")!!

            )
        }

        composable(route = Screens.About.route){
            AppAbout()
        }
    }


}