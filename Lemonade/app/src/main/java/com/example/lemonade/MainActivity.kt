package com.example.lemonade

import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LemonadeView()
                }
            }
        }
    }
}

data class StepValue(var id: Int, var message: String)

interface Stepper {
    fun nextStep(): StepType
}

enum class StepType(val stepValue: StepValue): Stepper {
    TREE(StepValue(id = R.drawable.lemon_tree, message = "Tap the lemon tree to select a lemon")) {
        override fun nextStep() = SQUEEZE
    },
    SQUEEZE(StepValue(id = R.drawable.lemon_squeeze, message = "Keep tapping the lemon to squeeze it")){
        override fun nextStep() = DRINK
    },
    DRINK(StepValue(id = R.drawable.lemon_drink, message = "Tap the lemonade to drink it")){
        override fun nextStep() = RESTART
    },
    RESTART(StepValue(id = R.drawable.lemon_restart, message = "Tap the empty glass to start again")){
        override fun nextStep() = TREE
    }
}

@Composable
fun LemonadeView(modifier: Modifier = Modifier) {
    var position by remember { mutableStateOf(StepType.TREE) }
    var tapCount by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (position == StepType.TREE) {
                    tapCount = (2..4).random()
                    position = position.nextStep()
                } else {
                    tapCount = if (tapCount > 0) tapCount-1 else 0
                    if (tapCount == 0) {
                        position = position.nextStep()
                    }
                }
            },
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Image(
                painter = painterResource(id = position.stepValue.id),
                contentDescription = null,
                modifier = Modifier
                    .width(dimensionResource(id = R.dimen.button_image_width))
                    .height(dimensionResource(id = R.dimen.button_image_height))
                    .padding(dimensionResource(id = R.dimen.button_interior_padding)),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = position.stepValue.message,
            fontSize = 18.sp
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    LemonadeTheme {
//        LemonadeView()
//    }
//}