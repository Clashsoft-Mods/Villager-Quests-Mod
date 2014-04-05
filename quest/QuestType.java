package clashsoft.mods.avi.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class QuestType
{
	public static Map<String, QuestType>	questMap		= new HashMap();
	public static List<QuestType>			questList		= new ArrayList();
	
	public static QuestType					generic			= new QuestType("quest.generic", 0);
	public static QuestType					collectWood		= new QuestCollect("quest.collect.wood", 1, new ItemStack(Blocks.log, 16, OreDictionary.WILDCARD_VALUE));
	public static QuestType					collectStone	= new QuestCollect("quest.collect.stone", 2, new ItemStack(Blocks.stone));
	public static QuestType					findDiamond		= new QuestCollect("quest.find.diamond", 2, new ItemStack(Items.diamond));
	
	private int								id;
	private String							name;
	private int								reward;
	
	public QuestType(String name, int reward)
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
	
	public int getReward()
	{
		return this.reward;
	}
	
	public static int rewardIcon(int reward)
	{
		if (reward < 9)
		{
			return 0;
		}
		else if (reward < 27)
		{
			return 1;
		}
		else if (reward < 81)
		{
			return 2;
		}
		else if (reward < 163)
		{
			return 3;
		}
		return 4;
	}
	
	public static ItemStack rewardToStack(int reward)
	{
		if (reward < 9)
		{
			return new ItemStack(Items.gold_nugget, reward);
		}
		else if (reward < 27)
		{
			return new ItemStack(Items.gold_ingot, reward / 9);
		}
		else if (reward < 81)
		{
			return new ItemStack(Items.emerald, reward / 27);
		}
		return new ItemStack(Items.diamond, reward / 81);
	}
}
