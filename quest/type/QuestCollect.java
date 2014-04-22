package clashsoft.mods.avi.quest.type;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class QuestCollect extends QuestType
{
	public ItemStack	stack;
	
	public QuestCollect(String name, float reward, ItemStack stack)
	{
		super(name, reward);
		this.stack = stack;
	}
	
	@Override
	public int getAmount(Random random)
	{
		int i = (int) (this.reward * 2.5F);
		return random.nextInt(16) + i;
	}
	
	@Override
	public boolean isCompleted(EntityPlayer player, int amount)
	{
		return QuestType.getItemCount(player, this.stack) > amount;
	}
}
