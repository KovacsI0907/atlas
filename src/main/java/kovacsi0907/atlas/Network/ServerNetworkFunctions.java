package kovacsi0907.atlas.Network;

import kovacsi0907.atlas.Data.PlayerData;
import kovacsi0907.atlas.Data.ServerData;
import kovacsi0907.atlas.Skills.Experience;
import kovacsi0907.atlas.Skills.Skill;
import kovacsi0907.atlas.Skills.Skills;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

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
}
