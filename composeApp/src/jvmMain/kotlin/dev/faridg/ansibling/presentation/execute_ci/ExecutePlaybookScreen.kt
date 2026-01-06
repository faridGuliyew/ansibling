package dev.faridg.ansibling.presentation.execute_ci

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.faridg.ansibling.data.room.entity.execution.ExecutionOutputEntity
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.DeviceGroup
import dev.faridg.ansibling.domain.TaskStatusType
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.domain.StatusType
import dev.faridg.ansibling.ui_kit.ExpandableCard

@Composable
fun ExecutePlaybookScreen(
    playbook: Playbook
) {
    val vm = remember { ExecutePlaybookViewModel(playbook) }

    val execution by vm.execution.collectAsStateWithLifecycle()
    val outputs = vm.outputs //.collectAsStateWithLifecycle()
    val progress by vm.progress.collectAsState(initial = 0f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ---- Header Card ----
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = playbook.nickName,
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatusChip(execution?.status)
                    Text(
                        text = "${playbook.actions.size} action(s)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

//                LinearProgressIndicator(
//                    progress = progress,
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }

        // ---- Output Header ----
        Text(
            text = "Execution Output",
            style = MaterialTheme.typography.titleMedium
        )

        // ---- Terminal ----
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0E0E0E), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                outputs.forEach { it ->
                    GroupOutputCard(
                        group = it.key,
                        deviceOutputMap = it.value
                    )
                }
            }
        }
    }
}

@Composable
fun GroupOutputCard(
    group: DeviceGroup?,
    deviceOutputMap: Map<Device?, List<ExecutionOutputEntity>>
) {
    val groupName = group?.nickName ?: "Default"

    ExpandableCard(
        title = {
            Text(
                text = groupName,
                style = MaterialTheme.typography.titleMedium
            )
        }
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            deviceOutputMap.forEach { (device, outputs) ->
                DeviceOutputCard(
                    device = device,
                    outputs = outputs
                )
            }
        }
    }
}

@Composable
private fun DeviceOutputCard(
    device: Device?,
    outputs: List<ExecutionOutputEntity>
) {
    val deviceName = device?.nickName ?: "-ERROR-"

    ExpandableCard(
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        title = {
            Text(
                text = deviceName,
                style = MaterialTheme.typography.titleSmall
            )
        }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 260.dp) // important: prevents infinite growth
        ) {
            items(outputs) { output ->
                OutputRow(output)
            }
        }
    }

//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(10.dp),
//        border = BorderStroke(
//            1.dp,
//            MaterialTheme.colorScheme.outlineVariant
//        )
//    ) {
//        Column(
//            modifier = Modifier.padding(10.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//            // ---- Device title ----
//            Text(
//                text = deviceName,
//                style = MaterialTheme.typography.titleSmall
//            )
//
//            // ---- Output ----
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(max = 260.dp) // important: prevents infinite growth
//            ) {
//                items(outputs) { output ->
//                    OutputRow(output)
//                }
//            }
//        }
//    }
}


@Composable
fun OutputRow(output: ExecutionOutputEntity) {
    val color = when (output.type) {
        StatusType.SUCCESS -> Color(0xFF81C784)
        StatusType.ERROR -> Color(0xFFE57373)
        StatusType.WARNING -> Color(0xFFFFB74D)
        StatusType.INFO -> Color(0xFF64B5F6)
    }

    Text(
        text = output.output,
        color = color,
        fontFamily = FontFamily.Monospace,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}


@Composable
fun StatusChip(status: TaskStatusType?) {
    val (text, color) = when (status) {
        TaskStatusType.SUCCESS -> "DONE" to Color(0xFF2E7D32)
        TaskStatusType.ERROR -> "FAILED" to Color(0xFFC62828)
        TaskStatusType.IN_PROGRESS -> "RUNNING" to Color(0xFF0277BD)
        null -> "PENDING" to Color.Gray
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
