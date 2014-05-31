package clashsoft.mods.villagerquests.common;

import clashsoft.cslib.minecraft.common.BaseProxy;
import clashsoft.mods.villagerquests.entity.EntityVillager2;
import clashsoft.mods.villagerquests.inventory.ContainerVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AVIProxy extends BaseProxy
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
			return new ContainerVillager2(player.inventory, this.getVillager(world, x), world);
		}
		return null;
	}
}
