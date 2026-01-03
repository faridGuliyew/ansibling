package dev.faridg.ansibling.data.room

import dev.faridg.ansibling.data.room.entity.playbook.RemoteActionEntity
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupWithDevicesRelation
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookWithActionsAndDevicesRelation
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity
import dev.faridg.ansibling.data.room.entity.variable.VariableEntity
import dev.faridg.ansibling.domain.RemoteAction
import dev.faridg.ansibling.domain.Device
import dev.faridg.ansibling.domain.DeviceGroup
import dev.faridg.ansibling.domain.Playbook
import dev.faridg.ansibling.domain.Script
import dev.faridg.ansibling.domain.Variable

fun Device.toEntity(): DeviceEntity =
    DeviceEntity(
        deviceId = id,
        nickName = nickName,
        username = username,
        password = password,
        port = port,
        ip = ip
    )


fun RemoteActionEntity.toDomain(): RemoteAction =
    RemoteAction(
        id = actionId,
        playbookId = playbookId,
        command = command,
        commandType = commandType,
        exceptionBehaviour = exceptionBehaviour,
        order = order
    )


fun RemoteAction.toEntity(): RemoteActionEntity =
    RemoteActionEntity(
        playbookId = playbookId,
        actionId = id,
        command = command,
        commandType = commandType,
        exceptionBehaviour = exceptionBehaviour,
        order = order
    )


fun VariableEntity.toDomain() = Variable(
    id = variableId,
    key = key,
    value = value
)

fun Variable.toEntity() = VariableEntity(
    variableId = id,
    key = key,
    value = value
)

fun ScriptEntity.toDomain() = Script(
    id = scriptId,
    name = name,
    content = content
)

fun Script.toEntity() = ScriptEntity(
    scriptId = id,
    name = name,
    content = content
)

fun DeviceEntity.toDomain(): Device = Device(
    id = deviceId,
    nickName = nickName,
    username = username,
    password = password,
    port = port,
    ip = ip
)

fun DeviceGroupWithDevicesRelation.toDomain() = DeviceGroup(
    id = deviceGroup.groupId,
    nickName = deviceGroup.nickName,
    devices = devices.map { it.toDomain() }
)

fun DeviceGroup.toEntity() = DeviceGroupWithDevicesRelation(
    deviceGroup = DeviceGroupEntity(
        groupId = id,
        nickName = nickName
    ),
    devices = devices.map { it.toEntity() }
)

fun PlaybookWithActionsAndDevicesRelation.toDomain() = Playbook(
    id = playbook.playbookId,
    nickName = playbook.nickName,
    actions = actions.map { it.toDomain() },
    devices = devices.map { it.toDomain() } + deviceGroups.map {
        DeviceGroup(
            id = it.groupId,
            nickName = it.nickName,
            devices = emptyList()
        )
    }
)