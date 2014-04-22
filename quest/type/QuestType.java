package clashsoft.mods.avi.quest.type;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;

public class QuestType
{
	public static Map<String, QuestType>	questMap		= new HashMap();
	public static List<QuestType>			questList		= new ArrayList();
	
	public static QuestType					generic			= new QuestType("quest.generic", 0);
	public static QuestType					collectWood		= new QuestCollect("quest.collect.wood", 1F, new ItemStack(Blocks.log));
	public static QuestType					collectStone	= new QuestCollect("quest.collect.stone", 2F, new ItemStack(Blocks.stone));
	public static QuestType					findDiamond		= new QuestFind("quest.find.diamond", 20, new ItemStack(Items.diamond));
	
	protected final int						id;
	protected final String					name;
	protected float							reward;
	
	public QuestType(String name, float reward)
	{
		this.id = questList.size();
		this.name = name;
		this.reward = reward;
		
		questMap.put(name, this);
		questList.add(this);
	}
	
	public static QuestType get(String name)
	{
		return questMap.get(name);
	}
	
	public static QuestType get(int id)
	{
		return questList.get(id);
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public float getReward(int amount)
	{
		return this.reward * amount;
	}
	
	public int getAmount(Random random)
	{
		return 1;
	}
	
	public boolean isCompleted(EntityPlayer player, int amount)
	{
		return false;
	}
	
	public static int rewardIcon(int reward)
	{
		if (reward == 0)
		{
			return -1;
		}
		else if (reward < 8)
		{
			return 0;
		}
		else if (reward < 16)
		{
			return 1;
		}
		else if (reward < 32)
		{
			return 2;
		}
		else if (reward < 64)
		{
			return 3;
		}
		else if (reward < 128)
		{
			return 4;
		}
		return 5;
	}
	
	public static ItemStack rewardToStack(int reward)
	{
		if (reward == 0)
		{
			return null;
		}
		else if (reward < 8)
		{
			return new ItemStack(Items.gold_nugget, reward);
		}
		else if (reward < 16)
		{
			return new ItemStack(Items.gold_ingot, reward / 8);
		}
		else if (reward < 32)
		{
			return new ItemStack(Items.emerald, reward / 16);
		}
		else if (reward < 64)
		{
			return new ItemStack(Items.diamond, reward / 32);
		}
		return null;
	}
	
	public static int getItemCount(EntityPlayer player, ItemStack stack)
	{
		int count = 0;
		int id = Item.getIdFromItem(stack.getItem());
		StatisticsFile stats = ((EntityPlayerMP) player).func_147099_x();
		StatBase stat;
		
		stat = (StatBase) StatList.mineBlockStatArray[id];
		if (stat != null)
			count += stats.writeStat(stat);
		
		stat = (StatBase) StatList.objectCraftStats[id];
		if (stat != null)
			count += stats.writeStat(stat);
		
		return count;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.id;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + Float.floatToIntBits(this.reward);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestType other = (QuestType) obj;
		if (this.id != other.id)
			return false;
		if (this.reward != other.reward)
		if (this.name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!this.name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("QuestType [id=").append(this.id);
		stringBuilder.append(", name=").append(this.name);
		stringBuilder.append(", reward=").append(this.reward);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
