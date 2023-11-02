package com.example.interesesapp.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.interesesapp.components.Alert
import com.example.interesesapp.components.MainButton
import com.example.interesesapp.components.MainTextField
import com.example.interesesapp.components.ShowInfoCards
import com.example.interesesapp.components.SpaceH
import com.example.interesesapp.viewModels.PrestamoViewModels
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun ContentHomeView(paddingValues: PaddingValues, viewModel: PrestamoViewModels){
    Column (
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        var montoPrestamo by remember { mutableStateOf("") }
        var cantCuotas by remember { mutableStateOf("") }
        var tasa by remember { mutableStateOf("") }
        var montoInteres by remember { mutableStateOf(0.0) }
        var montoCuota by remember { mutableStateOf(0.0) }
        //alerta
        var showAlert by remember { mutableStateOf(false) }
        //funcion incluir
        ShowInfoCards(titleInteres = "Total: ",
            montoInteres = montoInteres,
            titleMonto = "Cuota",
            monto = montoCuota 
        )
        MainTextField(value = montoPrestamo,
            onValueChange = {viewModel.onValue(value = it, campo =  "montoPrestamo")}, label ="Prestamo" )
        SpaceH(3.dp)

        MainTextField(value = cantCuotas,
            onValueChange = {viewModel.onValue(value = it, campo =  "cuotas")}, label ="Cuotas" )
        SpaceH(10.dp)

        MainTextField(value = tasa,
            onValueChange = {viewModel.onValue(value = it, campo =  "tasa")}, label ="tasa" )
        SpaceH(10.dp)

        MainButton(text = "Calcular") {

            viewModel.calcular()

        }
        SpaceH()
        
        MainButton(text = "limpar", color= Color.Red) {

            viewModel.limpiar()

        }
        if (viewModel.state.showAlert){
            Alert(title = "Alerta",
                message = "Ingresa datos",
                confirmText = "Aceptar",
                onConfirmClick = {viewModel.confirmDialog()}) { }
        }
        
    }
    val state = viewModel.state
    ShowInfoCards(titleInteres = "Total: ",
        montoInteres = state.montoInteres,
        titleMonto = "Cuota",
        monto =state.montoCuota )
}

fun calcularCuota (montoPrestamo: Double, cantCuotas:Int, tasaInteresAnual: Double): Double {

    val tasaInteresMensual = tasaInteresAnual / 12 / 100

    val cuota = montoPrestamo * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, cantCuotas.toDouble())/
            (Math.pow( 1 + tasaInteresMensual,cantCuotas.toDouble()) -1 )
    val cuotaRedondeada = BigDecimal(cuota).setScale(2,RoundingMode.HALF_UP).toDouble()

    return cuotaRedondeada
    return cuota
}


fun calcularTotal(montoPrestamo: Double, cantCuotas: Int, tasaInteresAnual: Double): Double {
val res = cantCuotas * calcularCuota(montoPrestamo, cantCuotas, tasaInteresAnual)
    return BigDecimal(res).setScale(2,RoundingMode.HALF_UP).toDouble()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView (viewModel: PrestamoViewModels ) {
    Scaffold ( topBar = {
        CenterAlignedTopAppBar(
            title ={ Text(text = "Calcular Prestamos", color = Color.White)},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }
    ){
        ContentHomeView(it, viewModel)
    }
}


