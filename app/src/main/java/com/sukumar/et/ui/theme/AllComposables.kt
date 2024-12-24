package com.sukumar.et.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.sukumar.et.R
import kotlinx.coroutines.delay
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController


import androidx.compose.ui.unit.dp
import com.sukumar.et.ETData.ETViewModel
import com.sukumar.et.ETData.Expense
import com.sukumar.et.ETData.MonthlyBudget
import kotlinx.coroutines.launch


import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.content.FileProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sukumar.et.ETData.OExpenses
import com.sukumar.et.ETData.OcationaBudget

@Composable
fun SplashScreen(
    navController: NavController
){

    LaunchedEffect(key1 = true) {

        delay(1000L)
        navController.popBackStack()
        navController.navigate(Screens.EnterExp.route)

    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.et),
            contentDescription = "Expense Tracker",
            modifier = Modifier
                .clip(RoundedCornerShape(250.dp))
        )
    }

}




@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState", "RememberReturnType", "UnrememberedMutableInteractionSource",
    "Range"
)
@Composable
fun BudgetTracker(
    vm : ETViewModel,
    navController: NavController
) {

    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime = currentDateTime.format(formatter)
    val cs = rememberCoroutineScope()
    val currentDate = LocalDate.now()
    val crrMonth = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val crrYear = currentDate.year
    val monthYear = "$crrMonth $crrYear"

    val checkMonth by vm.monthlyBudgetsVM.collectAsState()

    var editBudget by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(checkMonth){
        delay(1000)
        val isCurrentMonthPresent = checkMonth.any { it.monthName == monthYear }
        if(!isCurrentMonthPresent){
            cs.launch {
                vm.insertBudget(MonthlyBudget(
                    monthName = monthYear

                ))

            }

        }
        delay(1000)


    }
    val keyboardController = LocalSoftwareKeyboardController.current

    var currentMonthId = 0
    var expensesInMonth by remember{
        mutableStateOf(0.0)
    }
    var currentMonthName by remember{
        mutableStateOf("")
    }
    var remainingInMonth by remember{
        mutableStateOf(0.0)
    }
    var currentBudget by remember{
        mutableStateOf(0.0)
    }
    val monthlyBudgets by vm.getMonthlyBudgets(monthYear).collectAsState(emptyList())


    var showPriceField by remember { mutableStateOf(false) }
    var showProductField by remember {
        mutableStateOf(true)
    }

    val priceFocusRequester = remember { FocusRequester() }

    monthlyBudgets.forEach { dtls->
        currentMonthId = dtls.id
        currentMonthName = dtls.monthName
        currentBudget = dtls.totalBudget
    }


    val con = LocalContext.current
    val expensesFlow = vm.getMonthlyExpenses(currentMonthId)

    val thisMonthExpenses by expensesFlow.collectAsState(initial = emptyList())

    val totalItems = thisMonthExpenses.size

    LaunchedEffect(key1 = thisMonthExpenses, key2 = currentBudget) {
        expensesInMonth = thisMonthExpenses.sumOf { it.productAmount }
        remainingInMonth = currentBudget - expensesInMonth
    }


    var inputText by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }


    val listState = rememberLazyListState()

    LaunchedEffect(thisMonthExpenses.size) {

        listState.animateScrollToItem(thisMonthExpenses.size )
    }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(color = Color(0xFFFFFBFA))
            ) {
                var dropdownMenuVisible by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF474935))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentMonthName,
                        color = Color(0xFFFFFFFF),
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    ) {

                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clickable { dropdownMenuVisible = !dropdownMenuVisible }


                        )
                        DropdownMenu(
                            expanded = dropdownMenuVisible,
                            onDismissRequest = { dropdownMenuVisible = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Share",
                                        color = Color(0xFF000000),
                                        fontSize = 17.sp

                                    )
                                       },
                                onClick = {
                                    dropdownMenuVisible =false
                                    val genpdf = generatePdf(con,monthlyBudgets,thisMonthExpenses)
                                    genpdf?.let {
                                        sharePdf(con, it)
                                    }

                                }
                            )
                            DropdownMenuItem(
                                text = { Text("History",color = Color(0xFF000000), fontSize = 17.sp) },
                                onClick = {
                                    dropdownMenuVisible = false
                                    navController.navigate(Screens.ListHistory.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Occasion",color = Color(0xFF000000), fontSize = 17.sp) },
                                onClick = {
                                    dropdownMenuVisible = false
                                    navController.navigate(Screens.Ocations.route)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("About",color = Color(0xFF000000), fontSize = 17.sp) },
                                onClick = {
                                    dropdownMenuVisible = false
                                    navController.navigate(Screens.About.route)
                                }
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 4f
                            )
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Total Budget", color = Color(0xFF000000))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹$currentBudget",
                            color = Color(0xFF6F9283),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            editBudget = true
                                            showProductField = false

                                        }
                                    )
                                }
                                .background(color = Color(0xFFF0F0F0))
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Remaining", color = Color(0xFF000000))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "₹$remainingInMonth", style = MaterialTheme.typography.bodyLarge,color = Color(0xFF6F9283))

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Total Expenses", color = Color(0xFF000000))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "₹$expensesInMonth", style = MaterialTheme.typography.bodyLarge,color = Color(0xFF6F9283))


                    }
                }

            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFBFA))
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(thisMonthExpenses) { expense ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            cs.launch {
                                                vm.deleteIndividualExpenses(
                                                    currentMonthId,
                                                    expense.id,
                                                    expense.productName
                                                )
//                                                delay(1000)
                                            }
                                        }
                                    )
                                }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = expense.productName,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = expense.dateTime,
                                    color = Color.Black,// Display the time here
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = "${expense.productAmount}",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }


                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    if(showProductField){
//                        showPriceField=false
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                         placeholder = { Text(text = "Items : $totalItems")},

                        label = { Text(text = "Enter Product Name", fontSize = 16.sp) },

                        modifier = Modifier
                            .weight(1f)
                            //.height(45.dp)// Occupy available space in the row
                            .padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if(productName.isNotEmpty()){
                                    showPriceField = true
                                }
                                else{
                                    Toast.makeText(con,"Enter Product name",Toast.LENGTH_LONG).show()
                                }


                            }
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,color=Color.Black
                        )

                    )
                }


                    if (showPriceField) {
                        LaunchedEffect(Unit) {
                            priceFocusRequester.requestFocus()
                        }
                        OutlinedTextField(
                            value = productPrice,
                            onValueChange = { productPrice = it },
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
                            placeholder = {Text(text = "Enter Price", fontSize = 16.sp)},
                            modifier = Modifier
                                .weight(1f) // Occupy available space in the row
                                .focusRequester(priceFocusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),

                                keyboardActions = KeyboardActions(

                                        onDone = {

                                            // Insert into the database
                                            if(productPrice.isNotEmpty()){
                                                keyboardController?.hide()
                                                cs.launch {
                                                    vm.insertExpenses(
                                                        Expense(
                                                            monthId = currentMonthId!!,
                                                            productName = productName,
                                                            productAmount = productPrice.toDoubleOrNull()
                                                                ?: 0.0,
                                                            dateTime = formattedDateTime
                                                        )
                                                    )
                                                    // Clear the fields
                                                    productName = ""
                                                    productPrice = ""
                                                    showPriceField = false
                                                }
                                            }else{
                                                Toast.makeText(con,"Enter  Price",Toast.LENGTH_LONG).show()
                                            }
//
                                        }

                                )
                        )

                    }



                    if(showProductField) {

                        IconButton(onClick = {
                            if (productName.isNotEmpty() && productPrice.isNotEmpty()) {
                            cs.launch {

                                    vm.insertExpenses(
                                        Expense(
                                            monthId = currentMonthId!!,
                                            productName = productName,
                                            productAmount = productPrice.toDouble(),
                                            dateTime = formattedDateTime
                                        )
                                    )
                                    inputText = ""
                                    productName = ""
                                    productPrice = ""
                                    keyboardController?.hide()
                                    showPriceField = false
                                }
                            }
                            else{
                                Toast.makeText(con,"Field should not empty",Toast.LENGTH_LONG).show()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                tint = Color.Red,
                                contentDescription = "Add"
                            )
                        }
                    }
                }

                if (editBudget) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                        LaunchedEffect(Unit) {
                            priceFocusRequester.requestFocus()
                        }

                        var currentBudgetState by remember {
                            mutableStateOf(currentBudget.toInt().toString())
                        }

                        OutlinedTextField(
                            value = currentBudgetState,
                            onValueChange = {
                                currentBudgetState = it
                            },
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
                            modifier = Modifier
                                .focusRequester(priceFocusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()

                                    cs.launch {

                                        vm.updateBudget(
                                            currentBudgetState.toInt().toDouble(),
                                            currentMonthId
                                        )

                                    }
                                    currentBudget = currentBudgetState.toInt().toDouble()
                                    showProductField=true
                                    editBudget = false
                                }
                            )
                        )
                    }
                }



            }
        }
    )
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TotalHistory(
    vm: ETViewModel,
    navController: NavController
) {
    val allMonths by vm.getAllMonthlyBudgets().collectAsState(initial = emptyList())
val cs = rememberCoroutineScope()
    val con = LocalContext.current
    val currentDate = LocalDate.now()
    val crrMonth = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val crrYear = currentDate.year
    val monthYear = "$crrMonth $crrYear"
    val expandedblock = remember {
        mutableStateListOf<Int>()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.background(Color(0xFFFFFFFF))


    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color(0xFF474935))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Text(text = "History", color = Color.White, fontWeight = FontWeight(600), fontSize = 23.sp)
            }
        }
        items(allMonths.size) { i->

                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = TweenSpec(
                                durationMillis = 700,
                                easing = LinearOutSlowInEasing
                            )
                        )
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF6EA4BF))
                        .clickable {
                            navController.navigate("individualHistory/${allMonths[i].monthName}")
                        }

                ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF6EA4BF))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {

                        Text(


                            text = allMonths[i].monthName,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFFFFFFF),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                            IconButton(onClick = {
                                if(i in expandedblock){
                                    expandedblock.remove(i)
                                }
                                else{
                                    expandedblock.add(i)
                                }

                            }) {
                                Icon(imageVector = if (i in expandedblock) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.White)
                            }

                }
                    if(i in expandedblock){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Budget:  ₹${allMonths[i].totalBudget}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 18.sp,
                                color = Color(0xFFFFFFFF),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            if(allMonths[i].monthName!=monthYear) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .padding(end = 20.dp)

                                        .size(24.dp)
                                        .clickable {
                                            cs.launch {
                                                vm.deleteMonthlyBudget(allMonths[i].monthName)
                                            }
                                        }

                                )

                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                    }

                }
        }
    }

}

