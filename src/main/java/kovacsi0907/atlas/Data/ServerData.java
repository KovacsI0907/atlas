package kovacsi0907.atlas.Data;

import kovacsi0907.atlas.Atlas;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerData extends PersistentState {

    public HashMap<String, PlayerData> playerData = new HashMap<>();
    public List<WareStack> wareStacks = new ArrayList<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playerDataCompound = new NbtCompound();
        playerData.forEach((uuid, data) ->{
            playerDataCompound.put(uuid, data.createNbtCompound());
        });
        nbt.put("playerData", playerDataCompound);

        NbtCompound wareStacksCompound = new NbtCompound();
        for(WareStack stack : wareStacks){
            wareStacksCompound.put(stack.stackUuid ,stack.createNbtCompound());
        }
        nbt.put("wareStacks", wareStacksCompound);
        return nbt;
    }

    public static ServerData createFromNbt(NbtCompound mainCompound){
        ServerData serverData = new ServerData();
        NbtCompound playerDataCompound = mainCompound.getCompound("playerData");
        playerDataCompound.getKeys().forEach((uuid) ->{
            serverData.playerData.put(uuid, PlayerData.createFromNbt(playerDataCompound.getCompound(uuid), uuid));
        });

        NbtCompound wareStacksCompound = mainCompound.getCompound("wareStacks");
        wareStacksCompound.getKeys().forEach((uuid) -> {
            serverData.wareStacks.add(WareStack.createFromNbt(wareStacksCompound.getCompound(uuid)));
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

    public static List<WareStack> getWareStacksForVendor(MinecraftServer server, String vendorId){
        ServerData serverData = getServerData(server);
        List<WareStack> matching = new ArrayList<>();
        for(WareStack wareStack : serverData.wareStacks){
            if(wareStack.vendorIds.contains(vendorId))
                matching.add(wareStack);
        }
        return matching;
    }
}
