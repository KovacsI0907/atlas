package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.ClientData;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.Skills.ExpType;
import kovacsi0907.atlas.Skills.Experience;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public abstract class ClientNetworkReceiver {

    public static void registerListeners() {
        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_SKILLS_PACKET, ((client, handler, buf, responseSender) -> {
            ClientData.skills.clear();
            for(int i = buf.readInt();i>0;i--){
                ClientData.skills.add(buf.readString());
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.PLAYER_XP_PACKET, ((client, handler, buf, responseSender) -> {
            ExpType[] enumValues = ExpType.values();
            ClientData.experienceList.clear();
            for(int i = buf.readInt();i>0;i--){
                ClientData.experienceList.add(new Experience(enumValues[buf.readInt()], buf.readInt()));
            }
            ClientData.overallExperienceList.clear();
            for(int i = buf.readInt();i>0;i--){
                ClientData.overallExperienceList.add(new Experience(enumValues[buf.readInt()], buf.readInt()));
            }
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.REQUEST_GET_WARESTACKS, (((client, handler, buf, responseSender) -> {
            ClientData.wareStacks.clear();
            for(int i = buf.readInt();i>0;i--){
                ClientData.wareStacks.add(WareStack.fromPacket(buf));
            }
            TrackedNetworkReciever.receive(Channels.REQUEST_GET_WARESTACKS);
        })));

        ClientPlayNetworking.registerGlobalReceiver(Channels.REQUEST_SELL_ITEMS, ((client, handler, buf, responseSender) -> {
            ClientData.sellResponse = buf.readString();
            TrackedNetworkReciever.receive(Channels.REQUEST_SELL_ITEMS);
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.GET_MONEY, ((client, handler, buf, responseSender) -> {
            ClientData.money = buf.readDouble();
            TrackedNetworkReciever.receive(Channels.GET_MONEY);
        }));

        ClientPlayNetworking.registerGlobalReceiver(Channels.BUY_STACK, (((client, handler, buf, responseSender) -> {
            ClientData.buyResponse = buf.readString();
            TrackedNetworkReciever.receive(Channels.BUY_STACK);
        })));
    }
}
