package clashsoft.mods.avi.quest;

import java.util.*;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class Quest
{
	public static Map<String, Quest> questMap = new HashMap();
	public static List<Quest> questList = new ArrayList();
	
	public static Quest collectWood = new QuestCollect("collect.wood", 1, new ItemStack(Blocks.log, 16, OreDictionary.WILDCARD_VALUE));
	public static Quest collectStone = new QuestCollect("collect.stone", 2, new ItemStack(Blocks.stone));
	
	public String name;
	public int reward;
	
	public Quest(String name, int reward)
	{
		this.name = name;
		this.reward = reward;
		
		questMap.put(name, this);
		questList.add(this);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Name", this.name);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		Quest original = questMap.get(nbt.getString("Name"));
		copy(original, this);
	}
	
	public static Quest getQuestFromNBT(NBTTagCompound nbt)
	{
		Quest original = questMap.get(nbt.getString("Name"));
		return original;
	}
	
	public static Quest random(Random seed)
	{
		return questList.get(seed.nextInt(questList.size()));
	}
	
	public static void copy(Quest src, Quest dest)
	{
		dest.name = src.name;
		dest.reward = src.reward;
	}
}
