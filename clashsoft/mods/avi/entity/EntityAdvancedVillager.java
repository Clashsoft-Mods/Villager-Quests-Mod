package clashsoft.mods.avi.entity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EntityAdvancedVillager extends EntityVillager
{
	public List<Quest> quests = new ArrayList();
	
	public EntityAdvancedVillager(World world)
	{
		super(world);
	}
	
	public EntityAdvancedVillager(World world, int profession)
	{
		super(world, profession);
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
