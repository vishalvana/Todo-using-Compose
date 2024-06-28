package com.example.myapplication.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Database.TodoEntity
import com.example.myapplication.Database.addDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val todos by viewModel.todos.collectAsState()

    val (dialogOpen, setDialogOpen) = remember {
        mutableStateOf(false)
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredTodos = todos.filter { it.tittle.contains(searchQuery, ignoreCase = true) }

    if (dialogOpen) {
        val (title, setTitle) = remember {
            mutableStateOf("")
        }
        val (subTitle, setSubTitle) = remember {
            mutableStateOf("")
        }

        Dialog(onDismissRequest = { setDialogOpen(false) }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = title, onValueChange = {
                        setTitle(it)
                    }, modifier = Modifier.fillMaxWidth(), label = {
                        Text(text = "Title")
                    }, colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedSupportingTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = subTitle, onValueChange = {
                        setSubTitle(it)
                    }, modifier = Modifier.fillMaxWidth(), label = {
                        Text(text = "SubTitle")
                    }, colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedSupportingTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(onClick = {
                    if (title.isNotEmpty() && subTitle.isNotEmpty()) {
                        viewModel.addTodo(
                            TodoEntity(
                                tittle = title,
                                subTittle = subTitle
                            )
                        )
                        setDialogOpen(false)
                    }

                }, shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = "Submit", color = Color.White)
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondary,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                setDialogOpen(true)
            }, contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search", color = Color.White) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White,
                    textColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    AnimatedVisibility(
                        visible = filteredTodos.isEmpty(),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Text(text = "NO Todos Yet!", color = Color.White, fontSize = 22.sp)
                    }
                    AnimatedVisibility(
                        visible = filteredTodos.isNotEmpty(),
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    bottom = paddings.calculateBottomPadding() + 8.dp,
                                    top = 8.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                ), verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredTodos.sortedWith(compareBy<TodoEntity> { it.done }.thenBy { it.iD }), key = {
                                it.iD
                            }) { todo ->
                                TodoItem(todo = todo, onClick = {
                                    viewModel.updateTodo(
                                        todo.copy(done = !todo.done)
                                    )
                                }, onDelete = {
                                    viewModel.deleteTodo(todo)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.TodoItem(todo: TodoEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    val color by animateColorAsState(
        targetValue = if (todo.done) Color(0xff24d65f) else Color(0xffff6363),
        animationSpec = tween(500)
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .animateItemPlacement(), contentAlignment = Alignment.BottomEnd) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(color)
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp
                ), horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(4.dp), contentAlignment = Alignment.Center
            ) {
                Row {
                    AnimatedVisibility(
                        visible = todo.done,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = color)
                    }
                }
                Row {
                    AnimatedVisibility(
                        visible = !todo.done,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = color)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp) // Add padding to align to the left
            ) {
                Text(
                    text = todo.tittle,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(text = todo.subTittle, color = Color(0xffebebeb))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
                verticalArrangement = Arrangement.Center // Center items vertically within the column
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp), contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Delete, tint = Color.White, contentDescription = null, modifier = Modifier.clickable { onDelete() })
                }
                Spacer(modifier = Modifier.height(8.dp)) // Space between icon and text
                val dateTime = todo.addDate.split(" ")
                Text(
                    text = dateTime[1], // Display time
                    color = Color.White,
                    fontSize = 12.sp
                )
                Text(
                    text = dateTime[0], // Display date
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}
