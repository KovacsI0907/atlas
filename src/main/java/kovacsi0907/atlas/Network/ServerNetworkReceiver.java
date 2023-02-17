package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.Data.ServerData;
import kovacsi0907.atlas.Data.WareStack;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

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

        ServerPlayNetworking.registerGlobalReceiver(Channels.REQUEST_GET_WARESTACKS, (((server, player, handler, buf, responseSender) -> {
            ServerNetworkFunctions.sendWareStacksPacket(player, server, buf.readString());
        })));

        ServerPlayNetworking.registerGlobalReceiver(Channels.REQUEST_SELL_ITEMS, ((server, player, handler, buf, responseSender) -> {
            int slot = buf.readInt();
            int itemId = buf.readInt();
            String vendorId = buf.readString();
            int amount = buf.readInt();
            int price = buf.readInt();
            int discount = buf.readInt();
            int discountVolume = buf.readInt();
            String response = "error";
            ItemStack stack = player.getInventory().main.get(slot);
            if(Item.getRawId(stack.getItem()) == itemId && stack.getCount() >= amount){
                player.getInventory().removeStack(slot, amount);
                player.getInventory().markDirty();
                ServerData.getServerData(server).wareStacks.add(new WareStack(
                        player.getUuidAsString(),
                        player.getGameProfile().getName(),
                        Item.byRawId(itemId),
                        amount,
                        price,
                        discount,
                        discountVolume,
                        Arrays.stream(new String[]{vendorId}).toList()
                ));
                response = "success";
            }


            ServerPlayNetworking.send(player, Channels.REQUEST_SELL_ITEMS, PacketByteBufs.create().writeString(response));
        }));

        ServerPlayNetworking.registerGlobalReceiver(Channels.GET_MONEY, ((server, player, handler, buf, responseSender) -> {
            ServerNetworkFunctions.sendMoney(server, player);
        }));
    }

}
