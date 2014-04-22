package clashsoft.mods.avi.inventory;

import clashsoft.mods.avi.api.IQuestProvider;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerVillager2 extends Container
{
	public IMerchant			theMerchant;
	public InventoryMerchant	merchantInventory;
	
	private Slot inputSlot1;
	private Slot inputSlot2;
	private Slot outputSlot;
	
	private final World			theWorld;
	
	public ContainerVillager2(InventoryPlayer inventory, IQuestProvider merchant, World world)
	{
		this.theMerchant = merchant;
		this.theWorld = world;
		this.merchantInventory = new InventoryMerchant(inventory.player, merchant);
		
		this.inputSlot1 = new Slot(this.merchantInventory, 0, 8, 53);
		this.inputSlot2  = new Slot(this.merchantInventory, 1, 34, 53);
		this.outputSlot = new SlotMerchantResult(inventory.player, merchant, this.merchantInventory, 2, 92, 53);
		this.addSlotToContainer(this.inputSlot1);
		this.addSlotToContainer(this.inputSlot2);
		this.addSlotToContainer(this.outputSlot);
		
		int i;
		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}
	
	public void setQuestMode(boolean questMode)
	{
		if (questMode)
		{
			this.inputSlot1.yDisplayPosition = -2000;
			this.inputSlot2.yDisplayPosition = -2000;
			this.outputSlot.yDisplayPosition = -2000;
		}
		else
		{
			this.inputSlot1.yDisplayPosition = 53;
			this.inputSlot2.yDisplayPosition = 53;
			this.outputSlot.yDisplayPosition = 53;
		}
	}
	
	public InventoryMerchant getMerchantInventory()
	{
		return this.merchantInventory;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory iinventory)
	{
		this.merchantInventory.resetRecipeAndSlots();
		super.onCraftMatrixChanged(iinventory);
	}
	
	public void setCurrentRecipeIndex(int slot)
	{
		this.merchantInventory.setCurrentRecipeIndex(slot);
	}
	
	@Override
	public void updateProgressBar(int id, int progress)
	{
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.theMerchant.getCustomer() == player;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotID);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (slotID == 2)
			{
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (slotID != 0 && slotID != 1)
			{
				if (slotID >= 3 && slotID < 30)
				{
					if (!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if (slotID >= 30 && slotID < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 3, 39, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
		}
		
		return itemstack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		this.theMerchant.setCustomer((EntityPlayer) null);
		super.onContainerClosed(par1EntityPlayer);
		
		if (!this.theWorld.isRemote)
		{
			ItemStack itemstack = this.merchantInventory.getStackInSlotOnClosing(0);
			
			if (itemstack != null)
			{
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
			
			itemstack = this.merchantInventory.getStackInSlotOnClosing(1);
			
			if (itemstack != null)
			{
				par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
			}
		}
	}
}
