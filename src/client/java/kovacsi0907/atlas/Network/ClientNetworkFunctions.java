package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.AtlasClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public abstract class ClientNetworkFunctions {
    public static void syncSkills() {
        ClientPlayNetworking.send(Channels.PLAYER_SKILLS_PACKET, PacketByteBufs.empty());
    }

    public static void syncXP(){
        ClientPlayNetworking.send(Channels.PLAYER_XP_PACKET, PacketByteBufs.empty());
    }

    public static void requestGetSkill(String skillId){
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeString(skillId);
        ClientPlayNetworking.send(Channels.REQUEST_GET_SKILL_PACKET, buffer);
    }

    public static void requestGetWareStacksForVendor(String vendorId) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeString(vendorId);
        ClientPlayNetworking.send(Channels.REQUEST_GET_WARESTACKS, buffer);
    }

    public static void requestGetWareStacksForVendorAndWait(String vendorId){
        requestGetWareStacksForVendor(vendorId);
        while(!AtlasClient.wareStacksUpdated) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        AtlasClient.wareStacksUpdated = false;
    }

    public static String requestSellItemsAndGetResponse(int slot, int itemId, String vendorId, int amount, int price, int discount) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(slot);
        buffer.writeInt(itemId);
        buffer.writeString(vendorId);
        buffer.writeInt(amount);
        buffer.writeInt(price);
        buffer.writeInt(discount);
        ClientPlayNetworking.send(Channels.REQUEST_SELL_ITEMS, buffer);
        while(AtlasClient.sellResponse.equals("")){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String response = AtlasClient.sellResponse;
        AtlasClient.sellResponse = "";
        return response;
    }
}
