package clashsoft.mods.avi.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.avi.entity.EntityVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.village.MerchantRecipeList;

public class PacketRecipeList extends CSPacket
{
	public int					villager;
	public MerchantRecipeList	recipeList;
	
	public PacketRecipeList()
	{
	}
	
	public PacketRecipeList(EntityVillager2 villager, MerchantRecipeList recipeList)
	{
		this.villager = villager.getEntityId();
		this.recipeList = recipeList;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.villager);
		this.recipeList.func_151391_a(buf);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.villager = buf.readInt();
		this.recipeList = MerchantRecipeList.func_151390_b(buf);
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		EntityVillager2 villager = (EntityVillager2) player.worldObj.getEntityByID(this.villager);
		villager.setRecipes(this.recipeList);
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
	}
}
