package dev.faridg.ansibling.ui_kit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_arrow_down
import org.jetbrains.compose.resources.painterResource

object ExpandableCardDefaults {
    val borderStroke = BorderStroke(0.dp, Color.Transparent)
}

@Composable
fun ExpandableCard(
    isExpanded: Boolean = false,
    border: BorderStroke = ExpandableCardDefaults.borderStroke,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    var isExpanded by remember (isExpanded) { mutableStateOf(isExpanded) }
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
    )

    Card (
        border = border
    ) {
        Column (modifier = Modifier.padding(16.dp)) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(modifier = Modifier.weight(1f)) { title() }
                Image(
                    modifier = Modifier.size(24.dp)
                        .clickable(onClick = { isExpanded = !isExpanded })
                        .graphicsLayer {
                            rotationZ = rotation
                        },
                    painter = painterResource(Res.drawable.ic_arrow_down),
                    contentDescription = null
                )
            }
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}