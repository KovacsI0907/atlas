package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.Skills.ExpType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public abstract class NetworkReciever {
    public static void registerListeners() {
        ServerPlayNetworking.registerGlobalReceiver(Channels.PLAYER_SKILLS_PACKET, ((server, player, handler, buf, responseSender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeString("bronze_weapon_smithing");
            ServerPlayNetworking.send(player, Channels.PLAYER_SKILLS_PACKET, buffer);
        }));

        ServerPlayNetworking.registerGlobalReceiver(Channels.PLAYER_XP_PACKET, ((server, player, handler, buf, responseSender) -> {
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeInt(1); //number of Experience data
            buffer.writeInt(ExpType.SMITHING.ordinal()); //ExpType
            buffer.writeInt(320); //Exp points
            ServerPlayNetworking.send(player, Channels.PLAYER_XP_PACKET, buffer);
        }));
    }
}
