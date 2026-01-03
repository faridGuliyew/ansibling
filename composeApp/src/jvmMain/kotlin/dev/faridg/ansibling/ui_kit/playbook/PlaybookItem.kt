package dev.faridg.ansibling.ui_kit.playbook

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_book
import dev.faridg.ansibling.WindowRoute
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.windowManager
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlaybookItem(
    playbook: Playbook,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---- Playbook identity ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(Res.drawable.ic_book),
                    contentDescription = null
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playbook.nickName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${playbook.devices.size} device(s) • ${playbook.actions.size} action(s)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // ---- Primary action ----
                Button(
                    onClick = {
                        windowManager.push(
                            WindowRoute.ExecutePlaybook(playbook)
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Run")
                }
            }

            // ---- Divider ----
            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            // ---- Secondary actions ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        windowManager.push(
                            WindowRoute.EditPlaybook(playbook)
                        )
                    }
                ) {
                    Text("Edit")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
