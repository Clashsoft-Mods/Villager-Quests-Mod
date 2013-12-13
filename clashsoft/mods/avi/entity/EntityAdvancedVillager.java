package clashsoft.mods.avi.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EntityAdvancedVillager extends EntityVillager implements IQuestProvider
{
	public List<Quest> quests = new ArrayList();
	public Random questRandom;
	
	public EntityAdvancedVillager(World world)
	{
		this(world, 0);
	}
	
	public EntityAdvancedVillager(World world, int profession)
	{
		super(world, profession);
		this.questRandom = new Random(world.getSeed() ^ 29285198294861982L);
	}
	
	public void shuffleQuests()
	{
		this.quests.clear();
		for (int i = 0; i < 3; i++)
		{
			this.quests.add(Quest.random(this.questRandom));
		}
	}
	
	@Override
	public int getQuests()
	{
		return this.quests.size();
	}
	
	@Override
	public Quest getQuest(int index)
	{
		return this.quests.get(index);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		NBTTagList list = new NBTTagList();
		for (Quest quest : quests)
		{
			NBTTagCompound questNBT = new NBTTagCompound();
			quest.writeToNBT(questNBT);
			list.appendTag(questNBT);
		}
		
		nbt.setTag("Quests", list);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Quests");
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound questNBT = (NBTTagCompound) list.tagAt(i);
			Quest quest = Quest.getQuestFromNBT(questNBT);
			this.quests.add(quest);
		}
	}
}
