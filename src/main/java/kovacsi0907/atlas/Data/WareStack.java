package kovacsi0907.atlas.Data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class WareStack {
    public String playerUuid;
    public String stackUuid;
    public Item item;
    public int count;
    public double price;
    public int bulkDiscount;
    public int discountVolume;
    List<String> vendorIds;

    public WareStack(String playerUuid, String stackUuid, Item item, int count, double price, int bulkDiscount, int discountVolume, List<String> vendorIds) {
        this.playerUuid = playerUuid;
        this.stackUuid = stackUuid;
        this.item = item;
        this.count = count;
        this.price = price;
        this. bulkDiscount = bulkDiscount;
        this.discountVolume = discountVolume;
        this.vendorIds = vendorIds;
    }

    public WareStack(String playerUuid, Item item, int count, double price, int bulkDiscount, int discountVolume, List<String> vendorIds) {
        this.playerUuid = playerUuid;
        this.stackUuid = getNewUuid();
        this.item = item;
        this.count = count;
        this.price = price;
        this. bulkDiscount = bulkDiscount;
        this.discountVolume = discountVolume;
        this.vendorIds = vendorIds;
    }

    String getNewUuid() {
        String s;
        do{
            s = new BigInteger(50, new SecureRandom()).toString(32);
        }while(s.length()<10);
        return s.substring(0,10);
    }

    public NbtCompound createNbtCompound(){
        NbtCompound wareStackCompound = new NbtCompound();
        wareStackCompound.putString("uuid", playerUuid);
        wareStackCompound.putInt("itemId", Item.getRawId(item));
        wareStackCompound.putInt("count", count);
        wareStackCompound.putDouble("price", price);
        wareStackCompound.putInt("bulkDiscount", bulkDiscount);
        wareStackCompound.putInt("discountVolume", discountVolume);
        NbtCompound vendorsCompound = new NbtCompound();
        for(String vendorId : vendorIds)
            vendorsCompound.putString(vendorId, vendorId);
        wareStackCompound.put("vendors", vendorsCompound);
        return wareStackCompound;
    }

    public static WareStack createFromNbt(NbtCompound wareStackCompound) {
        List<String> vendors = (List<String>) wareStackCompound.getCompound("vendors").getKeys();
        return new WareStack(wareStackCompound.getString("uuid"),
                wareStackCompound.getString("playerName"),
                Item.byRawId(wareStackCompound.getInt("itemId")),
                wareStackCompound.getInt("count"),
                wareStackCompound.getInt("price"),
                wareStackCompound.getInt("bulkDiscount"),
                wareStackCompound.getInt("discountVolume"),
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
        buffer.writeInt(discountVolume);
        return buffer;
    }

    public static WareStack fromPacket(PacketByteBuf buffer) {
        return new WareStack(buffer.readString(),
                buffer.readString(),
                Item.byRawId(buffer.readInt()),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readInt(),
                buffer.readInt(),
                null);
    }
}
