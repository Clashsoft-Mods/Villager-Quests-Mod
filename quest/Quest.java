package clashsoft.mods.avi.quest;

import java.util.Random;
import clashsoft.mods.avi.api.IQuestProvider;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class Quest
{
	private IQuestProvider provider;
	private QuestType type;
	
	public Quest()
	{
	}
	
	public Quest(IQuestProvider provider, QuestType type)
	{
		this.provider = provider;
		this.type = type;
	}
	
	public IQuestProvider getProvider()
	{
		return this.provider;
	}
	
	public QuestType getType()
	{
		return this.type;
	}
	
	public String getName()
	{
		return this.type.getName();
	}

	public int getReward()
	{
		return (int) (this.type.getReward() * this.provider.getRewardMultiplier());
	}
	
	public void setProvider(IQuestProvider provider)
	{
		this.provider = provider;
	}
	
	public static Quest random(IQuestProvider provider, Random seed)
	{
		QuestType type = QuestType.questList.get(seed.nextInt(QuestType.questList.size()));
		return new Quest(provider, type);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Type", this.type.getID());
	}
	
	public void writeToBuffer(PacketBuffer buffer)
	{
		buffer.writeInt(this.type.getID());
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.type = QuestType.get(nbt.getInteger("Type"));
	}
	
	public void readFromBuffer(PacketBuffer buffer)
	{
		this.type = QuestType.get(buffer.readInt());
	}
}
