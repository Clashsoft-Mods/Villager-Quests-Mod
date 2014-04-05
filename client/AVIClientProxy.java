package clashsoft.mods.avi.client;

import clashsoft.mods.avi.client.gui.GuiVillager2;
import clashsoft.mods.avi.common.AVICommonProxy;
import clashsoft.mods.avi.entity.EntityVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AVIClientProxy extends AVICommonProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			EntityVillager2 villager = getVillager(world, x);
			return new GuiVillager2(player.inventory, villager, world, villager.getCustomNameTag());
		}
		return null;
	}
}
