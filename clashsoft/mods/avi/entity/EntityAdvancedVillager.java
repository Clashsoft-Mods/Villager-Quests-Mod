package clashsoft.mods.avi.entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clashsoft.mods.avi.AdvancedVillagerInteraction;
import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.quest.Quest;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class EntityAdvancedVillager extends EntityVillager implements IQuestProvider
{
	public List<Quest>	quests	= new ArrayList();
	public Random		questRandom;
	
	public EntityAdvancedVillager(World world)
	{
		this(world, 0);
	}
	
	public EntityAdvancedVillager(World world, int profession)
	{
		super(world, profession);
		this.questRandom = new Random(world.getSeed() ^ 29285198294861982L);
	}
	
	public void shuffleQuests()
	{
		this.quests.clear();
		for (int i = 0; i < 3; i++)
		{
			this.quests.add(Quest.random(this.questRandom));
		}
	}
	
	@Override
	public int getQuests()
	{
		return this.quests.size();
	}
	
	@Override
	public Quest getQuest(int index)
	{
		return this.quests.get(index);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		NBTTagList list = new NBTTagList();
		for (Quest quest : quests)
		{
			NBTTagCompound questNBT = new NBTTagCompound();
			quest.writeToNBT(questNBT);
			list.appendTag(questNBT);
		}
		
		nbt.setTag("Quests", list);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Quests");
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound questNBT = (NBTTagCompound) list.tagAt(i);
			Quest quest = Quest.getQuestFromNBT(questNBT);
			this.quests.add(quest);
		}
	}
	
	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer player)
	{
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = itemstack != null && itemstack.itemID == Item.monsterPlacer.itemID;
		
		if (!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking())
		{
			if (!this.worldObj.isRemote)
			{
				this.setCustomer(player);
				this.sync();
				player.openGui(AdvancedVillagerInteraction.instance, 0, this.worldObj, this.entityId, 0, 0);
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void sync()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try
		{
			NBTTagCompound nbt = new NBTTagCompound();
			this.writeEntityToNBT(nbt);
			writeNBTTagCompound(nbt, dos);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		Packet250CustomPayload packet = new Packet250CustomPayload("AVI", bos.toByteArray());
	}
	
	public void onPacket(Packet250CustomPayload packet)
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
		DataInputStream dis = new DataInputStream(bis);
		
		try
		{
			NBTTagCompound nbt = readNBTTagCompound(dis);
			this.readEntityFromNBT(nbt);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) throws IOException
	{
		if (par0NBTTagCompound == null)
		{
			par1DataOutput.writeShort(-1);
		}
		else
		{
			byte[] abyte = CompressedStreamTools.compress(par0NBTTagCompound);
			par1DataOutput.writeShort((short) abyte.length);
			par1DataOutput.write(abyte);
		}
	}
	
	public static NBTTagCompound readNBTTagCompound(DataInput par0DataInput) throws IOException
	{
		short short1 = par0DataInput.readShort();
		
		if (short1 < 0)
		{
			return null;
		}
		else
		{
			byte[] abyte = new byte[short1];
			par0DataInput.readFully(abyte);
			return CompressedStreamTools.decompress(abyte);
		}
	}
}
