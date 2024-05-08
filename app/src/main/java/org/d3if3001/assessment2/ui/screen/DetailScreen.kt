package org.d3if3001.assessment2.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3001.assessment2.R
import org.d3if3001.assessment2.database.SepatuDb
import org.d3if3001.assessment2.navigation.SetupNavGraph
import org.d3if3001.assessment2.ui.theme.Assessment2Theme
import org.d3if3001.assessment2.util.ViewModelFactory

const val KEY_ID = "idMahasiswa"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null){
    val context = LocalContext.current
    val db = SepatuDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var ukuran by remember {
        mutableStateOf("")
    }
    var nama by remember {
        mutableStateOf("")
    }
    var wilayah by remember {
        mutableStateOf("")
    }
    var kondisiButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true){
        if (id == null) return@LaunchedEffect

        val data = viewModel.getSepatu(id) ?: return@LaunchedEffect
        ukuran = data.ukuran.toString()
        nama = data.nama
        wilayah = data.wilayah
        kondisiButton = true

    }
    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null) Text(text = stringResource(id = R.string.tambah_data))
                    else Text(text = stringResource(id = R.string.edit_data))
                },

                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (nama.equals("") || ukuran.equals("")){
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            } else if (ukuran.toInt() < 40) {
                                Toast.makeText(context, R.string.invalid_ukuran, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) viewModel.insert(nama, ukuran.toInt(),wilayah)
                            else viewModel.update(id, nama, ukuran.toInt(), wilayah)
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null){
                        DeleteById {
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        },
    ){
            padding ->
        FormSepatu(
            ukuran = ukuran,
            onUkuranChange = { ukuran = it },
            nama =  nama,
            onNamaChange = { nama = it},
            wilayah = wilayah,
            onWilayahChange = { wilayah = it },
            kondisi = kondisiButton,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun FormSepatu(
    ukuran: String,
    onUkuranChange: (String) -> Unit,
    nama: String,
    onNamaChange: (String) -> Unit,
    wilayah: String,
    onWilayahChange: (String) -> Unit,
    kondisi: Boolean,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

            OutlinedTextField(
                value = ukuran,
                onValueChange = { onUkuranChange(it) },
                label = { Text(text = stringResource(id = R.string.ukuran)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
        Column (
                modifier = Modifier.padding(32.dp)
            ){
                Text(text = "Pilih Wilayah : ")
                listOf("UK", "US").forEach { WilayahOpsi ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable {
                            onWilayahChange(WilayahOpsi)
                        }
                    ) {
                        RadioButton(
                            selected = wilayah == WilayahOpsi,
                            onClick = {
                                onWilayahChange(WilayahOpsi)
                            }
                        )
                        Text(text = WilayahOpsi)
                    }
                }
            }
        Divider()
        if (!nama.equals("") && !ukuran.equals("") && !wilayah.equals("") && ukuran.toInt() >= 40 && kondisi){
            Text(text = "Ukuran Convert : \n" + stringResource(id = Convert(ukuran, wilayah)),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ))
            }
        }
    }

fun Convert(ukuran: String, wilayah: String): Int {
    return if (wilayah.equals("UK")){
        when {
            ukuran.equals("40") -> R.string.enam
            ukuran.equals("41") -> R.string.tujuh
            ukuran.equals("42") -> R.string.delapan
            ukuran.equals("43") || ukuran.equals("44") -> R.string.sembilan
            ukuran.equals("45") -> R.string.sepuluh
            ukuran.equals("46") -> R.string.sebelas
            else -> {
                R.string.sebelas_lebih
            }
        }
    } else {
        when{
            ukuran.equals("40") -> R.string.tujuh
            ukuran.equals("41") || ukuran.equals("42") -> R.string.delapan
            ukuran.equals("43") -> R.string.sembilan
            ukuran.equals("44") -> R.string.sepuluh
            ukuran.equals("45") -> R.string.sebelas
            ukuran.equals("46") -> R.string.dua_belas
            else -> {
                R.string.dua_belas_lebih
            }
        }
    }
}

@Composable
fun DeleteById(delete: () -> Unit){
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.hapus)) }, onClick = {
                expanded = false
                delete() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assessment2Theme {
        DetailScreen(rememberNavController())
    }
}