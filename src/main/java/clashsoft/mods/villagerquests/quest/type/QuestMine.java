package clashsoft.mods.villagerquests.quest.type;

import java.util.Random;

import net.minecraft.block.Block;

public class QuestMine extends QuestType
{
	public Block	block;
	public int		metadata;
	
	public QuestMine(String name, float reward, Block block)
	{
		this(name, reward, block, 0);
	}
	
	public QuestMine(String name, float reward, Block block, int metadata)
	{
		super(name, reward);
		this.block = block;
		this.metadata = metadata;
	}
	
	public boolean blockMatches(Block block, int metadata)
	{
		return this.block == block && this.metadata == metadata;
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
