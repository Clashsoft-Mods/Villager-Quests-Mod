package clashsoft.mods.villagerquests.client;

import clashsoft.mods.villagerquests.client.gui.GuiVillager2;
import clashsoft.mods.villagerquests.common.AVIProxy;
import clashsoft.mods.villagerquests.entity.EntityVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class AVIClientProxy extends AVIProxy
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == 0)
		{
			EntityVillager2 villager = this.getVillager(world, x);
			return new GuiVillager2(player.inventory, villager, world, villager.getCustomNameTag());
		}
		return null;
	}
}
