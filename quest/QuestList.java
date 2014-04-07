package clashsoft.mods.avi.quest;

import java.util.ArrayList;
import java.util.Random;

import clashsoft.mods.avi.api.IQuestProvider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class QuestList extends ArrayList<Quest>
{
	private static final long	serialVersionUID	= -3968245030216357893L;
	
	public IQuestProvider		provider;
	
	public QuestList(IQuestProvider provider)
	{
		this.provider = provider;
	}
	
	public void setProvider(IQuestProvider provider)
	{
		this.provider = provider;
		for (Quest quest : this)
		{
			if (quest != null)
			{
				quest.setProvider(provider);
			}
		}
	}
	
	@Override
	public boolean add(Quest quest)
	{
		quest.setProvider(this.provider);
		return super.add(quest);
	}
	
	public void shuffle(Random random)
	{
		this.clear();
		for (int i = 0; i < 2; i++)
		{
			this.add(Quest.random(this.provider, random));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (Quest q : this)
		{
			NBTTagCompound nbt1 = new NBTTagCompound();
			q.writeToNBT(nbt1);
			list.appendTag(nbt1);
		}
		nbt.setTag("Quests", list);
	}
	
	public void writeToBuffer(PacketBuffer buffer)
	{
		buffer.writeShort(this.size());
		for (Quest q : this)
		{
			q.readFromBuffer(buffer);
		}
	}
	
	public static QuestList readFromNBT(NBTTagCompound nbt)
	{
		QuestList quests = new QuestList(null);
		
		NBTTagList list = nbt.getTagList("Quests", 11);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			Quest q = new Quest();
			q.readFromNBT(nbt1);
			quests.add(q);
		}
		
		return quests;
	}
	
	public static QuestList readFromBuffer(PacketBuffer buffer)
	{
		QuestList quests = new QuestList(null);
		
		int size = buffer.readShort();
		for (int i = 0; i < size; i++)
		{
			Quest q = new Quest();
			q.readFromBuffer(buffer);
			quests.add(q);
		}
		
		return quests;
	}
}
