package com.lamarques.testecompose.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lamarques.testecompose.R
import com.lamarques.testecompose.chat.models.ChatMessage
import com.lamarques.testecompose.presentation.MainViewModel
import com.lamarques.testecompose.ui.theme.Shapes
import com.lamarques.testecompose.ui.theme.TesteComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TesteComposeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val vmMain: MainViewModel = viewModel()

        Scaffold(
            topBar = {
                AppTopBar()
            },
            bottomBar = {
                AppBottomBar()
            }

        ) {
            ChatContent(vmMain)
        }
    }
}

@Composable
fun AppTopBar() {
    Column(
        Modifier
            .background(Color.Green)
            .padding(Dp(8f))
            .clip(RoundedCornerShape(bottomStart = Dp(16f), bottomEnd = Dp(16f)))
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar")
            Image(
                painter = painterResource(id = R.drawable.person_1),
                contentDescription = null,
                Modifier
                    .size(
                        Dp(36f)
                    )
                    .padding(start = Dp(8f))
                    .clip(CircleShape)
            )
            Text(text = "Camille", Modifier.padding(start = Dp(8f)))
        }
    }
}

@Composable
fun AppBottomBar() {
    Row(Modifier.fillMaxWidth()) {
        val vmMain: MainViewModel = viewModel()
        var message by remember {
            mutableStateOf("")
        }

        TextField(
            value = message,
            onValueChange = { message = it },
            Modifier
                .padding(Dp(4f))
                .clip(Shapes.large)
                .background(Color.Green)
                .fillMaxWidth(0.9f),
            maxLines = 1,
            placeholder = { Text("Insira sua mensagem...") }
        )

        Icon(
            Icons.Rounded.Send,
            contentDescription = "Enviar",
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .clickable {
                    vmMain.sendMessage(message)
                    message = ""
                }
        )
    }
}

@Composable
fun ChatContent(vmMain: MainViewModel) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        val listMessages = vmMain.handleListMessage().observeAsState()

        LazyColumn(content = {
            listMessages.value?.let { list ->
                items(list.size) {
                    val chatMessage = list[it]
                    if (chatMessage.origin == 0) {
                        MessageContainer(
                            chatMessage = chatMessage,
                            alignment = Alignment.CenterEnd
                        )
                    } else {
                        MessageContainer(
                            chatMessage = chatMessage,
                            alignment = Alignment.CenterStart
                        )
                    }
                }
            }
        })

    }
}

@Composable
fun MessageContainer(chatMessage: ChatMessage, alignment: Alignment) {
    Box(
        Modifier
            .padding(Dp(8f))
            .fillMaxWidth(), contentAlignment = alignment
    ) {
        ChatBubble(chatMessage = chatMessage, Color.LightGray)
    }
}

@Composable
fun ChatBubble(chatMessage: ChatMessage, color: Color) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(color)
            .defaultMinSize(Dp(80f))
            .fillMaxWidth(0.7f)
            .padding(Dp(8f))
    ) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = chatMessage.message,
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
            Text(
                text = chatMessage.time,
                Modifier.align(Alignment.End),
                style = TextStyle(
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TesteComposeTheme {
        val chatMessage = ChatMessage("08:51", "Teste", 0)
        MessageContainer(chatMessage = chatMessage, Alignment.CenterEnd)
    }
}