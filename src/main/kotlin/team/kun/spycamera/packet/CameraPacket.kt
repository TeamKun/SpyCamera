package team.kun.spycamera.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class CameraPacket(private val entity: Entity) : PacketClient() {
    override fun send(player: Player) {
        val packetContainer = getPacketContainer()
        sendPacket(player, packetContainer)
    }

    private fun getPacketContainer(): PacketContainer {
        val packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA)
        packet.integers.write(0, entity.entityId)
        return packet
    }
}