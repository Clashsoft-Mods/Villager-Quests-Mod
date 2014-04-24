package clashsoft.mods.avi.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.avi.entity.EntityVillager2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class PacketRefreshQuests extends CSPacket
{
	public int	villager;
	
	public PacketRefreshQuests()
	{
	}
	
	public PacketRefreshQuests(EntityVillager2 villager)
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
		villager.refreshQuests(player);
		villager.syncQuests(player);
	}
}
