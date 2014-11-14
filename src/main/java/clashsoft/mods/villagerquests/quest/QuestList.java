package clashsoft.mods.villagerquests.quest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import clashsoft.cslib.minecraft.entity.CSEntities;
import clashsoft.mods.villagerquests.entity.VQEntityProperties;
import clashsoft.mods.villagerquests.quest.type.QuestType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class QuestList extends ArrayList<Quest>
{
	private static final long	serialVersionUID	= -3968245030216357893L;
	
	public QuestList()
	{
	}
	
	public void shuffle(EntityPlayer player, Random random)
	{
		this.clear();
		int len = QuestType.questList.size();
		for (int i = 0; i < len; i++)
		{
			Quest quest = Quest.random(random);
			
			if (!this.containsType(quest.getType()))
			{
				if (quest.hasAmount())
				{
					while (quest.checkCompleted(player) && random.nextInt(1024) > 0)
					{
						quest.maxAmount += quest.getType().getRandomAmount(random);
					}
				}
				else if (quest.checkCompleted(player))
				{
					continue;
				}
				
				this.add(quest);
				
				if (this.size() == 3)
				{
					break;
				}
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
		QuestList playerQuests = QuestList.getPlayerQuests(player);
		
		boolean flag = true;
		for (Quest quest : this)
		{
			if (playerQuests != null && !playerQuests.contains(quest))
			{
				playerQuests.add(quest);
			}
			
			flag &= quest.checkCompleted(player) & quest.isRewarded();
		}
		if (flag)
		{
			this.shuffle(player, random);
		}
	}
	
	public void reward(EntityPlayer player)
	{
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
	
	public void writeToBuffer(PacketBuffer buffer) throws IOException
	{
		buffer.writeShort(this.size());
		for (Quest q : this)
		{
			q.writeToBuffer(buffer);
		}
	}
	
	public static QuestList readFromNBT(NBTTagCompound nbt)
	{
		QuestList quests = new QuestList();
		
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
	
	public static QuestList readFromBuffer(PacketBuffer buffer) throws IOException
	{
		QuestList quests = new QuestList();
		
		int size = buffer.readShort();
		for (int i = 0; i < size; i++)
		{
			Quest q = new Quest();
			q.readFromBuffer(buffer);
			quests.add(q);
		}
		
		return quests;
	}
	
	public static QuestList getPlayerQuests(EntityPlayer harvester)
	{
		if (harvester == null)
		{
			return null;
		}
		VQEntityProperties properties = (VQEntityProperties) CSEntities.getProperties("PlayerQuests", harvester);
		return properties.getQuests();
	}
}
