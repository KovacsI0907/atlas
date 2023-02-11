package kovacsi0907.atlas.Blocks;

import kovacsi0907.atlas.ImplementedInventory;
import kovacsi0907.atlas.Recipes.CustomSmithingRecipe;
import kovacsi0907.atlas.ScreenHandlers.SmithingStationScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SmithingStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 10;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    public SmithingStationBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.SMITHING_STATION_ENTITY, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0: return SmithingStationBlockEntity.this.progress;
                    case 1: return SmithingStationBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0: SmithingStationBlockEntity.this.progress = value; break;
                    case 1: SmithingStationBlockEntity.this.maxProgress = value; break;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("smithing_station_name");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SmithingStationScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, SmithingStationBlockEntity entity) {
        if(world.isClient)
            return;

        if(hasRecipe(entity)){
            entity.progress++;
            markDirty(world, blockPos, state);
            if(entity.progress>=entity.maxProgress){
                craftItem(entity);
            }
        }else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(SmithingStationBlockEntity entity) {
        SimpleInventory tempInventory = new SimpleInventory(entity.size());
        for(int i = 0;i<entity.size();i++){
            tempInventory.setStack(i, entity.getStack(i));
        }

        Optional<CustomSmithingRecipe> match = entity.getWorld().getRecipeManager().getFirstMatch(CustomSmithingRecipe.Type.INSTANCE, tempInventory, entity.getWorld());

        if(hasRecipe(entity)){
            entity.removeStack(1,1);
            entity.setStack(2, new ItemStack(match.get().getOutput().getItem(), entity.getStack(2).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(SmithingStationBlockEntity entity) {
        SimpleInventory tempInventory = new SimpleInventory(entity.size());
        for(int i = 0;i<entity.size();i++){
            tempInventory.setStack(i, entity.getStack(i));
        }

        Optional<CustomSmithingRecipe> match = entity.getWorld().getRecipeManager().getFirstMatch(CustomSmithingRecipe.Type.INSTANCE, tempInventory, entity.getWorld());

        return match.isPresent() && canInsertItemIntoResultSlot(tempInventory, match.get().getOutput().getItem()) && canInsertAmountIntoResultSlot(tempInventory, 1);
    }

    private static boolean canInsertAmountIntoResultSlot(SimpleInventory stationInventory, int count) {
        return stationInventory.getStack(2).getMaxCount() > stationInventory.getStack(2).getCount() + count;
    }

    private static boolean canInsertItemIntoResultSlot(SimpleInventory stationInventory, Item item) {
        return stationInventory.getStack(2).getItem() == item || stationInventory.getStack(2).isEmpty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("smithing_station.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("smithing_station.progress");
        super.readNbt(nbt);
    }
}
