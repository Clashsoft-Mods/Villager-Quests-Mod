package clashsoft.mods.avi.client;

import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;

public class GuiAdvancedVillager extends GuiMerchant
{
	public GuiAdvancedVillager(InventoryPlayer inventory, IMerchant merchant, World world, String name)
	{
		super(inventory, merchant, world, name);
	}
	
}
