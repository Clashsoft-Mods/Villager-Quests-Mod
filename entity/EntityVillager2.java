package clashsoft.mods.avi.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clashsoft.cslib.reflect.CSReflection;
import clashsoft.mods.avi.AdvancedVillagerInteraction;
import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.network.PacketRecipeList;
import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityVillager2 extends EntityVillager implements IQuestProvider
{
	public List<Quest>	quests	= new ArrayList();
	public Random		questRandom;
	
	public EntityVillager2(World world)
	{
		this(world, 0);
	}
	
	public EntityVillager2(World world, int profession)
	{
		super(world, profession);
		this.questRandom = new Random(world.getSeed() ^ 29285198294861982L);
	}
	
	@Override
	public void setRecipes(MerchantRecipeList recipeList)
	{
		CSReflection.setValue(EntityVillager.class, this, recipeList, 5);
	}
	
	@Override
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
		
		NBTTagList list = nbt.getTagList("Quests", 11);
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound questNBT = list.getCompoundTagAt(i);
			Quest quest = Quest.getQuestFromNBT(questNBT);
			this.quests.add(quest);
		}
	}
	
	@Override
	public boolean interact(EntityPlayer player)
	{
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.getItem() == Items.spawn_egg;
		
		if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
		{
			if (!this.worldObj.isRemote)
			{
				this.setCustomer(player);
				this.displayGUIMerchant((EntityPlayerMP) player);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void displayGUIMerchant(EntityPlayerMP player)
	{
		this.syncRecipeList(player);
		player.openGui(AdvancedVillagerInteraction.instance, 0, this.worldObj, this.getEntityId(), 0, 0);
	}
	
	public void syncRecipeList(EntityPlayerMP player)
	{
		MerchantRecipeList merchantrecipelist = this.getRecipes(player);
		if (merchantrecipelist != null)
		{
			AdvancedVillagerInteraction.netHandler.sendTo(new PacketRecipeList(this, merchantrecipelist), player);
		}
	}
}
