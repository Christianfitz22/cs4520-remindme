package com.cs4520.remindme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.work.WorkManager
import com.cs4520.assignment5.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNavHost()
                }
            }
        }
    }

    @Composable
    fun MyNavHost() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(onNavigateToCreate = { navController.navigate("create") }, onNavigateToList = {navController.navigate("list")})
            }
            composable("create") {
                Create()
            }
            composable("list"){
                List(onNavigateToDetail = { navController.navigate("detail") })
            }
            composable("detail"){
                Detail()
            }
        }
    }

    @Composable
    fun Home(onNavigateToCreate: () -> Unit, onNavigateToList: () -> Unit){
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            val context = LocalContext.current
            Text("RemindMe")
            Button(onClick = {
                onNavigateToCreate()}) {
                Text("Create Reminder")
            }
            Button(onClick = {
                    onNavigateToList()}) {
                Text("View Reminders")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun Create(){
        var nameText by remember { mutableStateOf("")}
        var descText by remember { mutableStateOf("")}
        val categories = arrayOf("Home", "Work", "Family", "Personal")
        var expanded by remember { mutableStateOf(false)}
        var selectedCategory by remember { mutableStateOf(categories[0])}

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            TextField(
                value = nameText,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { nameText = it },
                label = { Text("Name")})

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded})
            {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)}
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false})
                {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            content = { -> Text(text = item)},
                            onClick = {
                                selectedCategory = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            TextField(
                value = descText,
                modifier = Modifier.fillMaxWidth().height(400.dp),
                onValueChange = { descText = it },
                label = { Text("Description")})

            Button(onClick = {}) {
                Text("Create")
            }
        }
    }

    @Composable
    fun List(onNavigateToDetail: () -> Unit){
        val sampleReminder1 = ReminderEntry("test1", ReminderCategory.Home, "test1desc")
        val sampleReminder2 = ReminderEntry("test2", ReminderCategory.Family, "test2desc")
        val sampleReminder3 = ReminderEntry("test3", ReminderCategory.Personal, "test3desc")
        val sampleData = arrayOf(sampleReminder1, sampleReminder2, sampleReminder3)

        LazyColumn {
            items(items = sampleData) { data -> ReminderEntryParse(data)}
        }

        /*
        Column() {
            Text("Placeholder2")
            Button(onClick = {
                onNavigateToDetail()}) {
                Text("Get Details")
            }
        }
         */
    }

    @Composable
    fun ReminderEntryParse(reminderEntry: ReminderEntry) {
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(CategoryToImage(reminderEntry.category)),
                contentDescription = "category image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(60.dp)
            )
            Text(
                text = reminderEntry.name,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.8f)
            )
            Button(
                onClick = {},
                modifier = Modifier.width(50.dp).height(50.dp)) {
                Text("...")
            }
        }
    }

    fun CategoryToImage(category: ReminderCategory): Int {
        if (category == ReminderCategory.Home) {
            return R.drawable.home_120
        }
        else if (category == ReminderCategory.Work) {
            return R.drawable.custom_apps_120
        }
        else if (category == ReminderCategory.Family) {
            return R.drawable.groups_120
        }
        else if (category == ReminderCategory.Personal) {
            return R.drawable.user_120
        }
        return 0
    }

    @Composable
    fun Detail() {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically)
            {
                Text(
                    text = "Name",
                    modifier = Modifier.width(140.dp).height(IntrinsicSize.Min),
                    textAlign = TextAlign.Center)
                Image(
                    painter = painterResource(R.drawable.equipment),
                    contentDescription = "category image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp).padding(0.dp)
                )
            }
            Text(
                text = "Description content\nDescription content\nDescription content\nDescription content\nDescription content\nDescription content\n",
                modifier = Modifier.height(400.dp))
            Button(onClick = {}) {
                Text("Delete")
            }
        }
    }

}