package clashsoft.mods.avi.entity;

import java.util.ArrayList;
import java.util.List;

import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
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
}
