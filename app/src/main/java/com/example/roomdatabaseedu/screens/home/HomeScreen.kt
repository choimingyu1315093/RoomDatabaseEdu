package com.example.roomdatabaseedu.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roomdatabaseedu.db.Diary
import com.example.roomdatabaseedu.db.DiaryDAO
import com.example.roomdatabaseedu.navigation.DiaryScreens
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, dao: DiaryDAO, modifier: Modifier = Modifier){
    DiaryApp(navController, dao)
}

@Composable
fun DiaryApp(navController: NavController, diaryDAO: DiaryDAO) {
    val allDiaries = remember { mutableStateListOf<Diary>() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            allDiaries.clear()
            allDiaries.addAll(diaryDAO.getAllDiary())
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Diary")
            }
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            DiaryList(navController, allDiaries)
        }

        if (showDialog) {
            AddDiaryDialog(
                onDismiss = {
                    showDialog = false
                },
                onSave = { title, content ->
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date()
                    )
                    val newDiary = Diary(
                        date = currentDate,
                        title = title,
                        content = content
                    )
                    scope.launch {
                        diaryDAO.saveDiary(newDiary)
                        allDiaries.clear()
                        allDiaries.addAll(diaryDAO.getAllDiary())
                    }
                    showDialog = false
                })
        }
    }
}

@Composable
fun AddDiaryDialog(onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Diary") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(title, content) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DiaryList(navController: NavController, diaries: List<Diary>) {
    LazyColumn {
        items(diaries) { diary ->
            DiaryItem(diary, onClick = {
                navController.navigate(DiaryScreens.DETAILSCREEN.name+"/${diary.idx}")
            })
        }
    }
}

@Composable
fun DiaryItem(diary: Diary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = diary.date, style = MaterialTheme.typography.bodyMedium)
            Text(text = diary.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = diary.content, style = MaterialTheme.typography.bodySmall)
        }
    }
}