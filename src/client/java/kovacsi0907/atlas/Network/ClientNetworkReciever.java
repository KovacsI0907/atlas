package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.AtlasClient;
import kovacsi0907.atlas.Experience;
import kovacsi0907.atlas.Skills.ExpType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.List;

public abstract class ClientNetworkReciever {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_SKILLS_PACKET, ((client, handler, buf, responseSender) -> {
            AtlasClient.skills = List.of(buf.readString().split(";"));
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_XP_PACKET, ((client, handler, buf, responseSender) -> {
            ExpType[] enumValues = ExpType.values();
            AtlasClient.experienceList.clear();
            for(int i = buf.readInt();i>0;i--){
                AtlasClient.experienceList.add(new Experience(enumValues[buf.readInt()], buf.readInt()));
            }
        }));
    }
}
