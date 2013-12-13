package clashsoft.mods.avi.common;

import clashsoft.mods.avi.entity.EntityAdvancedVillager;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class AVIEventHandler
{
	@ForgeSubscribe
	public void onEntityJoined(EntityJoinWorldEvent event)
	{
		if (event.entity.getClass() == EntityVillager.class)
		{
			EntityVillager villager = (EntityVillager) event.entity;
			
			NBTTagCompound copy = new NBTTagCompound();
			villager.writeToNBT(copy);
			villager.setDead();
			
			EntityAdvancedVillager villager2 = new EntityAdvancedVillager(event.world, villager.getProfession());
			villager2.readFromNBT(copy);
			event.world.spawnEntityInWorld(villager2);
		}
	}
}
