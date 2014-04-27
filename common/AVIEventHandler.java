package clashsoft.mods.villagerquests.common;

import clashsoft.mods.villagerquests.entity.EntityVillager2;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class AVIEventHandler
{
	@SubscribeEvent
	public void onEntityJoined(EntityJoinWorldEvent event)
	{
		if (!event.world.isRemote && event.entity.getClass() == EntityVillager.class)
		{
			EntityVillager villager = (EntityVillager) event.entity;
			
			NBTTagCompound copy = new NBTTagCompound();
			villager.writeToNBT(copy);
			villager.setDead();
			
			EntityVillager2 villager2 = new EntityVillager2(event.world, villager.getProfession());
			villager2.readFromNBT(copy);
			event.world.spawnEntityInWorld(villager2);
		}
	}
}
