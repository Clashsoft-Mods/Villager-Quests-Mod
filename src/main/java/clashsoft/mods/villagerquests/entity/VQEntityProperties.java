package clashsoft.mods.villagerquests.entity;

import clashsoft.mods.villagerquests.quest.Quest;
import clashsoft.mods.villagerquests.quest.QuestList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class VQEntityProperties implements IExtendedEntityProperties
{
	public EntityPlayer	player;
	protected QuestList	quests	= new QuestList();
	
	public VQEntityProperties(Entity entity)
	{
		this.player = (EntityPlayer) entity;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		this.quests.writeToNBT(compound);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		for (Quest quest : QuestList.readFromNBT(compound))
		{
			if (!quest.isCompleted(this.player))
			{
				this.quests.add(quest);
			}
		}
	}
	
	public QuestList getQuests()
	{
		return this.quests;
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
}
