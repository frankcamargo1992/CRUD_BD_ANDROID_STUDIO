package com.example.myapp_bd

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.ui.res.painterResource
import com.example.myapp_bd.R
import androidx.core.splashscreen.SplashScreen

private lateinit var dbHelper: DatabasOpenHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dbHelper = DatabasOpenHelper(this)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            if (showSplash) {
                SplashScreen {
                    showSplash = false
                }
            } else {
                AddUser()
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUser() {

    var editingUserId by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }


    var gender by remember { mutableStateOf("") }
    val genderOptions = listOf("Male", "Female", "Other")
    var expended by remember { mutableStateOf(false) }


    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var users by remember { mutableStateOf(dbHelper.getAllUsers()) }

    Column (modifier = Modifier.padding(50.dp)){
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") }
        )
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") }
        )
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.width(280.dp),
            trailingIcon = {
                IconButton(onClick = { expended = !expended }){
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expended,
                    onDismissRequest = { expended = false }
                ) {
                    genderOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                gender = selectionOption
                                expended = false
                            }
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Button(onClick = {
            if(editingUserId == null){
                if(dbHelper.insertUser(name, lastName, age.toIntOrNull() ?: 0, gender, phone, email)) {
                    Toast.makeText(context, "Usuario agregado exitosamente", Toast.LENGTH_LONG).show()
                    users = dbHelper.getAllUsers()
                }else{
                    Toast.makeText(context, "Error al agregar el usuario", Toast.LENGTH_LONG).show()
                }
            }
            else{
                if(dbHelper.updateUser(editingUserId!!, name, lastName, age.toIntOrNull() ?: 0, gender, phone, email)){
                    Toast.makeText(context, "Usuario actualizado exitosamente", Toast.LENGTH_LONG).show()
                    users = dbHelper.getAllUsers()
                    editingUserId = null
                }
                else{
                    Toast.makeText(context, "Error al actualizar el usuario", Toast.LENGTH_LONG).show()
                }
            }
        }){
            Text(if(editingUserId == null) "Agregar Usuario" else "Actualizar Usuario")
        }

        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(users) { user ->
                UsersRow(user,
                    onDelete = {
                        //Accion de eliminar
                        if(dbHelper.deleteUser(user["id"] as Int)){
                            users = dbHelper.getAllUsers()
                            Toast.makeText(context, "Usuario eliminado exitosamente", Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_LONG).show()
                        }

                    },
                    onEdit = {
                        //Accion de editar
                        editingUserId = user["id"] as Int
                        name = user["name"] as String
                        lastName = user["last_name"] as String
                        age = user["age"].toString()
                        gender = user["gender"] as String
                        phone = user["phone"] as String
                        email = user["email"] as String
                    }
                )
            }
        }

    }
}
@Composable
fun UsersRow(user: Map<String, Any>, onDelete: () -> Unit, onEdit: () -> Unit   ) {
    Column(modifier = Modifier.padding(8.dp).fillMaxSize()) {
        Text(text = "Name: ${user["name"]}")
        Text(text = "Last Name: ${user["last_name"]}")
        Text(text = "Age: ${user["age"]}")
        Text(text = "Gender: ${user["gender"]}")
        Text(text = "Phone: ${user["phone"]}")
        Text(text = "Email: ${user["email"]}")
        Row{
            Button(onClick = onEdit) {
                Text("Editar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDelete) {
                Text("Eliminar")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
