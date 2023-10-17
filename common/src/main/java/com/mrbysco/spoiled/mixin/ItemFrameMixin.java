package com.mrbysco.spoiled.mixin;

import com.mrbysco.spoiled.util.SpoilHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * A mixin to implement the spoil rate logic directly onto an item frame.
 */
@Mixin(ItemFrame.class)
public abstract class ItemFrameMixin extends HangingEntity {

    protected ItemFrameMixin(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    /**
     * @see ItemFrame#setItem(ItemStack)
     */
    @Shadow
    public abstract void setItem(ItemStack stack);

    /**
     * @see ItemFrame#getItem()
     */
    @Shadow
    public abstract ItemStack getItem();

    @Override
    public void tick() {
        super.tick();
        // Execute at the end of the tick
        if (!this.level().isClientSide()) {
            SpoilHelper.spoilSingleItemAndReplace(this.level(), this.getItem(), this::setItem);
        }
    }
}