@Composable
fun IndHistory(
    mName :String,
    vm : ETViewModel

) {

    val currMonthDetails by vm.getMonthlyBudgets(mName).collectAsState(initial = emptyList())
    var crrMonthName by remember {
        mutableStateOf("")
    }
    var crrMonthBudget by remember {
        mutableStateOf(0.0)
    }
    var crrMonthid by remember {
        mutableStateOf(0)
    }
    var crrMonthExpenses by remember {
        mutableStateOf(0.0)
    }
    currMonthDetails.forEach { dt ->
        crrMonthName = dt.monthName
        crrMonthBudget = dt.totalBudget
        crrMonthid = dt.id
    }
    val con = LocalContext.current
    val expensesFlow = vm.getMonthlyExpenses(crrMonthid)

    val thisMonthExpenses by expensesFlow.collectAsState(initial = emptyList())


    LaunchedEffect(key1 = thisMonthExpenses) {
        crrMonthExpenses = thisMonthExpenses.sumOf { it.productAmount }

    }


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(color = Color(0xFFFFFBFA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF474935))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = crrMonthName,
                        color = Color(0xFFFFFFFF),
                        fontSize = 24.sp

                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    ){
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier

                                .size(24.dp)
                                .clickable {
                                    val genpdf =
                                        generatePdf(con, currMonthDetails, thisMonthExpenses)
                                    genpdf?.let {
                                        sharePdf(con, it)
                                    }
                                }

                        )

                }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Budget", style = MaterialTheme.typography.bodyLarge,color = Color.Black,)
                    Text(text = "Total Expenses", style = MaterialTheme.typography.bodyLarge,color = Color.Black)
                } //                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 4f
                            )
                        }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "₹$crrMonthBudget",
                        color = Color(0xFF6F9283),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(text = "₹$crrMonthExpenses", style = MaterialTheme.typography.bodyLarge,color = Color(0xFF6F9283))
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFFFBFA))
            ) {
                LazyColumn(

                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(thisMonthExpenses) { expense ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()

                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = expense.productName,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = expense.dateTime,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = "${expense.productAmount}",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

    )
}


fun generatePdf(context: Context, budgets: List<MonthlyBudget>, expensess: List<Expense>): File? {

    val totalExpensesInMonth = expensess.sumOf { it.productAmount }
    var crMonthName = ""

    val document = PdfDocument()
    val pageHeight = 600
    val pageWidth = 300
    var yPosition = 40f
    val maxYPosition = pageHeight - 10f  // Leave some margin at the bottom
    var currentPageNumber = 1
    var currentPage: PdfDocument.Page? = null
    var canvas = currentPage?.canvas
    val paint = Paint().apply { textSize = 12f }
    val titlePaint = Paint().apply { textSize = 16f; isFakeBoldText = true }

    // Function to create a new page
    fun createNewPage() {
        currentPage?.let { document.finishPage(it) }
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
        currentPage = document.startPage(pageInfo)
        canvas = currentPage?.canvas
        yPosition = 40f
        currentPageNumber++
    }

    // Create the first page
    createNewPage()

    budgets.forEach { budget ->

        crMonthName = budget.monthName
        canvas?.drawText(budget.monthName, pageWidth / 2f - 40f, yPosition, titlePaint)
        yPosition += 40

        canvas?.drawText("Total Budget", 10f, yPosition, titlePaint)
        canvas?.drawText("Total Expenses", pageWidth - 120f, yPosition, titlePaint)
        yPosition += 25
        canvas?.drawText("${budget.totalBudget}", 10f, yPosition, titlePaint)
        canvas?.drawText("${totalExpensesInMonth}", pageWidth - 120f, yPosition, titlePaint)
        yPosition += 40

        // Iterate over the expenses list
        expensess.forEach { product ->
            val itemHeight = 45f  // Height for each expense item (including name, amount, and date)

            // Check if the next item would exceed the page height
            if (yPosition + itemHeight > maxYPosition) {
                createNewPage()  // Create a new page if this item would overflow
            }

            // Draw the expense item on the canvas
            canvas?.drawText(product.productName, 10f, yPosition, paint)
            canvas?.drawText(product.dateTime, 10f, yPosition + 18, paint)
            canvas?.drawText("${product.productAmount}", pageWidth - 120f, yPosition + 7, paint)
            yPosition += itemHeight
        }
    }

    // Finish the current page
    currentPage?.let { document.finishPage(it) }

    // Save the document to a file
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "$crMonthName.pdf")

    try {
        document.writeTo(FileOutputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        document.close()
    }

    return file
}

//fun generatePdf(context: Context, budgets: List<MonthlyBudget>, expensess: List<Expense>): File? {
//
//    val totalExpensesInMonth = expensess.sumOf { it.productAmount }
//    var crMonthName = ""
//
//
//    val document = PdfDocument()
//    val pageHeight = 600
//    val pageWidth = 300
//    var itemsPerPage = 9
//    var yPosition = 40f
//    var currentPageNumber = 1
//    var currentPage: PdfDocument.Page? = null
//    var canvas = currentPage?.canvas
//    val paint = Paint().apply { textSize = 12f }
//    val titlePaint = Paint().apply { textSize = 16f; isFakeBoldText = true }
//
//    fun createNewPage() {
//        currentPage?.let { document.finishPage(it) }
//        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
//        currentPage = document.startPage(pageInfo)
//        canvas = currentPage?.canvas
//        yPosition = 40f
//        if(currentPageNumber>1){
//            itemsPerPage  = 11
//        }
//        currentPageNumber++
//
//    }
//
//    createNewPage()
//
//    budgets.forEach { budget ->
//
//        crMonthName = budget.monthName
//        canvas?.drawText(budget.monthName, pageWidth / 2f - 40f, yPosition, titlePaint)
//        yPosition += 40
//
//
//        canvas?.drawText("Total Budget", 10f, yPosition, titlePaint)
//        canvas?.drawText("Total Expenses", pageWidth - 120f, yPosition, titlePaint)
//        yPosition += 25
//        canvas?.drawText("${budget.totalBudget}", 10f, yPosition, titlePaint)
//        canvas?.drawText("${totalExpensesInMonth}", pageWidth - 120f, yPosition, titlePaint)
//        yPosition += 40
//
//
//        expensess.forEachIndexed { index, product ->
//            if (index % itemsPerPage == 0 && index != 0) {
//                createNewPage()
//            }
//            canvas?.drawText(product.productName, 10f, yPosition, paint)
//            canvas?.drawText(product.dateTime,10f,yPosition+18,paint)
//            canvas?.drawText("${product.productAmount}", pageWidth - 120f, yPosition+7, paint)
//            yPosition += 45
//        }
//
//    }
//
//    currentPage?.let { document.finishPage(it) }
//
//    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//    val file = File(directory, "$crMonthName.pdf")
//
//    try {
//        document.writeTo(FileOutputStream(file))
//    } catch (e: IOException) {
//        e.printStackTrace()
//        return null
//    } finally {
//        document.close()
//    }
//
//    return file
//
//}


fun sharePdf(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
}


@Composable
fun AppAbout() {
    val context = LocalContext.current

    val email = "kumareng967@gmail.com"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4E5166))
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF474935))
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Text(
                text = "Hi There",
                fontSize = 30.sp,
                color = Color(0xFFEDEDF4)
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top=100.dp, start = 20.dp)
        ) {
            Text(
                text = "1.You can delete the products by double tap on it",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "2.You can Edit budget by double tap on it",
                color = Color.White,
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("3.Report to this mail ")
                    pushStringAnnotation(tag = "email", annotation = email)
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            textDecoration = TextDecoration.Underline
                        ),
                    ) {
                        append(email)
                    }
                    pop()
                    append(" if you find any bug")
                },
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$email")
                    }
                    context.startActivity(intent)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "4.Don't forget to share your feedback on PlayStore",
                color = Color.White,
                fontSize = 20.sp,
            )
        }
    }
}


