package clashsoft.mods.villagerquests.entity;

import clashsoft.mods.villagerquests.quest.QuestList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class VQEntityProperties implements IExtendedEntityProperties
{
	public Entity		entity;
	protected QuestList	quests;
	
	public VQEntityProperties(Entity entity)
	{
		this.entity = entity;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		this.quests.writeToNBT(compound);
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.quests = QuestList.readFromNBT(compound);
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
