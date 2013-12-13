package clashsoft.mods.avi.client;

import clashsoft.mods.avi.client.gui.GuiAdvancedVillager;
import clashsoft.mods.avi.common.AVICommonProxy;
import clashsoft.mods.avi.entity.EntityAdvancedVillager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AVIClientProxy extends AVICommonProxy
{
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
