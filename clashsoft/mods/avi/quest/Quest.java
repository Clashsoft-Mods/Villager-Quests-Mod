package clashsoft.mods.avi.quest;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;

public class Quest
{
	public static Map<String, Quest> quests = new HashMap();
	
	public String name;
	public String description;
	public int reward;
	
	public Quest(String name, String description, int reward)
	{
		this.name = name;
		this.description = description;
		this.reward = reward;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Name", this.name);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		Quest original = quests.get(nbt.getString("Name"));
		copy(original, this);
	}
	
	public static Quest getQuestFromNBT(NBTTagCompound nbt)
	{
		Quest original = quests.get(nbt.getString("Name"));
		return original;
	}
	
	public static void copy(Quest src, Quest dest)
	{
		dest.name = src.name;
		dest.description = src.description;
		dest.reward = src.reward;
	}
}
