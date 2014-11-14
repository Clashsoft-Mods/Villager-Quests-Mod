package clashsoft.mods.villagerquests.quest.type;

import java.util.Random;

import clashsoft.cslib.minecraft.stack.CSStacks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class QuestItem extends QuestType
{
	public Item	item;
	public int	metadata;
	
	public QuestItem(String name, float reward, Block block)
	{
		this(name, reward, block, 0);
	}
	
	public QuestItem(String name, float reward, Block block, int metadata)
	{
		super(name, reward);
		this.item = Item.getItemFromBlock(block);
		this.metadata = metadata;
	}
	
	public QuestItem(String name, float reward, Item item)
	{
		this(name, reward, item, 0);
	}
	
	public QuestItem(String name, float reward, Item item, int metadata)
	{
		super(name, reward);
		this.item = item;
		this.metadata = metadata;
	}
	
	public boolean itemMatches(ItemStack stack)
	{
		return CSStacks.equals(this.item, this.metadata, stack.getItem(), stack.getItemDamage());
	}
	
	@Override
	public boolean hasAmount()
	{
		return true;
	}
	
	@Override
	public float getRandomAmount(Random random)
	{
		int i = (int) (this.reward * 3F);
		return random.nextInt(16) + i;
	}
}
