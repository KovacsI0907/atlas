package kovacsi0907.atlas.Blocks;

import kovacsi0907.atlas.Data.ServerData;
import kovacsi0907.atlas.ImplementedInventory;
import kovacsi0907.atlas.Recipes.CustomSmithingRecipe;
import kovacsi0907.atlas.ScreenHandlers.SmithingStationScreenHandler;
import kovacsi0907.atlas.Skills.ExpType;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SmithingStationBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 40;
    private int fuel = 0;
    private int maxFuel = 0;

    private PlayerEntity lastInteractingPlayer = null;

    //index 0-8:crafting items, index 9:fuel, index 10: crafting result
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(11, ItemStack.EMPTY);
    public SmithingStationBlockEntity(BlockPos pos, BlockState state) {
        super(CustomBlocks.SMITHING_STATION_ENTITY, pos, state);

        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SmithingStationBlockEntity.this.progress;
                    case 1 -> SmithingStationBlockEntity.this.maxProgress;
                    case 2 -> SmithingStationBlockEntity.this.fuel;
                    case 3 -> SmithingStationBlockEntity.this.maxFuel;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SmithingStationBlockEntity.this.progress = value;
                    case 1 -> SmithingStationBlockEntity.this.maxProgress = value;
                    case 2 -> SmithingStationBlockEntity.this.fuel = value;
                    case 3 -> SmithingStationBlockEntity.this.maxFuel = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };

    }

    public static int getResultSlotIndex() {
        return 10;
    }

    public static int getFuelSlotIndex() {
        return 9;
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
        lastInteractingPlayer = player;
        return new SmithingStationScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, SmithingStationBlockEntity entity) {
        if(world.isClient)
            return;

        boolean hadFuel = false;
        if(entity.fuel > 0){
            entity.fuel--;
            hadFuel = true;
        }

        CustomSmithingRecipe recipe = getRecipe(entity);
        if(recipe != null){
            if(entity.lastInteractingPlayer != null && hasSkills(entity.lastInteractingPlayer, recipe.getSkillsNeeded(), recipe.allSkillsNecessary)) {
                entity.maxProgress = recipe.getTimeToMake();
                if (entity.fuel <= 0)
                    addFuel(entity);
                if (hadFuel) {
                    entity.progress++;
                    markDirty(world, blockPos, state);
                }
                if (entity.progress >= entity.maxProgress) {
                    craftItem(entity);
                }
            }
        }else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }

    private static void addFuel(SmithingStationBlockEntity entity) {
        SimpleInventory tempInventory = new SimpleInventory(entity.size());
        for(int i = 0;i<entity.size();i++){
            tempInventory.setStack(i, entity.getStack(i));
        }

        if(tempInventory.getStack(getFuelSlotIndex()).isEmpty())
            return;

        entity.fuel = FuelRegistry.INSTANCE.get(entity.removeStack(getFuelSlotIndex(), 1).getItem());
        entity.maxFuel = entity.fuel;
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
            for(int i = 0;i<9;i++){
                int x = i%3;
                int y = i/3;
                int invIndex = 3*x + y;
                entity.removeStack(i, match.get().itemCounts.get(invIndex));
            }
            entity.setStack(getResultSlotIndex(), new ItemStack(match.get().getOutput().getItem(), entity.getStack(getResultSlotIndex()).getCount() + 1));
            if(entity.lastInteractingPlayer != null)
                ServerData.getOrCreatePlayerData(entity.getWorld().getServer(), entity.lastInteractingPlayer.getUuidAsString()).addXp(ExpType.SMITHING, match.get().getXpGain());
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

    private static CustomSmithingRecipe getRecipe(SmithingStationBlockEntity entity) {
        SimpleInventory tempInventory = new SimpleInventory(entity.size());
        for(int i = 0;i<entity.size();i++){
            tempInventory.setStack(i, entity.getStack(i));
        }

        Optional<CustomSmithingRecipe> match = entity.getWorld().getRecipeManager().getFirstMatch(CustomSmithingRecipe.Type.INSTANCE, tempInventory, entity.getWorld());
        if(match.isPresent() && canInsertItemIntoResultSlot(tempInventory, match.get().getOutput().getItem()) && canInsertAmountIntoResultSlot(tempInventory, 1))
            return match.get();
        return null;
    }

    private static boolean canInsertAmountIntoResultSlot(SimpleInventory stationInventory, int count) {
        return stationInventory.getStack(getResultSlotIndex()).getMaxCount() >= stationInventory.getStack(getResultSlotIndex()).getCount() + count;
    }

    private static boolean canInsertItemIntoResultSlot(SimpleInventory stationInventory, Item item) {
        return stationInventory.getStack(getResultSlotIndex()).getItem() == item || stationInventory.getStack(getResultSlotIndex()).isEmpty();
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

    public static boolean hasSkills(PlayerEntity player, List<String> neededSkills, boolean allSkillsNecessary){
        if(neededSkills.isEmpty())
            return true;
        List<String> unlockedSkills = ServerData.getOrCreatePlayerData(player.getServer(), player.getUuidAsString()).unlockedSkills;
        for(String skill : neededSkills){
            boolean hasSkill = unlockedSkills.contains(skill);
            if(allSkillsNecessary && !hasSkill)
                return false;
            else if(hasSkill)
                return true;
        }

        return false;
    }
}