@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (String) -> Unit
) {
    var OccName by remember { mutableStateOf("") }


    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),


        ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color(0xFF00B0FF))
            ,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)

            ) {
                Text(text = "Occasion Name",color = Color.Red)
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = OccName,
                    onValueChange = { OccName = it }

                )




                Spacer(modifier = Modifier.padding(8.dp))


                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(),  colors = ButtonDefaults.buttonColors( Color(0xFFff477e)),) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF00B0FF)),
                    onClick = {
                        onAddItem(OccName)
                    }
                ) {
                    Text("Ok")
                }

            }
        }
    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OcationBudgets(
    vm: ETViewModel,
    navController: NavController
) {
    val AllOccations by vm.AllOct.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var editBudget by remember {
        mutableStateOf(false)
    }
    var currentOBudget by remember{
        mutableStateOf(0.0)
    }
    var bId by remember {
        mutableStateOf(0)
    }
val con = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(AllOccations.size) {

        listState.animateScrollToItem(AllOccations.size )
    }
var showButton by remember{ mutableStateOf(true) }



    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(horizontal = 2.dp)
                    .background(Color(0xFFFFFBFA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = Color(0xFF474935))
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ){
                    Text(text = "Occasions", color = Color.White, fontWeight = FontWeight(600), fontSize = 23.sp)
                }

                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {

                    items(AllOccations) { itms ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF9FA0C3))
                                .clickable {
                                    navController.navigate("ocExpenses/${itms.id}/${itms.OcationName}/${itms.EstimatedBudget}")
                                }
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                        .background(Color(0xFF9FA0C3))
                                        ,
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = itms.OcationName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color(0xFF000000),
                                        fontSize = 24.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.End)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFf72585),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {
                                                    scope.launch {
                                                        vm.DeleteOcation(itms.OcationName, itms.id)
                                                    }
                                                }
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Estimated Budget: ₹${itms.EstimatedBudget}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 17.sp,
                                    color = Color(0xFF000000),
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 3.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onDoubleTap = {
                                                    editBudget = true
                                                    showButton = false
                                                    currentOBudget = itms.EstimatedBudget
                                                    bId = itms.id

                                                })
                                        }

                                )

                            }
                        }
                    }
                }


                if(showButton) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            //.background(Color.Green)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FA0C3)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Add",
                                    color = Color.Black,
                                    fontSize = 18.sp,

                                    )
                            }
                        }
                    }
                }


                if (showDialog) {
                    AddItemDialog(
                        onDismiss = { showDialog = false }
                    ) { OccName ->
                        if(OccName.isNotEmpty() and OccName.isNotBlank()){
                            scope.launch {
                                vm.insertOcation(OcationaBudget(OcationName = OccName))
                            }
                            showDialog = false

                        }else{
                            Toast.makeText(con,"Enter occasion ",Toast.LENGTH_LONG).show()
                        }


                    }
                }

                if (editBudget) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    val priceFocusRequester = remember { FocusRequester() }

                        LaunchedEffect(Unit) {
                            priceFocusRequester.requestFocus()
                        }

                        var currentBudgetState by remember {
                            mutableStateOf(currentOBudget.toInt().toString())
                        }

                        OutlinedTextField(
                            value = currentBudgetState,
                            onValueChange = {
                                currentBudgetState = it
                            },
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
                            modifier = Modifier
                                .focusRequester(priceFocusRequester),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()

                                    scope.launch {

                                        vm.updateOBudget(
                                            currentBudgetState.toInt().toDouble(),
                                            bId
                                        )

                                    }
                                    currentOBudget = currentBudgetState.toInt().toDouble()
                                    showButton=true
                                    editBudget = false
                                }
                            )
                        )
                    }
                }

            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OcationExpens(
    vm:ETViewModel,
    Oid : Int,
    OName : String,
    EstB :String
){

    val con = LocalContext.current
    val octExpensesFlow = vm.getIndOExpenses(Oid)

    val scope = rememberCoroutineScope()
    val octExpenses by octExpensesFlow.collectAsState(initial = emptyList())
    var totalOctExp by remember{
        mutableStateOf(0.0)
    }
    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = octExpenses) {
        totalOctExp = octExpenses.sumOf { it.productAmount }

    }

    val listState = rememberLazyListState()

    LaunchedEffect(octExpenses.size) {

        listState.animateScrollToItem(octExpenses.size )
    }

    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateTime = currentDateTime.format(formatter)


    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(color = Color(0xFFFFFBFA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFF474935))
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = OName,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                    ){
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White,
                            modifier = Modifier

                                .size(24.dp)
                                .clickable {
                                    val genpdf =
                                        generatePdfForO(
                                            con,
                                            OName = OName,
                                            OBudget = EstB,
                                            octExpenses
                                        )
                                    genpdf?.let {
                                        sharePdf(con, it)
                                    }
                                }

                        )

                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 4f
                            )
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Estimated Budget", style = MaterialTheme.typography.bodyLarge,color = Color.Black,)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "₹$EstB",
                            color = Color(0xFF68B0AB),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Total Expenses", style = MaterialTheme.typography.bodyLarge,color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "₹$totalOctExp", style = MaterialTheme.typography.bodyLarge,color = Color(0xFF68B0AB))
                    }


                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFFFFBFA))
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(octExpenses) { expense ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onDoubleTap = {
                                            scope.launch {
                                                vm.deleteOExpenseByOIdAndProductName(
                                                    Oid,
                                                    expense.id,
                                                    expense.productName
                                                )
//                                                delay(1000)
                                            }
                                        }
                                    )
                                }

                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = expense.productName,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = expense.dateTime,
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = "${expense.productAmount}",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }


                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF68B0AB)),
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                ,
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Add",
                                color = Color.White,
                                fontSize = 18.sp,

                            )
                        }
                    }
                }

                if (showDialog) {
                    AddOExpenses(
                        onDismiss = { showDialog = false }
                    ) { Pname,Pprice ->
                        if(Pname.isNotEmpty() and Pprice.isNotEmpty() and Pname.isNotBlank() and Pprice.isNotBlank()) {
                            val price = Pprice.toDoubleOrNull()
                                ?: 0.0
                            scope.launch {
                                vm.insertOExpenses(OExpenses(Oid, Pname, price, formattedDateTime))
                            }
                            showDialog = false
                        }else{
                            Toast.makeText(con,"Fields should be filled",Toast.LENGTH_LONG).show()
                        }

                    }
                }
            }
        }

    )


}




