package dev.faridg.ansibling.ui_kit.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_add
import org.jetbrains.compose.resources.painterResource

@Composable
fun TabScreen(
    title: String,
    onAdd: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(title, style = MaterialTheme.typography.titleMedium)

        Box (modifier = Modifier.weight(1F)) {
            content()
        }


        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAdd
        ) {
            Image(painter = painterResource(Res.drawable.ic_add), contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Add")
        }
    }
}