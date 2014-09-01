package clashsoft.mods.villagerquests.quest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import clashsoft.cslib.minecraft.lang.I18n;
import clashsoft.cslib.util.CSString;
import clashsoft.mods.villagerquests.quest.type.QuestType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class Quest
{
	private IQuestProvider	provider;
	private QuestType		type;
	public int				amount;
	
	private boolean			completed;
	private boolean			rewarded;
	
	private List<ItemStack>	rewards;
	
	public Quest()
	{
	}
	
	public Quest(IQuestProvider provider, QuestType type, int amount)
	{
		this.provider = provider;
		this.type = type;
		this.amount = amount;
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
		float f;
		if (this.hasAmount())
		{
			f = this.type.getReward(this.amount);
		}
		else
		{
			f = this.type.getReward();
		}
		return (int) (f * this.provider.getRewardMultiplier());
	}
	
	public void setProvider(IQuestProvider provider)
	{
		this.provider = provider;
	}
	
	public static Quest random(IQuestProvider provider, Random seed)
	{
		QuestType type = QuestType.random(seed);
		int amount = type.getAmount(seed);
		return new Quest(provider, type, amount);
	}
	
	public boolean isCompleted()
	{
		return this.completed;
	}
	
	public boolean isRewarded()
	{
		return this.rewarded;
	}
	
	public boolean hasAmount()
	{
		return this.type.hasAmount();
	}
	
	public boolean checkCompleted(EntityPlayer player)
	{
		if (player == null)
		{
			return false;
		}
		
		this.completed = this.type.isCompleted(player, this.amount);
		if (!this.completed)
		{
			this.rewarded = false;
		}
		return this.completed;
	}
	
	public List<ItemStack> getRewards()
	{
		if (this.rewards == null)
		{
			int i = this.getReward();
			
			if (i == 0)
			{
				this.rewards = Collections.EMPTY_LIST;
				return this.rewards;
			}
			
			List<ItemStack> rewards = new ArrayList();
			while (i > 0)
			{
				if (i < 9)
				{
					rewards.add(new ItemStack(Items.gold_nugget, i));
					break;
				}
				else if (i < 27)
				{
					int j = i / 9;
					rewards.add(new ItemStack(Items.gold_ingot, j));
					i -= j * 9;
				}
				else if (i < 81)
				{
					int j = i / 27;
					rewards.add(new ItemStack(Items.emerald, j));
					i -= j * 27;
				}
				else if (i < 162)
				{
					int j = i / 81;
					rewards.add(new ItemStack(Items.diamond, j));
					i -= j * 81;
				}
				else
				{
					int j = i / 162;
					rewards.add(new ItemStack(Items.experience_bottle, j));
					i -= j * 162;
				}
			}
			this.rewards = rewards;
		}
		return this.rewards;
	}
	
	public void reward(EntityPlayer player)
	{
		if (this.completed && !this.rewarded)
		{
			for (ItemStack stack : this.getRewards())
			{
				if (player.inventory.addItemStackToInventory(stack))
				{
				}
			}
			this.rewarded = true;
		}
	}
	
	public void addDescription(EntityPlayer player, List<String> lines)
	{
		lines.add(I18n.getString(this.type.getName()));
		String desc = I18n.getString(this.type.getName() + ".desc", this.amount);
		
		for (String s : CSString.cutString(desc, 20))
		{
			lines.add("\u00a77" + s);
		}
		
		if (this.completed)
		{
			lines.add("\u00a7a\u00a7o" + I18n.getString("quest.completed"));
			
			if (this.rewarded)
			{
				lines.add("\u00a7e\u00a7o" + I18n.getString("quest.rewarded"));
			}
			else
			{
				lines.add("\u00a78" + I18n.getString("quest.not_rewarded"));
			}
		}
		else
		{
			lines.add("\u00a78\u00a7o" + I18n.getString("quest.not_completed"));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Type", this.type.getName());
		nbt.setInteger("Amount", this.amount);
		nbt.setBoolean("Completed", this.completed);
		nbt.setBoolean("Rewarded", this.rewarded);
	}
	
	public void writeToBuffer(PacketBuffer buffer)
	{
		try
		{
			buffer.writeStringToBuffer(this.type.getName());
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		buffer.writeInt(this.amount);
		buffer.writeBoolean(this.completed);
		buffer.writeBoolean(this.rewarded);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.type = QuestType.get(nbt.getString("Type"));
		this.amount = nbt.getInteger("Amount");
		this.completed = nbt.getBoolean("Completed");
		this.rewarded = nbt.getBoolean("Rewarded");
	}
	
	public void readFromBuffer(PacketBuffer buffer)
	{
		try
		{
			this.type = QuestType.get(buffer.readStringFromBuffer(0xFFFF));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		this.amount = buffer.readInt();
		this.completed = buffer.readBoolean();
		this.rewarded = buffer.readBoolean();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.amount;
		result = prime * result + (this.completed ? 1231 : 1237);
		result = prime * result + (this.rewarded ? 1231 : 1237);
		result = prime * result + (this.type == null ? 0 : this.type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Quest other = (Quest) obj;
		if (this.amount != other.amount)
		{
			return false;
		}
		if (this.completed != other.completed)
		{
			return false;
		}
		if (this.rewarded != other.rewarded)
		{
			return false;
		}
		if (this.type == null)
		{
			if (other.type != null)
			{
				return false;
			}
		}
		else if (!this.type.equals(other.type))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Quest [type=").append(this.type);
		builder.append(", amount=").append(this.amount);
		builder.append(", completed=").append(this.completed);
		builder.append(", rewarded=").append(this.rewarded);
		builder.append("]");
		return builder.toString();
	}
}
