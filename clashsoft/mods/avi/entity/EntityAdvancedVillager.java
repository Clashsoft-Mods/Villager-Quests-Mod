package clashsoft.mods.avi.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EntityAdvancedVillager extends EntityVillager implements IQuestProvider
{
	public List<Quest>	quests	= new ArrayList();
	public Random		questRandom;
	
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
	
	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer player)
	{
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.itemID == Item.monsterPlacer.itemID;
		
		if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
		{
			if (!this.worldObj.isRemote)
			{
				this.setCustomer(player);
				
				player.displayGUIMerchant(this, this.getCustomNameTag());
				
				//player.openGui(AdvancedVillagerInteraction.instance, 0, this.worldObj, this.entityId, 0, 0);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
