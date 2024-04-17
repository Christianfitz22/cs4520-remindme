package com.cs4520.remindme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.cs4520.assignment5.R

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ReminderViewModel
    private lateinit var viewModelFactory: ReminderViewModelFactory

    private lateinit var selectedReminder: Reminder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ReminderDatabase.setContext(applicationContext)

        viewModelFactory = ReminderViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[ReminderViewModel::class.java]

        viewModel.reflectDatabase()
        viewModel.generateAdvice()

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
                HomeScreen(onNavigateToCreate = { navController.navigate("create") },
                    onNavigateToList = {
                        viewModel.reflectDatabase()
                        viewModel.generateAdvice()
                        navController.navigate("list")
                    })
            }
            composable("create") {
                Create()
            }
            composable("list"){
                List(onNavigateToDetail = {
                    navController.navigate("detail")
                })
            }
            composable("detail") {
                Detail(onNavigateToList = {navController.navigateUp()})
            }

        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun HomeScreen(onNavigateToCreate: () -> Unit, onNavigateToList: () -> Unit){
        Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){


            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .width(375.dp)
                    .background(color = Color(0xFFADD8E6),
                        shape = RoundedCornerShape(16.dp))
            ) {
                Text(
                text = "RemindMe",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF483D8B),
                modifier = Modifier.padding(vertical = 32.dp).testTag("Title")
            )}
            Spacer(modifier = Modifier.height(10.dp))

            val context = LocalContext.current
            Button(onClick = {
                onNavigateToCreate()}, modifier = Modifier.testTag("Create Button")) {
                Text("Create Reminder")
            }
            Button(onClick = {
                    onNavigateToList()}, modifier = Modifier.testTag("List Button")) {
                Text("View Reminders")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
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

            Box(contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .width(375.dp)
                    .background(color = Color(0xFFADD8E6),
                            shape = RoundedCornerShape(16.dp))
            ) {Text(
                text = "Create a new reminder",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF483D8B),
                modifier = Modifier.padding(vertical = 32.dp).testTag("Title")
            )}

            TextField(
                value = nameText,
                modifier = Modifier.width(300.dp).testTag("Name"),
                onValueChange = { nameText = it },
                label = { Text("Name")})

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.height(IntrinsicSize.Min).width(IntrinsicSize.Max)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    modifier = Modifier.testTag("Category"),
                    onExpandedChange = { expanded = !expanded}
                )
                {
                    TextField(
                        value = selectedCategory,
                        modifier = Modifier.width(244.dp).testTag("Category Chosen"),
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

                GlideImage(
                    model = CategoryConverter.ToURL(Category.valueOf(selectedCategory.uppercase())),
                    modifier = Modifier.size(56.dp)
                        .padding(start = 8.dp, end = 8.dp).testTag("Image"),
                    contentDescription = "Reminder Category Symbol",
                    failure = placeholder(CategoryConverter.ToPlaceholder(Category.valueOf(selectedCategory.uppercase())))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = descText,
                modifier = Modifier.width(375.dp).height(400.dp).testTag("Description"),
                onValueChange = { descText = it },
                label = { Text("Description")})

            Button(onClick = {
                if (createClicked(nameText, descText, selectedCategory)) {
                    nameText = ""
                    descText = ""
                    selectedCategory = categories[0]
                }
            }, modifier = Modifier.testTag("Create Button")) {
                Text("Create")
            }
        }
    }

    private fun createClicked(nameText: String, descText: String, selectedCategory: String): Boolean {
        if (nameText == "") {
            Toast.makeText(applicationContext, "No name provided.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (descText == "") {
            Toast.makeText(applicationContext, "No description provided.", Toast.LENGTH_SHORT).show()
            return false
        }
        viewModel.addReminder(Reminder(nameText, Category.valueOf(selectedCategory.uppercase()), descText))
        Toast.makeText(applicationContext, "New reminder created.", Toast.LENGTH_SHORT).show()
        return true
    }

    //list of all reminders
    @Composable
    fun List(onNavigateToDetail: () -> Unit){
        val reminders by viewModel.ResponseData.observeAsState(listOf())
        val advice by viewModel.AdviceData.observeAsState()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = advice.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().height(40.dp).wrapContentHeight(Alignment.CenterVertically).testTag("Quote"))

            DrawBox(reminders, onNavigateToDetail)
        }
    }

    @Composable
    fun DrawBox(reminders: List<Reminder>, onNavigateToDetail: () -> Unit){
        Box(modifier = Modifier.fillMaxSize().testTag("Box")) {
            LazyColumn {
                itemsIndexed(reminders) { index, reminder ->
                    Preview(reminder, onNavigateToDetail)
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Preview(reminder: Reminder, onNavigateToDetail: () -> Unit) {
        var backgroundColor = CategoryConverter.ToColor(reminder.category)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(color = backgroundColor)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = CategoryConverter.ToURL(reminder.category),
                modifier = Modifier.size(56.dp).testTag("Image")
                .padding(end = 8.dp),
                failure = placeholder(CategoryConverter.ToPlaceholder(reminder.category)),
                contentDescription = "Reminder Category Symbol")

                Text(
                    text = reminder.name,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp).testTag("Name")
                )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { reminderSelected(reminder, onNavigateToDetail) },
                    modifier = Modifier.padding(end = 8.dp).testTag("Button")) {
                    Text("...")
                }
            }

        }
    }

    fun reminderSelected(reminder: Reminder, onNavigateToDetail: () -> Unit) {
        selectedReminder = reminder
        run { onNavigateToDetail() }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun Detail(onNavigateToList: () -> Unit){
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .border(2.dp, Color(0xFF4B0082), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
            {
                Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically)
                {
                    Text(
                        text = selectedReminder.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("Name"))
                    GlideImage(
                        model = CategoryConverter.ToURL(selectedReminder.category),
                        failure = placeholder(CategoryConverter.ToPlaceholder(selectedReminder.category)),
                        contentDescription = "category image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(50.dp).padding(8.dp).testTag("Image"))
                }
            }
            Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                .border(2.dp, CategoryConverter.ToColor(selectedReminder.category), shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
                .height(200.dp)
                .width(500.dp)
            )
            {
                Text(
                    text = selectedReminder.description,
                    fontSize = 18.sp,
                    modifier = Modifier.height(400.dp).testTag("Description"))
            }

            Button(onClick = { deleteClicked(onNavigateToList) }, modifier = Modifier.testTag("Button")) {
                Text("Delete")
            }
        }
    }

    private fun deleteClicked(onNavigateToList: () -> Unit) {
        viewModel.deleteReminder(selectedReminder)
        run { onNavigateToList() }
    }
}
