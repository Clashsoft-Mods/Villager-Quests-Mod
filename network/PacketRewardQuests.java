package clashsoft.mods.villagerquests.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.villagerquests.entity.EntityVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class PacketRewardQuests extends CSPacket
{
	public int	villager;
	
	public PacketRewardQuests()
	{
	}
	
	public PacketRewardQuests(EntityVillager2 villager)
	{
		this.villager = villager.getEntityId();
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.villager);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.villager = buf.readInt();
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
		EntityVillager2 villager = (EntityVillager2) player.worldObj.getEntityByID(this.villager);
		villager.rewardQuests(player);
		villager.syncQuests(player);
	}
}
