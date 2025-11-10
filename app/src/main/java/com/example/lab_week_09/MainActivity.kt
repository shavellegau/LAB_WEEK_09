package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- Moshi Imports ---
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.JsonAdapter // Import tambahan untuk tipe eksplisit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton


// Data Model
data class Student(
    var name: String
)

// --- MainActivity Class ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

// --- Root Composable: App (Nav Host) ---
@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { listDataString ->
                navController.navigate("resultContent/?listData=$listDataString")
            }
        }

        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
                defaultValue = "[]"
            })
        ) { backStackEntry ->
            ResultContent(
                listData = backStackEntry.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

// --- Parent Composable: Home (State Holder & Serialization) ---
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    var inputField = remember { mutableStateOf(Student("")) }

    // Moshi setup for serialization (converting List<Student> to JSON)
    val moshi = remember { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }
    val type = remember { Types.newParameterizedType(List::class.java, Student::class.java) }

    // FIX 1: Tentukan tipe eksplisit untuk adapter
    val adapter: JsonAdapter<List<Student>> = remember { moshi.adapter(type) }

    HomeContent(
        listData = listData,
        inputField = inputField.value,

        onInputValueChange = { input ->
            inputField.value = inputField.value.copy(name = input)
        },

        // Input Validation (ensures empty strings are not added)
        onButtonClick = {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value.copy())
                inputField.value = Student("")
            }
        },

        // Logic Navigasi: Serialize List to JSON
        navigateFromHomeToResult = {
            val jsonString = adapter.toJson(listData.toList())
            navigateFromHomeToResult(jsonString)
        }
    )
}

// --- Child Composable: HomeContent (UI Renderer) ---
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OnBackgroundTitleText(text = stringResource(
                    id = R.string.enter_item)
                )

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        onInputValueChange(it)
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}


// --- Result Content Composable (Deserialization & Display) ---
@Composable
fun ResultContent(listData: String) {
    val moshi = remember { Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build() }
    val type = remember { Types.newParameterizedType(List::class.java, Student::class.java) }
    val adapter: JsonAdapter<List<Student>> = remember { moshi.adapter(type) }

    val studentList: List<Student> = remember(listData) {
        try {
            if (listData.isNotEmpty() && listData != "null" && listData != "[]") {
                adapter.fromJson(listData) ?: emptyList()
            } else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OnBackgroundTitleText(text = "Daftar Item Diterima:")
                Spacer(Modifier.height(16.dp))
            }
        }

        items(studentList) { student ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = student.name)
            }
        }

        if (studentList.isEmpty()) {
            item {
                OnBackgroundItemText(text = "Tidak ada data yang dikirim atau data kosong.")
            }
        }
    }
}


// --- Preview Composable ---
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        HomeContent(
            listData = mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono")),
            inputField = Student(""),
            onInputValueChange = {},
            onButtonClick = {},
            navigateFromHomeToResult = {}
        )
    }
}