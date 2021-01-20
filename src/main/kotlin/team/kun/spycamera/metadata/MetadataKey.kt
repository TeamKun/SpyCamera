package team.kun.spycamera.metadata

import org.bukkit.entity.Entity
import org.bukkit.entity.Player

sealed class MetadataKey<T>(val value: String) {
    object IsPlayerInteract : MetadataKey<Boolean>("IsPlayerInteract")

    object Camera : MetadataKey<Entity>("Camera")
    object CameraPlayers : MetadataKey<List<Player>>("CameraPlayers")
    object UsingCamera : MetadataKey<Boolean>("UsingCamera")
}