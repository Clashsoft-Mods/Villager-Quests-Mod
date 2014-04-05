package clashsoft.mods.avi.common;

import clashsoft.mods.avi.entity.EntityAdvancedVillager;
import clashsoft.mods.avi.inventory.ContainerAdvancedVillager;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

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
			return new ContainerAdvancedVillager(player.inventory, getVillager(world, x), world);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
	
	public void onTradeListUpdate(EntityPlayer player, Packet250CustomPayload packet)
	{
	}
}
