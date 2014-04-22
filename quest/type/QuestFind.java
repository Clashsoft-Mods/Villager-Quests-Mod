package clashsoft.mods.avi.quest.type;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class QuestFind extends QuestType
{
	public ItemStack	stack;
	
	public QuestFind(String name, float reward, ItemStack stack)
	{
		super(name, reward);
		this.stack = stack;
	}
	
	@Override
	public boolean isCompleted(EntityPlayer player, int amount)
	{
		return QuestType.getItemCount(player, this.stack) > 0;
	}
}
