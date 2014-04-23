package clashsoft.mods.avi.quest;

import java.util.ArrayList;
import java.util.Random;

import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.quest.type.QuestType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class QuestList extends ArrayList<Quest>
{
	private static final long	serialVersionUID	= -3968245030216357893L;
	
	public static boolean		COMPLETE_ALL		= false;
	
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
	
	public void shuffle(EntityPlayer player, Random random)
	{
		if (COMPLETE_ALL)
		{
			for (Quest quest : this)
			{
				if (!quest.checkCompleted(player))
				{
					return;
				}
			}
		}
		
		this.clear();
		for (int i = 0; i < 32 && this.size() < 3; i++)
		{
			Quest quest = Quest.random(provider, random);
			
			if (!this.containsType(quest.getType()))
			{
				if (quest.hasAmount())
				{
					while (quest.checkCompleted(player) && quest.amount < 1024)
					{
						quest.amount += quest.getType().getAmount(random);
					}
				}
				else if (quest.checkCompleted(player))
				{
					continue;
				}
				this.add(quest);
			}
		}
	}
	
	public boolean containsType(QuestType type)
	{
		for (Quest quest : this)
		{
			if (quest != null && quest.getType() == type)
			{
				return true;
			}
		}
		return false;
	}
	
	public void refresh(EntityPlayer player, Random random)
	{
		boolean flag = true;
		for (Quest quest : this)
		{
			flag &= quest.checkCompleted(player) & quest.isRewarded();
		}
		if (flag)
		{
			this.shuffle(player, random);
		}
	}
	
	public void reward(EntityPlayer player)
	{
		int reward = 0;
		for (Quest quest : this)
		{
			quest.checkCompleted(player);
			quest.reward(player);
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
			q.writeToBuffer(buffer);
		}
	}
	
	public static QuestList readFromNBT(NBTTagCompound nbt)
	{
		QuestList quests = new QuestList(null);
		
		NBTTagList list = (NBTTagList) nbt.getTag("Quests");
		if (list != null)
		{
			for (int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound nbt1 = list.getCompoundTagAt(i);
				Quest q = new Quest();
				q.readFromNBT(nbt1);
				quests.add(q);
			}
		}
		else
		{
			quests.shuffle(null, new Random());
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