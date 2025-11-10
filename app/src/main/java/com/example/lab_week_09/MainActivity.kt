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
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme // Ganti dengan path ke theme Anda
import com.example.lab_week_09.ui.theme.OnBackgroundItemText // Import Custom UI Elements
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
// import com.example.lab_week_09.R // Pastikan ini di-import jika R.string error

// 1. Data Model
data class Student(
    var name: String
)

// 2. MainActivity Class
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call the Home composable (State Holder)
                    Home()
                }
            }
        }
    }
}

// 3. Parent Composable: Home (State Holder)
@Composable
fun Home() {
    // State list for the items
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    // State for the input field value
    var inputField = remember { mutableStateOf(Student("")) }

    // Pass data down and event handlers up
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
        }
    )
}

// 4. Child Composable: HomeContent (UI Renderer)
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
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
                // Use Custom UI Element for Title Text
                OnBackgroundTitleText(text = stringResource(
                    id = R.string.enter_item)
                )

                // Input Field
                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        onInputValueChange(it)
                    }
                )

                // Use Custom UI Element for Button
                PrimaryTextButton(
                    text = stringResource(
                        id = R.string.button_click)
                ) {
                    onButtonClick()
                }
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
                // Use Custom UI Element for Item Text
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// 5. Preview Composable
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        HomeContent(
            listData = mutableStateListOf(Student("Tanu"), Student("Tina"), Student("Tono")),
            inputField = Student(""),
            onInputValueChange = {},
            onButtonClick = {}
        )
    }
}