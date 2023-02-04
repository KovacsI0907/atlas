package kovacsi0907.atlas.Data;

import kovacsi0907.atlas.Atlas;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerData extends PersistentState {

    public HashMap<String, PlayerData> playerData = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playerDataCompound = new NbtCompound();
        playerData.forEach((uuid, data) ->{
            playerDataCompound.put(uuid, data.createNbtCompound());
        });
        nbt.put("playerData", playerDataCompound);
        return nbt;
    }

    public static ServerData createFromNbt(NbtCompound mainCompound){
        ServerData serverData = new ServerData();
        NbtCompound playerDataCompound = mainCompound.getCompound("playerData");
        playerDataCompound.getKeys().forEach((uuid) ->{
            serverData.playerData.put(uuid, PlayerData.createFromNbt(playerDataCompound.getCompound(uuid), uuid));
        });

        return serverData;
    }

    public static ServerData getServerData(MinecraftServer server){
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        ServerData serverData = persistentStateManager.getOrCreate(ServerData::createFromNbt, ServerData::new, Atlas.MOD_ID);
        serverData.markDirty();

        return serverData;
    }

    public static PlayerData getOrCreatePlayerData(MinecraftServer server, String playerUuid){
        ServerData serverData = getServerData(server);
        PlayerData data = serverData.playerData.get(playerUuid);
        if(data == null){
            serverData.playerData.put(playerUuid, new PlayerData(playerUuid));
            data = serverData.playerData.get(playerUuid);
        }

        return data;
    }
}
