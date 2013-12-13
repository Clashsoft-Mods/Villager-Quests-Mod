package clashsoft.mods.avi.common;

import clashsoft.mods.avi.client.gui.GuiAdvancedVillager;
import clashsoft.mods.avi.entity.EntityAdvancedVillager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class AVICommonProxy implements IGuiHandler
{
	public EntityAdvancedVillager getVillager(World world, int entityID)
	{
		return (EntityAdvancedVillager) world.getEntityByID(entityID);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new ContainerMerchant(player.inventory, getVillager(world, x), world);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			EntityAdvancedVillager villager = getVillager(world, x);
			return new GuiAdvancedVillager(player.inventory, villager, world, villager.getCustomNameTag());
		}
		return null;
	}
	
}
