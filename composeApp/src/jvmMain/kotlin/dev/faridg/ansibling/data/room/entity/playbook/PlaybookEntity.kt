package dev.faridg.ansibling.data.room.entity.playbook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "playbooks",
)
data class PlaybookEntity (
    @PrimaryKey
    val playbookId: String,
    val nickName: String
)