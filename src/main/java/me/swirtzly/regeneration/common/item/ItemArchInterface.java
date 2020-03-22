package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.item.arch.ArchHelper;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Swirtzly on 29/01/2020 @ 20:41
 */
public class ItemArchInterface extends Item {


    public ItemArchInterface() {
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    /**
     * Called when the equipped item is right clicked.
     */

    //TODO Finish me
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack1.isEmpty()) {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            playerIn.openGui(RegenerationMod.INSTANCE, 99, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            itemstack.setCount(0);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        } else {
            if (playerIn.isSneaking()) {
                ArchHelper.onArchUse(playerIn, itemstack1.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(3));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new InvProvider();
    }


    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }

    private static class InvProvider implements ICapabilitySerializable<NBTBase> {

        private final IItemHandler inv = new ItemStackHandler(24) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack toInsert, boolean simulate) {
                if (!toInsert.isEmpty()) {
                    boolean isUseAble = toInsert.hasCapability(CapabilityArch.CAPABILITY, null);
                    if (isUseAble)
                        return super.insertItem(slot, toInsert, simulate);
                }
                return toInsert;
            }
        };

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
            else return null;
        }

        @Override
        public NBTBase serializeNBT() {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
        }
    }
}
