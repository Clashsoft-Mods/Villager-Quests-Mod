package clashsoft.mods.avi.quest;

import net.minecraft.item.ItemStack;

public class QuestCollect extends Quest
{
	public ItemStack stack;
	
	public QuestCollect(String name, int reward, ItemStack stack)
	{
		super(name, reward);
		this.stack = stack;
	}
	
}
