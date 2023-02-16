package kovacsi0907.atlas.Data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class WareStack {
    String playerUuid;
    String stackUuid;
    Item item;
    int count;
    double price;
    int bulkDiscount;
    List<String> vendorIds;

    public WareStack(String playerUuid, Item item, int count, double price, int bulkDiscount, List<String> vendorIds) {
        this.playerUuid = playerUuid;
        this.stackUuid = new BigInteger(50, new SecureRandom()).toString(32).substring(0, 10);
        this.item = item;
        this.count = count;
        this.price = price;
        this. bulkDiscount = bulkDiscount;
        this.vendorIds = vendorIds;
    }

    public WareStack(String playerUuid, String stackUuid, Item item, int count, double price, int bulkDiscount, List<String> vendorIds) {
        this.playerUuid = playerUuid;
        this.stackUuid = stackUuid;
        this.item = item;
        this.count = count;
        this.price = price;
        this. bulkDiscount = bulkDiscount;
        this.vendorIds = vendorIds;
    }

    public NbtCompound createNbtCompound(){
        NbtCompound wareStackCompound = new NbtCompound();
        wareStackCompound.putString("uuid", playerUuid);
        wareStackCompound.putInt("itemId", Item.getRawId(item));
        wareStackCompound.putInt("count", count);
        wareStackCompound.putDouble("price", price);
        wareStackCompound.putInt("bulkDiscount", bulkDiscount);
        NbtCompound vendorsCompound = new NbtCompound();
        for(String vendorId : vendorIds)
            vendorsCompound.putString(vendorId, vendorId);
        wareStackCompound.put("vendors", vendorsCompound);
        return wareStackCompound;
    }

    public static WareStack createFromNbt(NbtCompound wareStackCompound) {
        List<String> vendors = (List<String>) wareStackCompound.getCompound("vendors").getKeys();
        return new WareStack(wareStackCompound.getString("uuid"),
                Item.byRawId(wareStackCompound.getInt("itemId")),
                wareStackCompound.getInt("count"),
                wareStackCompound.getInt("price"),
                wareStackCompound.getInt("bulkDiscount"),
                vendors);
    }

    public PacketByteBuf createPacket(){
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeString(playerUuid);
        buffer.writeString(stackUuid);
        buffer.writeInt(Item.getRawId(item));
        buffer.writeInt(count);
        buffer.writeDouble(price);
        buffer.writeInt(bulkDiscount);
        return buffer;
    }

    public static WareStack fromPacket(PacketByteBuf buffer) {
        return new WareStack(buffer.readString(),
                buffer.readString(),
                Item.byRawId(buffer.readInt()),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readInt(),
                null);
    }
}
