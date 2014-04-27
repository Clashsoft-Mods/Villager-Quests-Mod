package clashsoft.mods.villagerquests.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.villagerquests.inventory.ContainerVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;

public class PacketSetRecipe extends CSPacket
{
	public int	recipe;
	
	public PacketSetRecipe()
	{
	}
	
	public PacketSetRecipe(int recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.recipe);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.recipe = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
		Container container = player.openContainer;
		
		if (container instanceof ContainerVillager2)
		{
			((ContainerVillager2) container).setCurrentRecipeIndex(this.recipe);
		}
	}
}
