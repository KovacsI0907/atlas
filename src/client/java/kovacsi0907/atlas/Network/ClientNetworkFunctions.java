package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.ClientData;
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

    public static void requestGetWareStacksForVendor(String vendorId, int from, int howMany) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeString(vendorId);
        buffer.writeInt(from);
        buffer.writeInt(howMany);
        ClientPlayNetworking.send(Channels.REQUEST_GET_WARESTACKS, buffer);
    }

    public static void requestGetWareStacksForVendorAndWait(String vendorId, int from, int howMany){
        requestGetWareStacksForVendor(vendorId, from, howMany);
        TrackedNetworkReciever.trackAndWait(Channels.REQUEST_GET_WARESTACKS, 20, 1000);
    }

    public static String requestSellItemsAndGetResponse(int slot, int itemId, String vendorId, int amount, int price, int discount, int discountVolume) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(slot);
        buffer.writeInt(itemId);
        buffer.writeString(vendorId);
        buffer.writeInt(amount);
        buffer.writeInt(price);
        buffer.writeInt(discount);
        buffer.writeInt(discountVolume);
        ClientPlayNetworking.send(Channels.REQUEST_SELL_ITEMS, buffer);
        TrackedNetworkReciever.trackAndWait(Channels.REQUEST_SELL_ITEMS, 20, 1000);
        String response = ClientData.sellResponse;
        ClientData.sellResponse = "";
        return response;
    }

    public static void requestGetMoneyAndWait() {
        ClientPlayNetworking.send(Channels.GET_MONEY, PacketByteBufs.empty());
        TrackedNetworkReciever.trackAndWait(Channels.GET_MONEY, 20, 1000);
    }

    public static String buyStackAndWait(String stackUuid, int count, String vendorId) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeString(stackUuid);
        buffer.writeInt(count);
        buffer.writeString(vendorId);
        ClientPlayNetworking.send(Channels.BUY_STACK,buffer);
        TrackedNetworkReciever.trackAndWait(Channels.BUY_STACK, 20, 1000);
        String response = ClientData.buyResponse;
        ClientData.buyResponse = "";
        return response;
    }
}
