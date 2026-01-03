package dev.faridg.ansibling.presentation.edit_device

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.faridg.ansibling.data.room.AppDatabase
import dev.faridg.ansibling.data.room.toEntity
import dev.faridg.ansibling.domain.Device
import kotlinx.coroutines.launch

@Composable
fun EditDeviceScreen(
    device: Device,
    onExit: () -> Unit
) {
    val uiModel = remember { device.toUiModel() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiModel.nickName.value,
            onValueChange = { uiModel.nickName.value = it },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = uiModel.username.value,
            onValueChange = { uiModel.username.value = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiModel.password.value,
            onValueChange = { uiModel.password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiModel.ip.value,
            onValueChange = { uiModel.ip.value = it },
            label = { Text("IP Address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiModel.port.value.toString(),
            onValueChange = { uiModel.port.value = it.toIntOrNull() ?: return@OutlinedTextField },
            label = { Text("Port") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    AppDatabase.deviceDao.insertDevice(uiModel.toDomain(device.id).toEntity())
                    onExit()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}