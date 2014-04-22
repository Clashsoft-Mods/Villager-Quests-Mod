package clashsoft.mods.avi.entity;

import java.util.Random;

import clashsoft.cslib.reflect.CSReflection;
import clashsoft.mods.avi.AVIMod;
import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.network.PacketQuestList;
import clashsoft.mods.avi.network.PacketRecipeList;
import clashsoft.mods.avi.quest.Quest;
import clashsoft.mods.avi.quest.QuestList;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityVillager2 extends EntityVillager implements IQuestProvider
{
	public QuestList	quests	= new QuestList(this);
	public Random		questRandom;
	
	public EntityVillager2(World world)
	{
		this(world, 0);
	}
	
	public EntityVillager2(World world, int profession)
	{
		super(world, profession);
		this.questRandom = new Random(world.getSeed() ^ this.rand.nextLong());
		this.shuffleQuests();
	}
	
	@Override
	public void setRecipes(MerchantRecipeList recipeList)
	{
		CSReflection.setValue(EntityVillager.class, this, recipeList, 5);
	}
	
	public void setQuests(QuestList questList)
	{
		questList.setProvider(this);
		this.quests = questList;
	}
	
	@Override
	public void shuffleQuests()
	{
		this.quests = new QuestList(this);
		for (int i = 0; i < 3; i++)
		{
			Quest quest = Quest.random(this, this.questRandom);
			this.quests.add(quest);
		}
	}
	
	@Override
	public void refreshQuests(EntityPlayerMP player)
	{
		for (Quest quest : this.quests)
		{
			quest.checkCompleted(player);
		}
	}
	
	@Override
	public void rewardQuests(EntityPlayerMP player)
	{
		int reward = 0;
		for (Quest quest : this.quests)
		{
			quest.checkCompleted(player);
			quest.reward(player);
		}
	}
	
	public void syncRecipeList(EntityPlayerMP player)
	{
		AVIMod.instance.netHandler.sendTo(new PacketRecipeList(this, this.getRecipes(player)), player);
	}
	
	public void syncQuests(EntityPlayerMP player)
	{
		AVIMod.instance.netHandler.sendTo(new PacketQuestList(this, this.quests), player);
	}
	
	@Override
	public float getRewardMultiplier()
	{
		return 1F;
	}
	
	@Override
	public QuestList getQuests()
	{
		return this.quests;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.quests.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.setQuests(QuestList.readFromNBT(nbt));
	}
	
	@Override
	public boolean interact(EntityPlayer player)
	{
		ItemStack itemstack = player.inventory.getCurrentItem();
		
		if ((itemstack == null || itemstack.getItem() != Items.spawn_egg) && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
		{
			if (!this.worldObj.isRemote)
			{
				this.setCustomer(player);
				this.syncRecipeList((EntityPlayerMP) player);
				this.syncQuests((EntityPlayerMP) player);
				player.openGui(AVIMod.instance, 0, this.worldObj, this.getEntityId(), 0, 0);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
}
