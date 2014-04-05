package clashsoft.mods.avi.network;

import clashsoft.cslib.minecraft.network.CSPacket;
import clashsoft.mods.avi.entity.EntityVillager2;
import clashsoft.mods.avi.quest.QuestList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class PacketQuestList extends CSPacket
{
	public int villager;
	public QuestList questList;
	
	public PacketQuestList()
	{
	}
	
	public PacketQuestList(EntityVillager2 villager, QuestList questList)
	{
		this.villager = villager.getEntityId();
		this.questList = questList;
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeInt(this.villager);
		this.questList.writeToBuffer(buf);
	}
	
	@Override
	public void read(PacketBuffer buf)
	{
		this.villager = buf.readInt();
		this.questList = QuestList.readFromBuffer(buf);
	}
	
	@Override
	public void handleClient(EntityPlayer player)
	{
		EntityVillager2 villager = (EntityVillager2) player.worldObj.getEntityByID(this.villager);
		villager.setQuests(this.questList);
	}
	
	@Override
	public void handleServer(EntityPlayerMP player)
	{
		System.out.println();
	}
}
