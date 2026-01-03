package dev.faridg.ansibling.ui_kit.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ansibling.composeapp.generated.resources.Res
import ansibling.composeapp.generated.resources.ic_device
import ansibling.composeapp.generated.resources.ic_edit
import ansibling.composeapp.generated.resources.ic_trash
import dev.faridg.ansibling.domain.Device
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun DeviceItem(
    item: Device,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---- Top: device identity ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(Res.drawable.ic_device),
                    contentDescription = null
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.nickName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = item.ip,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ---- Divider ----
            HorizontalDivider(
                color = Color.Gray
            )

            // ---- Bottom: actions (infra vibe) ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // looks like a terminal label
                Text(
                    modifier = Modifier.weight(1f),
                    text = item.username,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Row {
                    IconButton(onClick = onEdit) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(Res.drawable.ic_edit),
                            contentDescription = "Edit device"
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(Res.drawable.ic_trash),
                            contentDescription = "Delete device"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DeviceItem(
    item: Device,
    onIconClick: () -> Unit,
    iconRes: DrawableResource = Res.drawable.ic_trash
) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ---- Top: device identity ----
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(Res.drawable.ic_device),
                    contentDescription = null
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.nickName,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = item.ip,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onIconClick) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(iconRes),
                        contentDescription = "Delete device"
                    )
                }
            }
        }
    }
}