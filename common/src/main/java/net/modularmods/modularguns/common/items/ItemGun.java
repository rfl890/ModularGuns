package net.modularmods.modularguns.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.modularmods.modularguns.ModularGuns;
import net.modularmods.modularguns.common.modular.type.BaseType;
import net.modularmods.modularguns.common.types.GunType;

public class ItemGun extends Item {

    public static ItemGun instance;

    public ItemGun() {
        //super(new Properties().tab(ModularGuns.getInstance().getContentPackManager().getTab()).stacksTo(1));
        super(new Item.Properties().stacksTo(1));
        instance = this;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity holdingEntity, int intI, boolean flag) {
        if (holdingEntity instanceof Player) {
            Player player = (Player) holdingEntity;
            if (player.getItemInHand(InteractionHand.MAIN_HAND) != null && player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemGun) {
                ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (heldStack.getTag() == null) {
                    CompoundTag compoundTag = new CompoundTag();
                    BaseType type = ModularGuns.getInstance().contentPackManager.baseTypes.get(0);
                    compoundTag.putString("type", type.internalName);
                    heldStack.setTag(compoundTag);
                    stack.setHoverName(Component.literal(type.displayName));
                }
            }
        }
    }

    public static GunType getGunType(ItemStack gunStack) {
        if (gunStack.getTag() != null) {
            return ModularGuns.getInstance().getContentPackManager().getType(GunType.class, gunStack.getTag().getString("type"));
        } else {
            return new GunType();
        }
    }

    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }
}
