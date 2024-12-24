package com.sukumar.et.ui.theme

sealed class Screens(val route : String){
    object SplashSrn : Screens(route = "splashScreen")

    object EnterExp : Screens(route = "enterExp")
    object ListHistory : Screens(route = "listHistory")
    object IndividualHistory : Screens(route = "individualHistory/{MonthName}")
    object Ocations : Screens(route = "ocations")
    object OcExpenses : Screens(route = "ocExpenses/{OID}/{OName}/{EB}")
    object About : Screens(route = "about")

}
