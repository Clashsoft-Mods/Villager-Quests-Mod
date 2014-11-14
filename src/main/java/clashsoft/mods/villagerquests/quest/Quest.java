package clashsoft.mods.villagerquests.quest;

import java.io.IOException;
import java.util.*;

import clashsoft.cslib.minecraft.CSLib;
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
	private QuestType		type;
	
	protected float			amount;
	protected float			maxAmount;
	private float			completion	= -1F;
	private EntityPlayer	completedBy;
	
	private boolean			rewarded;
	private List<ItemStack>	rewards;
	
	public Quest()
	{
	}
	
	public Quest(QuestType type, float maxAmount)
	{
		this.type = type;
		this.maxAmount = maxAmount;
	}
	
	public static Quest random(Random seed)
	{
		QuestType type = QuestType.random(seed);
		float amount = type.getRandomAmount(seed);
		return new Quest(type, amount);
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
			f = this.type.getReward(this.maxAmount);
		}
		else
		{
			f = this.type.getReward();
		}
		return (int) f;
	}
	
	public boolean isCompleted()
	{
		return this.completion >= 1F && this.completedBy != null;
	}
	
	public boolean isCompleted(EntityPlayer player)
	{
		return this.completion >= 1F && (this.completedBy == player || this.completedBy.getUniqueID().equals(player.getUniqueID()));
	}
	
	public float getCompletion()
	{
		return this.completion;
	}
	
	public boolean isRewarded()
	{
		return this.rewarded;
	}
	
	public boolean hasAmount()
	{
		return this.type.hasAmount();
	}
	
	public void addAmount(EntityPlayer player, float amount)
	{
		this.amount += amount;
		this.updateCompletion(player);
	}
	
	public boolean checkCompleted(EntityPlayer player)
	{
		this.updateCompletion(player);
		return this.completedBy == player;
	}
	
	public float updateCompletion(EntityPlayer player)
	{
		if (player == null)
		{
			return 0F;
		}
		
		this.completion = this.amount / this.maxAmount;
		if (this.completion < 1F)
		{
			this.rewarded = false;
		}
		else
		{
			this.amount = this.maxAmount;
			this.completedBy = player;
		}
		return this.completion;
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
		if (this.isCompleted(player) && !this.rewarded)
		{
			for (ItemStack stack : this.getRewards())
			{
				player.inventory.addItemStackToInventory(stack);
			}
			this.rewarded = true;
		}
	}
	
	public void addDescription(EntityPlayer player, List<String> lines)
	{
		String name = I18n.getString(this.type.getName());
		String desc = I18n.getString(this.type.getName() + ".desc", this.maxAmount, this.maxAmount - this.amount);
		
		lines.add(name);
		for (String s : CSString.cutString(desc, name.length() + 10))
		{
			lines.add("\u00a77" + s);
		}
		
		// Completed by this player
		if (this.isCompleted(player))
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
		// Completed by someone else
		else if (this.isCompleted())
		{
			lines.add("\u00a7c" + I18n.getString("quest.completed_by", this.completedBy.getCommandSenderName()));
		}
		// Not completed
		else
		{
			lines.add("\u00a78\u00a7o" + I18n.getString("quest.not_completed"));
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Type", this.type.getName());
		nbt.setFloat("Amount", this.amount);
		nbt.setFloat("MaxAmount", this.maxAmount);
		nbt.setFloat("Completion", this.completion);
		nbt.setBoolean("Rewarded", this.rewarded);
		
		if (this.completedBy != null)
		{
			nbt.setString("CompletedBy", this.completedBy.getUniqueID().toString());
		}
	}
	
	public void writeToBuffer(PacketBuffer buffer) throws IOException
	{
		buffer.writeStringToBuffer(this.type.getName());
		buffer.writeFloat(this.amount);
		buffer.writeFloat(this.maxAmount);
		buffer.writeFloat(this.completion);
		buffer.writeBoolean(this.rewarded);
		buffer.writeStringToBuffer(this.completedBy == null ? "" : this.completedBy.getUniqueID().toString());
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.type = QuestType.get(nbt.getString("Type"));
		this.amount = nbt.getFloat("Amount");
		this.maxAmount = nbt.getFloat("MaxAmount");
		this.completion = nbt.getFloat("Completion");
		this.rewarded = nbt.getBoolean("Rewarded");
		
		if (nbt.hasKey("CompletedBy"))
		{
			this.completedBy = CSLib.proxy.findPlayer(UUID.fromString(nbt.getString("CompletedBy")));
		}
	}
	
	public void readFromBuffer(PacketBuffer buffer) throws IOException
	{
		this.type = QuestType.get(buffer.readStringFromBuffer(0xFF));
		this.amount = buffer.readFloat();
		this.maxAmount = buffer.readFloat();
		this.completion = buffer.readFloat();
		this.rewarded = buffer.readBoolean();
		
		String s = buffer.readStringFromBuffer(0xFF);
		if (!s.isEmpty())
		{
			this.completedBy = CSLib.proxy.findPlayer(UUID.fromString(s));
		}
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.amount);
		result = prime * result + Float.floatToIntBits(this.maxAmount);
		result = prime * result + Float.floatToIntBits(this.completion);
		result = prime * result + (this.rewarded ? 1231 : 1237);
		result = prime * result + (this.type == null ? 0 : this.type.hashCode());
		result = prime * result + (this.completedBy == null ? 0 : this.completedBy.hashCode());
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
		if (this.maxAmount != other.maxAmount)
		{
			return false;
		}
		if (this.completion != other.completion)
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
		if (this.completedBy == null)
		{
			if (other.completedBy != null)
			{
				return false;
			}
		}
		else if (!this.completedBy.equals(other.completedBy))
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
		builder.append(", maxAmount=").append(this.maxAmount);
		builder.append(", completion=").append(this.completion);
		builder.append(", rewarded=").append(this.rewarded);
		builder.append(", completedBy=").append(this.completedBy);
		builder.append("]");
		return builder.toString();
	}
}
