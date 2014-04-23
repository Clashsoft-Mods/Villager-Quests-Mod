package clashsoft.mods.avi.quest.type;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class QuestFind extends QuestType
{
	public Item		item;
	public Block	block;
	
	public QuestFind(String name, float reward, Item item, Block block)
	{
		super(name, reward);
		this.item = item;
		this.block = block;
	}
	
	@Override
	public boolean isCompleted(EntityPlayer player, int amount)
	{
		return QuestType.getItemCount(player, this.item, this.block) > 0;
	}
}