@Composable
fun AddOExpenses(
    onDismiss: () -> Unit,
    onAddItem: (String,String) -> Unit
) {
    var pName by remember { mutableStateOf("") }
    var pPrice by remember {
        mutableStateOf("")
    }


    val priceFocusRequester = remember { FocusRequester() }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),


        ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color(0xFF00B0FF))
            ,
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)

            ) {
                Text(text = "Product Name",color = Color.Red)
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = pName,
                    onValueChange = { pName = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            priceFocusRequester.requestFocus()
                        }
                    )

                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Product Price",color = Color.Red)
                Spacer(modifier = Modifier.height(5.dp))
                OutlinedTextField(
                    value = pPrice,
                    onValueChange = { pPrice = it },
                    modifier = Modifier

                        .focusRequester(priceFocusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )

                )


                Spacer(modifier = Modifier.padding(8.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(),  colors = ButtonDefaults.buttonColors( Color(0xFFff477e)),) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.padding(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF00B0FF)),
                    onClick = {
                        onAddItem(pName,pPrice)
                    }
                ) {
                    Text("Ok")
                }

            }
        }
    }
}

fun generatePdfForO(context: Context, OName:String,OBudget:String, octExpensess: List<OExpenses>): File? {

    val totalExpensesInMonth = octExpensess.sumOf { it.productAmount }


    val document = PdfDocument()
    var pageHeight = 600
    val pageWidth = 300
    var itemsPerPage = 9
    var yPosition = 40f
    var currentPageNumber = 1
    var currentPage: PdfDocument.Page? = null
    var canvas = currentPage?.canvas
    val paint = Paint().apply { textSize = 12f }
    val titlePaint = Paint().apply { textSize = 16f; isFakeBoldText = true }


    fun createNewPage() {
        currentPage?.let { document.finishPage(it) }
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, currentPageNumber).create()
        currentPage = document.startPage(pageInfo)
        canvas = currentPage?.canvas
        yPosition = 40f
        currentPageNumber++
        if (currentPageNumber > 1) {
            itemsPerPage=11
        }

    }

    createNewPage()

        canvas?.drawText(OName, pageWidth / 2f - 40f, yPosition, titlePaint)
        yPosition += 40

        canvas?.drawText("Estimated Budget", 10f, yPosition, titlePaint)
        canvas?.drawText("Total Expenses", pageWidth - 120f, yPosition, titlePaint)
        yPosition += 25
        canvas?.drawText("${OBudget}", 10f, yPosition, titlePaint)
        canvas?.drawText("${totalExpensesInMonth}", pageWidth - 120f, yPosition, titlePaint)
        yPosition += 40


        octExpensess.forEachIndexed { index, product ->
            if (index % itemsPerPage == 0 && index != 0) {
                createNewPage()

            }
            canvas?.drawText(product.productName, 10f, yPosition, paint)
            canvas?.drawText(product.dateTime, 10f, yPosition+15, paint)
            canvas?.drawText("${product.productAmount}", pageWidth - 120f, yPosition+7, paint)
            yPosition += 45
        }


    currentPage?.let { document.finishPage(it) }

    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "$OName.pdf")

    try {
        document.writeTo(FileOutputStream(file))
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        document.close()
    }

    return file

}