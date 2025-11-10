package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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

// Pastikan imports untuk Custom UI Elements sudah ada:
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
// import com.example.lab_week_09.R


// Data Model
data class Student(
    var name: String
)

// MainActivity Class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                // 5. Update Surface to use App()
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

// 4. Root Composable: App (Nav Host)
@Composable
fun App(navController: NavHostController) {
    // NavHost creates a navigation graph
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Route "home"
        composable("home") {
            // Pass navigation lambda to Home Composable
            Home { listDataString ->
                navController.navigate("resultContent/?listData=$listDataString")
            }
        }

        // Route "resultContent" with arguments
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            // Pass the value of the argument to the ResultContent composable
            ResultContent(
                listData = backStackEntry.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

// 6. Home Composable (State Holder & Navigation Logic)
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit // New parameter for navigation
) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    var inputField = remember { mutableStateOf(Student("")) }

    // 8. Update HomeContent calling function
    HomeContent(
        listData = listData,
        inputField = inputField.value,

        // Event: Update input field
        onInputValueChange = { input ->
            inputField.value = inputField.value.copy(name = input)
        },

        // Event: Add item and reset input
        onButtonClick = {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value.copy())
                inputField.value = Student("")
            }
        },

        // Event: Navigate, converting listData to String before passing
        navigateFromHomeToResult = {
            // Convert List<Student> to a single String representation
            navigateFromHomeToResult(listData.toList().toString())
        }
    )
}

// 7. & 9. Child Composable: HomeContent (UI Renderer with new button)
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit // New parameter for navigation
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

                // --- New: Row containing two buttons ---
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }
                    // New Finish/Navigate Button
                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
                // --- End Row ---
            }
        }

        // Display list items lazily
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}


// 10. Result Content Composable
@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .padding(16.dp) // Tambahkan padding untuk estetika
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Posisi di tengah layar
    ) {
        // Tampilkan data yang diterima dari navigasi
        OnBackgroundTitleText(text = "Daftar Item Diterima:")
        OnBackgroundItemText(text = listData)
    }
}


// Preview Composable (Updated to reflect HomeContent's new signature)
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        HomeContent(
            listData = mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono")),
            inputField = Student(""),
            onInputValueChange = {},
            onButtonClick = {},
            navigateFromHomeToResult = {} // Dummy function for preview
        )
    }
}