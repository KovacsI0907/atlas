package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.Data.PlayerData;
import kovacsi0907.atlas.Data.ServerData;
import kovacsi0907.atlas.Data.WareStack;
import kovacsi0907.atlas.Skills.Experience;
import kovacsi0907.atlas.Skills.Skill;
import kovacsi0907.atlas.Skills.Skills;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public final class ServerNetworkFunctions {
    public static void sendExperiencePacket(ServerPlayerEntity player, MinecraftServer server) {
        //first overall, then spendable
        //int : length of list
        //(n times) :
        //      int: ordinal of type enum
        //      int: value of exp for that type
        PacketByteBuf buffer = PacketByteBufs.create();
        PlayerData playerData = ServerData.getOrCreatePlayerData(server, player.getUuidAsString());
        //overall
        buffer.writeInt(playerData.overallExperienceList.size());
        for(Experience xp : playerData.experienceList){
            buffer.writeInt(xp.type.ordinal());
            buffer.writeInt(xp.points);
        }
        //spendable
        buffer.writeInt(playerData.experienceList.size());
        for(Experience xp : playerData.experienceList){
            buffer.writeInt(xp.type.ordinal());
            buffer.writeInt(xp.points);
        }

        ServerPlayNetworking.send(player, Channels.PLAYER_XP_PACKET, buffer);
    }

    public static void sendUnlockedSkillsPacket(ServerPlayerEntity player, MinecraftServer server){
        //int : length of list
        //(n times):
        //  string: skill id
        PacketByteBuf buffer = PacketByteBufs.create();
        PlayerData playerData = ServerData.getOrCreatePlayerData(server, player.getUuidAsString());
        buffer.writeInt(playerData.unlockedSkills.size());
        for(String skillId : playerData.unlockedSkills){
            buffer.writeString(skillId);
        }

        ServerPlayNetworking.send(player, Channels.PLAYER_SKILLS_PACKET, buffer);
    }

    public static void checkAndAddNewSkill(ServerPlayerEntity player, MinecraftServer server, String skillId){
        PlayerData playerData = ServerData.getOrCreatePlayerData(server, player.getUuidAsString());
        Skill skill = Skills.getSkillFromId(skillId);
        if(skill.xpRequired - playerData.getSpendableXp(skill.expType) <= 0) {
            playerData.unlockedSkills.add(skillId);
            playerData.addXp(skill.expType, (-1)*skill.xpRequired);
        }
        sendUnlockedSkillsPacket(player, server);
        sendExperiencePacket(player, server);
    }

    public static void sendWareStacksPacket(ServerPlayerEntity player, MinecraftServer server, String vendorId, int from, int howMany) {
        List<WareStack> allStacks = ServerData.getWareStacksForVendor(server, vendorId);
        List<WareStack> toSend = new ArrayList<>();
        PacketByteBuf buffer = PacketByteBufs.create();
        if(from > allStacks.size() || howMany<=0) {
            buffer.writeInt(0);
            ServerPlayNetworking.send(player, Channels.REQUEST_GET_WARESTACKS, buffer);
            return;
        }

        int to = from + howMany;
        if(to > allStacks.size())
            to = allStacks.size();
        for(int i = from;i<to;i++){
            toSend.add(allStacks.get(i));
        }
        buffer.writeInt(toSend.size());
        for (WareStack wareStack : toSend)
            buffer.writeBytes(wareStack.createPacket());
        ServerPlayNetworking.send(player, Channels.REQUEST_GET_WARESTACKS, buffer);
    }

    public static void sendMoney(MinecraftServer server, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeDouble(ServerData.getOrCreatePlayerData(server, player.getUuidAsString()).money);
        ServerPlayNetworking.send(player, Channels.GET_MONEY, buffer);
    }

    public static void sendBuyStackResponse(MinecraftServer server, ServerPlayerEntity player, String stackId, int amount, String vendorId) {
        ServerData serverData = ServerData.getServerData(server);
        if(!serverData.wareStacks.containsKey(stackId)){
            ServerPlayNetworking.send(player, Channels.BUY_STACK, PacketByteBufs.create().writeString("stack_not_found"));
            return;
        }
        WareStack stack = serverData.wareStacks.get(stackId);
        if(amount<=0 || amount>stack.count){
            ServerPlayNetworking.send(player, Channels.BUY_STACK, PacketByteBufs.create().writeString("invalid_amount"));
            return;
        }
        double price = (stack.price/stack.count)*amount;
        if(amount>=stack.discountVolume)
            price = price*(100-stack.bulkDiscount)/100;
        double money = ServerData.getOrCreatePlayerData(server, player.getUuidAsString()).money;
        if(money < price){
            ServerPlayNetworking.send(player, Channels.BUY_STACK, PacketByteBufs.create().writeString("not_enough_money"));
            return;
        }
        if(player.getInventory().insertStack(new ItemStack(stack.item, amount))){
            player.getInventory().markDirty();
            ServerData.getOrCreatePlayerData(server, player.getUuidAsString()).money -= price;
            serverData.wareStacks.get(stackId).count-=amount;
            if(serverData.wareStacks.get(stackId).count <= 0)
                serverData.wareStacks.remove(stackId);
            ServerData.getOrCreatePlayerData(server, stack.playerUuid).money += price;
            ServerPlayNetworking.send(player, Channels.BUY_STACK, PacketByteBufs.create().writeString("success"));
        }else{
            ServerPlayNetworking.send(player, Channels.BUY_STACK, PacketByteBufs.create().writeString("not_enough_room"));
        }
    }
}
