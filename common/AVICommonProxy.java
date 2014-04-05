package clashsoft.mods.avi.common;

import clashsoft.mods.avi.entity.EntityVillager2;
import clashsoft.mods.avi.inventory.ContainerAdvancedVillager;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AVICommonProxy implements IGuiHandler
{
	public EntityVillager2 getVillager(World world, int entityID)
	{
		return (EntityVillager2) world.getEntityByID(entityID);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			return new ContainerAdvancedVillager(player.inventory, getVillager(world, x), world);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
