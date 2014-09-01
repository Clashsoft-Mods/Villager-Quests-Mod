package clashsoft.mods.villagerquests.quest.type;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;

public class QuestType
{
	public static Map<String, QuestType>	questMap		= new HashMap();
	public static List<QuestType>			questList		= new ArrayList();
	
	public static QuestType					collectWood		= new QuestCollect("quest.collect.wood", 1.25F, Blocks.log);
	public static QuestType					collectStone	= new QuestCollect("quest.collect.stone", 2F, Blocks.stone);
	public static QuestType					collectDirt		= new QuestCollect("quest.collect.dirt", 1F, Blocks.dirt);
	public static QuestType					collectSand		= new QuestCollect("quest.collect.sand", 1.1F, Blocks.sand);
	public static QuestType					collectGravel	= new QuestCollect("quest.collect.gravel", 1.1F, Blocks.gravel);
	
	public static QuestType					findCoal		= new QuestFind("quest.find.coal", 5F, Items.coal, Blocks.coal_ore);
	public static QuestType					findIron		= new QuestFind("quest.find.iron", 7.5F, Items.iron_ingot, Blocks.iron_ore);
	public static QuestType					findGold		= new QuestFind("quest.find.gold", 10F, Items.gold_ingot, Blocks.gold_ore);
	public static QuestType					findLapis		= new QuestFind("quest.find.lapis", 12.5F, null, Blocks.lapis_ore);
	public static QuestType					findRedstone	= new QuestFind("quest.find.redstone", 15F, Items.redstone, Blocks.redstone_ore);
	public static QuestType					findEmerald		= new QuestFind("quest.find.emerald", 17.5F, Items.emerald, Blocks.emerald_ore);
	public static QuestType					findDiamond		= new QuestFind("quest.find.diamond", 20F, Items.diamond, Blocks.diamond_ore);
	
	public static QuestType					dungeon			= new QuestFind("quest.dungeon", 20F, null, Blocks.mossy_cobblestone);
	public static QuestType					nether			= new QuestStat("quest.nether", 20F, AchievementList.portal);
	public static QuestType					end				= new QuestStat("quest.end", 25F, AchievementList.theEnd);
	
	public static QuestType					craftPlanks		= new QuestCraft("quest.craft.planks", 1.5F, Blocks.planks);
	public static QuestType					craftSticks		= new QuestCraft("quest.craft.sticks", 1F, Items.stick);
	
	protected final String					name;
	protected float							reward;
	
	public QuestType(String name, float reward)
	{
		this.name = name;
		this.reward = reward;
		
		questMap.put(name, this);
		questList.add(this);
	}
	
	public static QuestType random(Random seed)
	{
		return questList.get(seed.nextInt(questList.size()));
	}
	
	public static QuestType get(String name)
	{
		return questMap.get(name);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public float getReward()
	{
		return this.reward;
	}
	
	public float getReward(float amount)
	{
		return this.reward * amount;
	}
	
	public boolean hasAmount()
	{
		return false;
	}
	
	public int getAmount(Random random)
	{
		return 0;
	}
	
	public float getCompletion(EntityPlayer player, float amount)
	{
		return 1F;
	}
	
	public static int rewardIcon(int reward)
	{
		if (reward == 0)
		{
			return -1;
		}
		else if (reward < 9)
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
		else if (reward < 162)
		{
			return 3;
		}
		return 4;
	}
	
	public static StatisticsFile getStats(EntityPlayer player)
	{
		return ((EntityPlayerMP) player).func_147099_x();
	}
	
	public static int getItemCount(EntityPlayer player, Item item, Block block)
	{
		int count = 0;
		StatisticsFile stats = getStats(player);
		StatBase stat;
		
		if (block != null)
		{
			stat = StatList.mineBlockStatArray[Block.getIdFromBlock(block)];
			if (stat != null)
			{
				count += stats.writeStat(stat);
			}
		}
		
		if (item != null)
		{
			stat = StatList.objectCraftStats[Item.getIdFromItem(item)];
			if (stat != null)
			{
				count += stats.writeStat(stat);
			}
		}
		
		return count;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		result = prime * result + Float.floatToIntBits(this.reward);
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		QuestType other = (QuestType) obj;
		if (this.reward != other.reward)
		{
			if (this.name == null)
			{
				if (other.name != null)
				{
					return false;
				}
			}
			else if (!this.name.equals(other.name))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("QuestType [name=").append(this.name);
		stringBuilder.append(", reward=").append(this.reward);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
