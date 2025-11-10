package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme // Ganti dengan path ke theme Anda
// import com.example.lab_week_09.R // Pastikan ini di-import jika R.string error

// --- 1. Data Model ---
// Declare a data class called Student
data class Student(
    var name: String
)

// --- 2. MainActivity Class ---
// Previously we extend AppCompatActivity, now we extend ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Here, we use setContent instead of setContentView
        setContent {
            // Here, we wrap our content with the theme
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // We use Modifier.fillMaxSize() to make the surface fill the whole screen
                    modifier = Modifier.fillMaxSize(),
                    // We use MaterialTheme.colorScheme.background to get the background color
                    // and set it as the color of the surface
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Here, we call the Home composable without passing state,
                    // as Home now manages its own state internally.
                    Home()
                }
            }
        }
    }
}

// --- 3. Parent Composable: Home (State Holder) ---
@Composable
fun Home() {
    // Here, we create a mutable state list of Student
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    // Here, we create a mutable state of Student for the input field
    var inputField = remember { mutableStateOf(Student("")) }

    // We call the HomeContent composable, passing state down and events up
    HomeContent(
        listData = listData,
        inputField = inputField.value,

        // Lambda to update the input field value
        onInputValueChange = { input ->
            // Update the state using .copy() to trigger recomposition
            inputField.value = inputField.value.copy(name = input)
        },

        // Lambda to handle button click (add item and reset input)
        onButtonClick = {
            if (inputField.value.name.isNotBlank()) {
                // Add the new item
                listData.add(inputField.value.copy()) // Use copy to ensure a new object is added
                // Reset the input field state
                inputField.value = Student("")
            }
        }
    )
}

// --- 4. Child Composable: HomeContent (UI Renderer) ---
// HomeContent is used to display the content of the Home composable
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    // Here, we use LazyColumn to display a list of items lazily
    LazyColumn {
        // Here, we use item to display the input section inside the LazyColumn
        item {
            Column(
                // Modifier.padding(16.dp) is used to add padding to the Column
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                // Alignment.CenterHorizontally is used to align the Column horizontally
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = stringResource(
                    id = R.string.enter_item)
                )
                // Here, we use TextField to display a text input field
                TextField(
                    // Set the value of the input field from the state
                    value = inputField.name,
                    // Set the keyboard type of the input field
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    // Set what happens when the value of the input field changes
                    onValueChange = {
                        // Here, we call the onInputValueChange lambda function
                        onInputValueChange(it)
                    }
                )
                // Here, we use Button to display a button
                Button(onClick = {
                    // Here, we call the onButtonClick lambda function
                    onButtonClick()
                }) {
                    // Set the text of the button
                    Text(text = stringResource(
                        id = R.string.button_click)
                    )
                }
            }
        }

        // Here, we use items to display a list of items inside the LazyColumn
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display the 'name' property of the Student data class
                Text(text = item.name)
            }
        }
    }
}

// --- 5. Preview Composable ---
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        // We call HomeContent directly for a simpler preview
        HomeContent(
            listData = mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono")),
            inputField = Student(""),
            onInputValueChange = {},
            onButtonClick = {}
        )
    }
}