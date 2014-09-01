package clashsoft.mods.villagerquests.quest.type;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class QuestCollect extends QuestType
{
	public Item		item;
	public Block	block;
	
	public QuestCollect(String name, float reward, Block block)
	{
		this(name, reward, Item.getItemFromBlock(block), block);
	}
	
	public QuestCollect(String name, float reward, Item item, Block block)
	{
		super(name, reward);
		this.item = item;
		this.block = block;
	}
	
	@Override
	public boolean hasAmount()
	{
		return true;
	}
	
	@Override
	public int getAmount(Random random)
	{
		int i = (int) (this.reward * 3F);
		return random.nextInt(16) + i;
	}
	
	@Override
	public float getCompletion(EntityPlayer player, float amount)
	{
		return QuestType.getItemCount(player, this.item, this.block) / amount;
	}
}
