package clashsoft.mods.villagerquests.entity;

import java.util.Random;

import clashsoft.cslib.reflect.CSReflection;
import clashsoft.mods.villagerquests.VillagerQuestsMod;
import clashsoft.mods.villagerquests.network.PacketQuestList;
import clashsoft.mods.villagerquests.network.PacketRecipeList;
import clashsoft.mods.villagerquests.quest.IQuestProvider;
import clashsoft.mods.villagerquests.quest.Quest;
import clashsoft.mods.villagerquests.quest.QuestList;

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
	public QuestList	quests	= new QuestList();
	public Random		questRandom;
	
	public EntityVillager2(World world)
	{
		this(world, 0);
	}
	
	public EntityVillager2(World world, int profession)
	{
		super(world, profession);
		this.questRandom = new Random(world.getSeed() ^ this.rand.nextLong());
	}
	
	@Override
	public void setRecipes(MerchantRecipeList recipeList)
	{
		CSReflection.setValue(EntityVillager.class, this, recipeList, 5);
	}
	
	public void setQuests(QuestList questList)
	{
		this.quests = questList;
	}
	
	@Override
	public void shuffleQuests(EntityPlayerMP player)
	{
		this.quests.shuffle(player, this.questRandom);
	}
	
	@Override
	public void refreshQuests(EntityPlayerMP player)
	{
		this.quests.refresh(player, this.questRandom);
	}
	
	@Override
	public void rewardQuests(EntityPlayerMP player)
	{
		this.quests.reward(player);
	}
	
	public void syncRecipeList(EntityPlayerMP player)
	{
		VillagerQuestsMod.instance.netHandler.sendTo(new PacketRecipeList(this, this.getRecipes(player)), player);
	}
	
	public void syncQuests(EntityPlayerMP player)
	{
		QuestList quests = QuestList.getPlayerQuests(player);
		for (Quest quest : this.quests)
		{
			if (!quests.contains(quest))
			{
				quests.add(quest);
			}
		}
		
		VillagerQuestsMod.instance.netHandler.sendTo(new PacketQuestList(this, this.quests), player);
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
				player.openGui(VillagerQuestsMod.instance, 0, this.worldObj, this.getEntityId(), 0, 0);
			}
			
			return true;
		}
		return false;
	}
}
