package clashsoft.mods.avi.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import clashsoft.mods.avi.client.gui.GuiAdvancedVillager;
import clashsoft.mods.avi.common.AVICommonProxy;
import clashsoft.mods.avi.entity.EntityAdvancedVillager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.village.MerchantRecipeList;
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
	
	@Override
	public void onTradeListUpdate(EntityPlayer player, Packet250CustomPayload packet)
	{
		DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(packet.data));

        try
        {
            int i = datainputstream.readInt();
            GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
            
            if (guiscreen != null && guiscreen instanceof GuiAdvancedVillager && i == player.openContainer.windowId)
            {
                IMerchant imerchant = ((GuiAdvancedVillager)guiscreen).getIMerchant();
                MerchantRecipeList merchantrecipelist = MerchantRecipeList.readRecipiesFromStream(datainputstream);
                imerchant.setRecipes(merchantrecipelist);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}
}
