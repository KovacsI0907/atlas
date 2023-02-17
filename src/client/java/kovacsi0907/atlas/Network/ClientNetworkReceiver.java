package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.AtlasClient;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.Skills.ExpType;
import kovacsi0907.atlas.Skills.Experience;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public abstract class ClientNetworkReceiver {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_SKILLS_PACKET, ((client, handler, buf, responseSender) -> {
            AtlasClient.skills.clear();
            for(int i = buf.readInt();i>0;i--){
                AtlasClient.skills.add(buf.readString());
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_XP_PACKET, ((client, handler, buf, responseSender) -> {
            ExpType[] enumValues = ExpType.values();
            AtlasClient.experienceList.clear();
            for(int i = buf.readInt();i>0;i--){
                AtlasClient.experienceList.add(new Experience(enumValues[buf.readInt()], buf.readInt()));
            }
            AtlasClient.overallExperienceList.clear();
            for(int i = buf.readInt();i>0;i--){
                AtlasClient.overallExperienceList.add(new Experience(enumValues[buf.readInt()], buf.readInt()));
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.REQUEST_GET_WARESTACKS, (((client, handler, buf, responseSender) -> {
            AtlasClient.wareStacks.clear();
            for(int i = buf.readInt();i>0;i--){
                AtlasClient.wareStacks.add(WareStack.fromPacket(buf));
            }
            TrackedNetworkReciever.receive(Channels.REQUEST_GET_WARESTACKS);
        })));

        ClientPlayNetworking.registerGlobalReceiver(Channels.REQUEST_SELL_ITEMS, ((client, handler, buf, responseSender) -> {
            AtlasClient.sellResponse = buf.readString();
            TrackedNetworkReciever.receive(Channels.REQUEST_SELL_ITEMS);
        }));
    }
}
