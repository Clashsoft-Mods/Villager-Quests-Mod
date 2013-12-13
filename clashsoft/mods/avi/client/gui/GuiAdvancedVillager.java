package clashsoft.mods.avi.client.gui;

import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiAdvancedVillager extends GuiMerchant
{
	public boolean questMode = false;
	
	public static ResourceLocation questBackground = new ResourceLocation("avi", "textures/gui/container/villager_quests.png");
	public static ResourceLocation tradeBackground = new ResourceLocation("avi", "textures/gui/container/villager_trading.png");
	
	public GuiAdvancedVillager(InventoryPlayer inventory, IMerchant merchant, World world, String name)
	{
		super(inventory, merchant, world, name);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		if (!questMode)
		{
			super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		}
		else
		{
			
		}	
	}
}
