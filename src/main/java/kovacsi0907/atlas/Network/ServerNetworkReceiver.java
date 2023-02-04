package kovacsi0907.atlas.Network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;

public abstract class ServerNetworkReceiver {
    public static void registerListeners() {
        ServerPlayNetworking.registerGlobalReceiver(Channels.PLAYER_SKILLS_PACKET, ((server, player, handler, buf, responseSender) -> {
            ServerNetworkFunctions.sendUnlockedSkillsPacket(player, server);
        }));

        ServerPlayNetworking.registerGlobalReceiver(Channels.PLAYER_XP_PACKET, ((server, player, handler, buf, responseSender) -> {
            ServerNetworkFunctions.sendExperiencePacket(player, server);
        }));

        ServerPlayNetworking.registerGlobalReceiver(Channels.REQUEST_GET_SKILL_PACKET, ((server, player, handler, buf, responseSender) -> {
            ServerNetworkFunctions.checkAndAddNewSkill(player, server, buf.readString());
        }));
    }
}
