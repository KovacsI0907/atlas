package kovacsi0907.atlas.Network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public abstract class ClientNetworkFunctions {
    public static void syncSkills() {
        ClientPlayNetworking.send(Channels.PLAYER_SKILLS_PACKET, PacketByteBufs.empty());
    }

    public static void syncXP(){
        ClientPlayNetworking.send(Channels.PLAYER_XP_PACKET, PacketByteBufs.empty());
    }
}
