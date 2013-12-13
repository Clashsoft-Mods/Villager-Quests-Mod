package clashsoft.mods.avi.quest;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class Quest
{
	public static Map<String, Quest> questMap = new HashMap();
	public static List<Quest> questList = new ArrayList();
	
	public static Quest collectWood = new QuestCollect("Collect Wood", "Collect 16 wood logs of any type", 1, new ItemStack(Block.wood, 16, OreDictionary.WILDCARD_VALUE));
	public static Quest collectStone = new QuestCollect("Collect Stone", "Collect 64 smooth stones", 2, new ItemStack(Block.stone));
	
	public String name;
	public String description;
	public int reward;
	
	public Quest(String name, String description, int reward)
	{
		this.name = name;
		this.description = description;
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
		dest.description = src.description;
		dest.reward = src.reward;
	}
}
