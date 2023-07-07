package com.example.marcatruko

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marcatruko.ui.theme.MarcaTrukoTheme

val lobster = FontFamily(
    listOf(Font(R.font.lobster_regular))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarcaTrukoTheme {
                App()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun App() {
    var teamOnePoints by remember { mutableStateOf(0) }
    var teamTwoPoints by remember { mutableStateOf(0) }
    var teamOneWins by remember { mutableStateOf(0) }
    var teamTwoWins by remember { mutableStateOf(0) }
    var trucoValue by remember { mutableStateOf(1) }

    fun resetPoints() {
        teamOnePoints = 0
        teamTwoPoints = 0
        trucoValue = 1
    }

    fun resetAll() {
        resetPoints()
        teamOneWins = 0
        teamTwoWins = 0
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopBar(reset = { resetAll() })
        }, content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .consumedWindowInsets(it)
                    .padding(it),
                Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Team(teamName = "Nós", points = teamOnePoints, wins = teamOneWins, onPointsUp = {
                    teamOnePoints += trucoValue
                    if (teamOnePoints >= 12) {
                        teamOneWins++
                        resetPoints()
                    }
                    trucoValue = 1
                }, onPointsDown = {
                    teamOnePoints -= 1
                    if (teamOnePoints == -1) {
                        teamOneWins--
                        resetPoints()
                    }

                })
                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .width(1.dp)
                )
                Team(teamName = "Eles", points = teamTwoPoints, wins = teamTwoWins, onPointsUp = {
                    teamTwoPoints += trucoValue
                    if (teamTwoPoints >= 12) {
                        teamTwoWins++
                        resetPoints()
                    }
                    trucoValue = 1
                }, onPointsDown = {
                    teamTwoPoints -= 1
                    if (teamTwoPoints == -1) {
                        teamTwoWins--
                        resetPoints()
                    }
                })
            }
        }, bottomBar = {
            BottomBar(trucoValue = trucoValue, onTrucoValueChange = { trucoValue = it })
        })

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    reset: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    InfoDialog(openDialog = openDialog, onOpenDialog = { openDialog = it })

    CenterAlignedTopAppBar(modifier = Modifier.padding(16.dp), title = {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Marca Truko",
            style = MaterialTheme.typography.displaySmall,
            fontFamily = lobster,
            color = Color.hsl(0F, 1F, 0.63F)
        )
    }, navigationIcon = {
        IconButton(modifier = Modifier.size(56.dp), content = {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Outlined.Info,
                contentDescription = ""
            )
        }, onClick = {
            openDialog = true
        })
    }, actions = {
        IconButton(modifier = Modifier.size(56.dp), content = {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                modifier = Modifier.size(36.dp),
                contentDescription = "",
            )
        }, onClick = { reset() })
    })
}

@Composable
private fun Team(
    teamName: String, points: Int, wins: Int, onPointsUp: () -> Unit, onPointsDown: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(144.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = teamName, style = MaterialTheme.typography.displayLarge)
            Text(
                text = wins.toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(text = points.toString(), fontSize = 96.sp)
        Column(modifier = Modifier.height(280.dp), verticalArrangement = Arrangement.SpaceEvenly) {
            ActionButton(icon = Icons.Filled.KeyboardArrowUp) {
                onPointsUp()
            }
            ActionButton(icon = Icons.Filled.KeyboardArrowDown) {
                onPointsDown()
            }
        }

    }
}

@Composable
private fun BottomBar(
    trucoValue: Int, onTrucoValueChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(modifier = Modifier
            .fillMaxWidth(0.8f)
            .size(80.dp), onClick = {
            when (trucoValue) {
                1 -> {
                    onTrucoValueChange(3)
                }

                3 -> {
                    onTrucoValueChange(6)
                }

                6 -> {
                    onTrucoValueChange(9)
                }

                9 -> {
                    onTrucoValueChange(12)
                }

                12 -> {
                    onTrucoValueChange(1)
                }
            }
        }) {
            if (trucoValue == 1) {
                Text(
                    text = "Truco!", style = MaterialTheme.typography.displaySmall
                )
            } else Text(
                text = trucoValue.toString(), style = MaterialTheme.typography.displaySmall
            )
        }

    }
}

@Composable
private fun InfoDialog(openDialog: Boolean, onOpenDialog: (Boolean) -> Unit) {
    @Composable
    fun dialogItem(icon: ImageVector, description: String) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp), imageVector = icon, contentDescription = ""
            )
            Text(text = description, style = MaterialTheme.typography.labelLarge)
        }

    }

    if (openDialog) {
        AlertDialog(

            onDismissRequest = {
                onOpenDialog(false)
            },
            title = {
                Text(
                    text = "Marca Truko",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Open source truco scorekeeper app",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    dialogItem(icon = Icons.Outlined.Info, description = "0.1")
                    Divider(modifier = Modifier.fillMaxWidth())
                    dialogItem(icon = Icons.Outlined.Place, description = "Source Code")
                    Divider(modifier = Modifier.fillMaxWidth())
                    dialogItem(Icons.Outlined.Build, "GPL 3.0")
                    Divider(modifier = Modifier.fillMaxWidth())
                    dialogItem(Icons.Outlined.Face, "Developed by João Reiter")

                }

            },
            confirmButton = {
                TextButton(onClick = {
                    onOpenDialog(false)
                }) {
                    Text("Close")
                }
            },
        )


    }
}

@Composable
private fun ActionButton(
    icon: ImageVector, block: () -> Unit
) {
    FilledTonalIconButton(
        onClick = block, modifier = Modifier
            .height(96.dp)
            .width(96.dp)
    ) {
        Icon(imageVector = icon, contentDescription = "", modifier = Modifier.size(56.dp))
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
fun AppPreview() {
    MarcaTrukoTheme { App() }
}