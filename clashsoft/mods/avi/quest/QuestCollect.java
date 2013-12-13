package clashsoft.mods.avi.quest;

import net.minecraft.item.ItemStack;

public class QuestCollect extends Quest
{
	public ItemStack stack;
	
	public QuestCollect(String name, String description, int reward, ItemStack stack)
	{
		super(name, description, reward);
		this.stack = stack;
	}
	
}
