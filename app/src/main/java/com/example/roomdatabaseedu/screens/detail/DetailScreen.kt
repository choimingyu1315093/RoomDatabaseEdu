package com.example.roomdatabaseedu.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
fun DetailScreen(
    navController: NavController,
    diaryIdx: Int?,
    dao: DiaryDAO,
    modifier: Modifier = Modifier
) {
    var diary by remember { mutableStateOf<Diary?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (diaryIdx != null) {
            diary = dao.getDiary(diaryIdx)
            diary?.let {
                title = it.title
                content = it.content
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    isEditing = !isEditing
                    if(!isEditing) {
                        val currentData =
                            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            )
                        val updateDiary = Diary(
                            idx = diaryIdx!!,
                            date = currentData,
                            title = title,
                            content = content
                        )
                        scope.launch {
                            dao.updateDiary(updateDiary)
                            navController.navigate(DiaryScreens.HOMESCREEN.name)
                        }
                    }
                }
            ) {
                Text(
                    text = if (isEditing) "저장" else "수정",
                    color = if (isEditing) Color.Blue else Color.Green
                )
            }
            TextButton(
                onClick = {
                    scope.launch {
                        dao.oneDeleteDiary(diaryIdx!!)
                        navController.navigate(DiaryScreens.HOMESCREEN.name)
                    }
                }
            ) {
                Text(
                    text = "삭제",
                    color = Color.Red
                )
            }
        }

        if (diary != null) {
            Text(
                text = "날짜: ${diary!!.date}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "제목") },
                enabled = isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(text = "내용") },
                enabled = isEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else {
            Text(
                text = "Loading...",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}