package clashsoft.mods.villagerquests.common;

import clashsoft.mods.villagerquests.quest.Quest;
import clashsoft.mods.villagerquests.quest.QuestList;
import clashsoft.mods.villagerquests.quest.type.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class AVIEventHandler
{
	@SubscribeEvent
	public void onEntityJoined(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			int dimension = event.world.provider.dimensionId;
			EntityPlayer player = (EntityPlayer) event.entity;
			QuestList quests = QuestList.getPlayerQuests(player);
			
			for (Quest quest : quests)
			{
				QuestType type = quest.getType();
				if (type.getClass() == QuestDimension.class)
				{
					if (((QuestDimension) type).dimension == dimension)
					{
						quest.addAmount(player, 1F);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void harvestBlock(HarvestDropsEvent event)
	{
		if (event.harvester == null)
		{
			return;
		}
		
		QuestList quests = QuestList.getPlayerQuests(event.harvester);
		for (Quest quest : quests)
		{
			QuestType type = quest.getType();
			if (type instanceof QuestMine)
			{
				if (((QuestMine) type).blockMatches(event.block, event.blockMetadata))
				{
					quest.addAmount(event.harvester, 1F);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onCrafted(ItemCraftedEvent event)
	{
		ItemStack stack = event.crafting;
		QuestList quests = QuestList.getPlayerQuests(event.player);
		
		for (Quest quest : quests)
		{
			QuestType type = quest.getType();
			if (type instanceof QuestCraft)
			{
				if (((QuestItem) type).itemMatches(stack))
				{
					quest.addAmount(event.player, stack.stackSize);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemPickup(ItemPickupEvent event)
	{
		ItemStack stack = event.pickedUp.getEntityItem();
		QuestList quests = QuestList.getPlayerQuests(event.player);
		
		for (Quest quest : quests)
		{
			QuestType type = quest.getType();
			if (type instanceof QuestCollect)
			{
				if (((QuestItem) type).itemMatches(stack))
				{
					quest.addAmount(event.player, stack.stackSize);
				}
			}
		}
	}
}
