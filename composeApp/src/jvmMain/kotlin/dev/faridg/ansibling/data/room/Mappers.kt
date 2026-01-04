package dev.faridg.ansibling.data.room

import dev.faridg.ansibling.data.room.entity.playbook.entity.PlaybookLocalScriptEntity
import dev.faridg.ansibling.data.room.entity.device.DeviceEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupEntity
import dev.faridg.ansibling.data.room.entity.device_group.DeviceGroupWithDevicesRelation
import dev.faridg.ansibling.data.room.entity.playbook.FullPlaybookRelation
import dev.faridg.ansibling.data.room.entity.playbook.PlaybookGlobalScriptRelation
import dev.faridg.ansibling.data.room.entity.script.ScriptEntity
import dev.faridg.ansibling.data.room.entity.variable.VariableEntity
import dev.faridg.ansibling.domain.PlaybookScript
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


fun PlaybookLocalScriptEntity.toDomain(): PlaybookScript =
    PlaybookScript(
        scriptId = scriptId,
        playbookId = playbookId,
        title = title,
        content = content,
        type = type,
        exceptionBehaviour = exceptionBehaviour,
        order = order,
        isGlobal = false
    )

fun PlaybookGlobalScriptRelation.toPlaybookDomain(
): PlaybookScript =
    PlaybookScript(
        scriptId = relation.scriptId,
        playbookId = relation.playbookId,
        title = script.title,
        content = script.content,
        type = script.type,
        exceptionBehaviour = script.exceptionBehaviour,
        order = relation.order,
        isGlobal = true
    )


fun PlaybookScript.toEntity(): PlaybookLocalScriptEntity =
    PlaybookLocalScriptEntity(
        playbookId = playbookId,
        scriptId = scriptId,
        title = title,
        content = content,
        type = type,
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
    scriptId = scriptId,
    title = title,
    content = content,
    type = type,
    exceptionBehavior = exceptionBehaviour
)

fun Script.toEntity() = ScriptEntity(
    scriptId = scriptId,
    title = title,
    content = content,
    type = type,
    exceptionBehaviour = exceptionBehavior
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

fun FullPlaybookRelation.toDomain() = Playbook(
    id = playbook.playbookId,
    nickName = playbook.nickName,
    actions = (localScripts.map { it.toDomain() } + globalScripts.map { it.toPlaybookDomain() }).sortedBy { it.order },
    devices = devices.map { it.toDomain() } + deviceGroups.map {
        DeviceGroup(
            id = it.groupId,
            nickName = it.nickName,
            devices = emptyList()
        )
    }
)