package com.benbenlaw.starforge.block.entity;

import com.benbenlaw.core.block.entity.SyncableBlockEntity;
import com.benbenlaw.core.block.entity.handler.IInventoryHandlingBlockEntity;
import com.benbenlaw.core.block.entity.handler.InputOutputItemHandler;
import com.benbenlaw.starforge.block.SFBlockEntities;
import com.benbenlaw.starforge.block.custom.StarBlock;
import com.benbenlaw.starforge.config.SFConfig;
import com.benbenlaw.starforge.recipe.StarForgeRecipe;
import com.benbenlaw.starforge.recipe.StarForgeRecipeInput;
import com.benbenlaw.starforge.util.SFTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StarForgeBlockEntity extends SyncableBlockEntity implements IInventoryHandlingBlockEntity {

    private int starPower;
    public boolean isCharging;
    public int progress;
    public int maxProgress;
    boolean isCrafting = false;
    private RecipeHolder<StarForgeRecipe> currentRecipe = null;
    private List<BlockPos> activePedestalPositions = new ArrayList<>();
    private ItemStack incomingOutput;

    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            progress = 0;
            setChanged();
            sync();
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    public StarForgeBlockEntity(BlockPos pos, BlockState state) {
        super(SFBlockEntities.STAR_FORGE_BLOCK_ENTITY.get(), pos, state);
    }

    private final IItemHandler forgeItemHandler = new InputOutputItemHandler(itemHandler,
            (i, stack) -> i == 0 ,  //
            i -> i == 1
    );

    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        return forgeItemHandler;
    }

    public void tick() {
        Level level = this.getLevel();
        assert level != null;

        if (level.isClientSide()) return;

        charge(level);

        if (currentRecipe == null || !matchingRecipe(level).map(r -> r.value().equals(currentRecipe.value())).orElse(false)) {
            currentRecipe = matchingRecipe(level).orElse(null);
            progress = 0;
        }

        int forgeTier = getStructureTier(level);
        boolean canCraft = currentRecipe != null
                && canInsertResult(currentRecipe)
                && forgeTier >= currentRecipe.value().tier()
                && starPower >= currentRecipe.value().starPower();

        if (canCraft) {

            if (!isCrafting) { // craft just started
                isCrafting = true;
                progress = 0;
                maxProgress = currentRecipe.value().duration();

                // Clear old list
                activePedestalPositions.clear();

                // Track pedestals that will be used
                if (currentRecipe.value().extraIngredients().isPresent()) {
                    List<PedestalBlockEntity> pedestals = getNearbyPedestals(level);
                    List<Ingredient> extras = currentRecipe.value().extraIngredients().get();

                    for (Ingredient extra : extras) {
                        for (PedestalBlockEntity pedestal : pedestals) {
                            ItemStack stack = pedestal.getItemStackHandler().getStackInSlot(0);
                            if (extra.test(stack) && !activePedestalPositions.contains(pedestal.getBlockPos())) {
                                activePedestalPositions.add(pedestal.getBlockPos());
                                break;
                            }
                        }
                    }
                }
            }

            // --- Progress crafting ---
            progress++;
            if (progress == 1) {
                level.playSound(null, worldPosition, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.5f, 0.3f);
            }

            if (progress == 20) {
                level.playSound(null, worldPosition, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.5f, 0.3f);
            }
            sync();

            // Craft finished
            if (progress >= maxProgress) {
                // Consume main input
                itemHandler.extractItem(0, 1, false);

                // Consume pedestals
                for (BlockPos pos : activePedestalPositions) {
                    if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                        pedestal.getItemStackHandler().extractItem(0, 1, false);
                        pedestal.setChanged();
                    }
                }

                // Output result
                itemHandler.insertItem(1, currentRecipe.value().result().copy(), false);
                starPower -= currentRecipe.value().starPower();
                level.playSound(null, worldPosition, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.5f, 0.3f);
                // Reset
                progress = 0;
                maxProgress = 0;
                isCrafting = false;
                currentRecipe = null;
                activePedestalPositions.clear();
            }

        } else {
            // Not crafting
            isCrafting = false;
            progress = 0;
            maxProgress = 0;
            currentRecipe = null;
            activePedestalPositions.clear(); // important: clear if craft canceled
        }

        setChanged();
        sync();
    }


    private boolean canInsertResult(RecipeHolder<StarForgeRecipe> recipeHolder) {
        ItemStack resultStack = recipeHolder.value().result();
        ItemStack outputStack = itemHandler.getStackInSlot(1);

        if (outputStack.isEmpty()) {
            return true;
        } else if (outputStack.getItem() == resultStack.getItem()) {
            int combinedCount = outputStack.getCount() + resultStack.getCount();
            return combinedCount <= outputStack.getMaxStackSize();
        } else {
            return false;
        }
    }

    public Optional<RecipeHolder<StarForgeRecipe>> matchingRecipe(Level level) {
        if (level == null) return Optional.empty();

        RecipeManager recipeManager = level.getRecipeManager();
        StarForgeRecipeInput recipeInput = new StarForgeRecipeInput(itemHandler);

        List<PedestalBlockEntity> pedestals = getNearbyPedestals(level);
        List<ItemStack> pedestalItems = pedestals.stream().map(pedestalBlockEntity ->
                pedestalBlockEntity.getItemStackHandler().getStackInSlot(0)).toList();


        return recipeManager.getAllRecipesFor(StarForgeRecipe.Type.INSTANCE).stream()
                .filter(recipe -> {
                    // Main input
                    if (!recipe.value().input().test(recipeInput.getItem(0))) return false;

                    incomingOutput = recipe.value().result();

                    if (recipe.value().extraIngredients().isPresent()) {
                        List<Ingredient> extras = new ArrayList<>(recipe.value().extraIngredients().get());
                        List<ItemStack> available = new ArrayList<>(pedestalItems);

                        for (Ingredient extra : extras) {
                            boolean matched = false;
                            for (ItemStack stack : available) {
                                if (extra.test(stack)) {
                                    matched = true;
                                    available.remove(stack); // remove matched stack
                                    break;
                                }
                            }
                            if (!matched) return false; // could not find a matching pedestal
                        }
                    }

                    return true;
                })
                .findFirst();

    }

    public void charge(Level level) {
        if (level == null) return;

        BlockPos starPos = this.worldPosition.above(4);
        BlockState starBlockState = level.getBlockState(starPos);

        int tier = getStructureTier(level);
        if (tier == -1) {
            isCharging = false;
            return;
        }

        if (!(starBlockState.getBlock() instanceof StarBlock)) {
            isCharging = false;
            return;
        }

        if (!(level.getBlockEntity(starPos) instanceof StarBlockEntity starEntity)) {
            isCharging = false;
            return;
        }

        int maxForgePower = getMaxStarPower(tier);

        int remainingCapacity = maxForgePower - starPower;
        if (remainingCapacity <= 0) {
            isCharging = false; // Forge full, cannot charge
            return;
        }

        int amountToExtract = Math.min(starEntity.getTransferRate(), remainingCapacity);
        int extracted = starEntity.extractPower(amountToExtract);

        starPower += extracted;
        isCharging = extracted > 0;

        setChanged();
        sync();
    }

    public int getStructureTier(Level level) {
        for (int t = 5; t >= 1; t--) {
            if (checkStructure(level, t)) return t;
        }

        if (checkStructure(level, 0)) return 0;

        return -1;
    }

    public boolean checkStructure(Level level, int tier) {
        if (level == null) return false;

        BlockPos forgePos = this.worldPosition;

        // 5x5 Base Blocks
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) continue; // skip center
                BlockPos pos = forgePos.offset(x, -1, z); // base layer
                if (!level.getBlockState(pos).is(SFTags.Blocks.STAR_FORGE_BASE_BLOCKS)) return false;
            }
        }

        // Pillars at corners
        int[] xs = {-3, 3};
        int[] zs = {-3, 3};
        for (int xCorner : xs) {
            for (int zCorner : zs) {
                for (int y = 0; y < 4; y++) { // 4 blocks above base
                    BlockPos pillarPos = forgePos.offset(xCorner, y - 1, zCorner);
                    if (!level.getBlockState(pillarPos).is(SFTags.Blocks.STAR_FORGE_PILLAR_BLOCKS)) return false;
                }
            }
        }

        // Caps on pillars
        if (tier >= 1) {
            for (int xCorner : xs) {
                for (int zCorner : zs) {
                    BlockPos capPos = forgePos.offset(xCorner, 3, zCorner);

                    boolean validCap = switch (tier) {
                        case 1 -> level.getBlockState(capPos).is(SFTags.Blocks.STAR_FORGE_TIER_1_CAP);
                        case 2 -> level.getBlockState(capPos).is(SFTags.Blocks.STAR_FORGE_TIER_2_CAP);
                        case 3 -> level.getBlockState(capPos).is(SFTags.Blocks.STAR_FORGE_TIER_3_CAP);
                        case 4 -> level.getBlockState(capPos).is(SFTags.Blocks.STAR_FORGE_TIER_4_CAP);
                        case 5 -> level.getBlockState(capPos).is(SFTags.Blocks.STAR_FORGE_TIER_5_CAP);
                        default -> false;
                    };

                    if (!validCap) return false;
                }
            }
        }

        return true; // structure is valid
    }

    public void rightClick(Player player) {
        ItemStack heldItem = player.getMainHandItem();

        // Stick debug message
        if (heldItem.is(Items.STICK)) {
            player.sendSystemMessage(Component.nullToEmpty(starPower + "/" + getMaxStarPower(getStructureTier(level))));
            return;
        }

        ItemStack outputStack = itemHandler.getStackInSlot(1);
        ItemStack inputStack = itemHandler.getStackInSlot(0);

        ItemStack extracted = ItemStack.EMPTY;

        if (!outputStack.isEmpty()) {
            extracted = itemHandler.extractItem(1, 1, false);
        } else if (!inputStack.isEmpty()) {
            extracted = itemHandler.extractItem(0, 1, false);
        }

        if (!extracted.isEmpty()) {
            if (heldItem.isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, extracted);
            } else {
                boolean added = player.getInventory().add(extracted);
                if (!added) player.drop(extracted, false);
            }
            level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1.0f);
            setChanged();
            sync();
            return;
        }

        if (!heldItem.isEmpty()) {
            ItemStack toInsert = heldItem.copy();
            toInsert.setCount(1);

            ItemStack remaining = itemHandler.insertItem(0, toInsert, false);
            if (remaining.isEmpty()) {
                heldItem.shrink(1);
                player.setItemInHand(player.getUsedItemHand(), heldItem);
                level.playSound(null, worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1.0f);
                setChanged();
                sync();
            }
        }
    }

    public int getMaxStarPower(int tier) {
        return switch (tier) {
            case 1 -> SFConfig.forgeCapacityCap1.get();
            case 2 -> SFConfig.forgeCapacityCap2.get();
            case 3 -> SFConfig.forgeCapacityCap3.get();
            case 4 -> SFConfig.forgeCapacityCap4.get();
            case 5 -> SFConfig.forgeCapacityCap5.get();
            default -> SFConfig.forgeCapacity.get();
        };
    }

    private List<PedestalBlockEntity> getNearbyPedestals(Level level) {
        List<PedestalBlockEntity> pedestals = new ArrayList<>();
        BlockPos forgePos = this.worldPosition;

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos checkPos = forgePos.offset(x, 0, z);
                if (level.getBlockEntity(checkPos) instanceof PedestalBlockEntity pedestal) {
                    pedestals.add(pedestal);
                }
            }
        }

        return pedestals;
    }

    public List<BlockPos> getActivePedestalPositions() {
        return activePedestalPositions;
    }

    public int getStarPower() {
        return starPower;
    }

    public boolean isCrafting() {
        return isCrafting;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public ItemStack getIncomingOutput() {
        return incomingOutput;
    }

    @Override
    public void setHandler(ItemStackHandler handler) {
        for (int i = 0; i < handler.getSlots(); i++) {
            this.itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
        }
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return this.itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("inventory", itemHandler.serializeNBT(provider));
        tag.putInt("starPower", starPower);
        tag.putBoolean("isCharging", isCharging);
        tag.putInt("progress", progress);
        tag.putInt("maxProgress", maxProgress);
        tag.putBoolean("isCrafting", isCrafting);

        ListTag posList = new ListTag();
        for (BlockPos pos : activePedestalPositions) {
            CompoundTag posTag = new CompoundTag();
            posTag.put("pos", NbtUtils.writeBlockPos(pos)); // put the IntArrayTag under some key
            posList.add(posTag);
        }
        tag.put("activePedestalPositions", posList);

        if (incomingOutput != null && !incomingOutput.isEmpty()) {
            tag.put("incomingOutput", incomingOutput.save(provider));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.itemHandler.deserializeNBT(provider, tag.getCompound("inventory"));
        starPower = tag.getInt("starPower");
        isCharging = tag.getBoolean("isCharging");
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("maxProgress");
        isCrafting = tag.getBoolean("isCrafting");

        if (tag.contains("incomingOutput", Tag.TAG_COMPOUND)) { // 10 = CompoundTag
            incomingOutput = ItemStack.parse(provider, tag.getCompound("incomingOutput")).get();
        } else {
            incomingOutput = ItemStack.EMPTY;
        }

        activePedestalPositions.clear();
        ListTag posList = tag.getList("activePedestalPositions", Tag.TAG_COMPOUND);
        for (int i = 0; i < posList.size(); i++) {
            CompoundTag posTag = posList.getCompound(i);
            NbtUtils.readBlockPos(posTag, "pos").ifPresent(activePedestalPositions::add);
        }

    }
}
