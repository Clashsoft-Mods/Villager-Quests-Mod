package clashsoft.mods.villagerquests.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import clashsoft.cslib.minecraft.lang.I18n;
import clashsoft.mods.villagerquests.VillagerQuestsMod;
import clashsoft.mods.villagerquests.entity.EntityVillager2;
import clashsoft.mods.villagerquests.inventory.ContainerVillager2;
import clashsoft.mods.villagerquests.network.PacketRefreshQuests;
import clashsoft.mods.villagerquests.network.PacketRewardQuests;
import clashsoft.mods.villagerquests.network.PacketSetRecipe;
import clashsoft.mods.villagerquests.network.PacketShuffleQuests;
import clashsoft.mods.villagerquests.quest.IQuestProvider;
import clashsoft.mods.villagerquests.quest.Quest;
import clashsoft.mods.villagerquests.quest.type.QuestType;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class GuiVillager2 extends GuiContainer
{
	public boolean					questMode		= false;
	
	public static ResourceLocation	questBackground	= new ResourceLocation("villagerquests", "textures/gui/container/villager_quests.png");
	public static ResourceLocation	tradeBackground	= new ResourceLocation("villagerquests", "textures/gui/container/villager_trading.png");
	
	public EntityVillager2			theVillager;
	public int						currentRecipeIndex;
	public String					name;
	
	public ContainerVillager2		villagerContainer;
	
	public MerchantButton			nextRecipeButton;
	public MerchantButton			prevRecipeButton;
	public GuiButton				shuffleQuestsButton;
	public GuiButton				rewardButton;
	public GuiButtonTradeMode		switchModeButton;
	
	public GuiVillager2(InventoryPlayer inventory, EntityVillager2 merchant, World world, String name)
	{
		super(new ContainerVillager2(inventory, merchant, world));
		this.villagerContainer = (ContainerVillager2) this.inventorySlots;
		this.theVillager = merchant;
		this.name = name != null && !name.isEmpty() ? name : StatCollector.translateToLocal("entity.Villager.name");
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		
		this.buttonList.add(this.switchModeButton = new GuiButtonTradeMode(0, this.guiLeft + 117, this.guiTop + 6));
		this.switchModeButton.questMode = this.questMode;
		this.villagerContainer.setQuestMode(this.questMode);
		
		if (this.questMode)
		{
			if (this.mc.thePlayer.capabilities.isCreativeMode)
			{
				this.buttonList.add(this.shuffleQuestsButton = new GuiButton(1, this.guiLeft + 119, this.guiTop + 37, 50, 20, "Shuffle"));
			}
			this.buttonList.add(this.rewardButton = new GuiButton(2, this.guiLeft + 119, this.guiTop + 59, 50, 20, "Reward"));
			
			VillagerQuestsMod.instance.netHandler.sendToServer(new PacketRefreshQuests(this.theVillager));
		}
		else
		{
			this.buttonList.add(this.nextRecipeButton = new MerchantButton(1, this.guiLeft + 102, this.guiTop + 4, true));
			this.buttonList.add(this.prevRecipeButton = new MerchantButton(2, this.guiLeft + 6, this.guiTop + 4, false));
			this.nextRecipeButton.enabled = false;
			this.prevRecipeButton.enabled = false;
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRendererObj.drawString(this.name, 60 - this.fontRendererObj.getStringWidth(this.name) / 2, 8, 4210752);
		if (!this.questMode)
		{
			this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
		}
		
		if (this.switchModeButton.func_146115_a())
		{
			this.drawCreativeTabHoveringText(I18n.getString(this.questMode ? "button.trademode" : "button.questmode"), mouseX - this.guiLeft, mouseY - this.guiTop);
		}
		
		int k = this.guiLeft + 8;
		int l = this.guiTop + 21;
		
		if (this.questMode)
		{
			for (Quest quest : this.theVillager.getQuests())
			{
				int mx = mouseX + this.guiLeft;
				int my = mouseY + this.guiTop;
				
				if (this.func_146978_c(k + 92, l + 4, 12, 12, mx, my))
				{
					List<ItemStack> reward = quest.getRewards();
					if (reward != null && !reward.isEmpty())
					{
						List<String> lines = new ArrayList();
						
						lines.add(I18n.getString("quest.reward"));
						for (ItemStack stack : reward)
						{
							String s = String.format("\u00a77%dx %s", stack.stackSize, stack.getDisplayName());
							lines.add(s);
						}
						this.drawHoveringText(lines, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRendererObj);
						
						l += 19;
						continue;
					}
				}
				else if (this.func_146978_c(k + 3, l + 3, 13, 13, mx, my))
				{
					List<String> lines = Arrays.asList(I18n.getString("quest.completion", quest.getCompletion() * 100F));
					this.drawHoveringText(lines, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRendererObj);
				}
				else if (this.func_146978_c(k, l, 108, 18, mx, my))
				{
					List<String> lines = new ArrayList();
					quest.addDescription(this.mc.thePlayer, lines);
					this.drawHoveringText(lines, mouseX - this.guiLeft, mouseY - this.guiTop, this.fontRendererObj);
				}
				l += 19;
			}
		}
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if (!this.questMode)
		{
			MerchantRecipeList recipeList = this.theVillager.getRecipes(this.mc.thePlayer);
			if (recipeList != null)
			{
				this.nextRecipeButton.enabled = this.currentRecipeIndex < recipeList.size() - 1;
				this.prevRecipeButton.enabled = this.currentRecipeIndex > 0;
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		boolean flag = false;
		
		if (button == this.switchModeButton)
		{
			this.questMode = !this.questMode;
			this.initGui();
		}
		else if (button == this.nextRecipeButton)
		{
			++this.currentRecipeIndex;
			flag = true;
		}
		else if (button == this.prevRecipeButton)
		{
			--this.currentRecipeIndex;
			flag = true;
		}
		else if (button == this.shuffleQuestsButton)
		{
			VillagerQuestsMod.instance.netHandler.sendToServer(new PacketShuffleQuests(this.theVillager));
		}
		else if (button == this.rewardButton)
		{
			VillagerQuestsMod.instance.netHandler.sendToServer(new PacketRewardQuests(this.theVillager));
		}
		
		if (flag)
		{
			this.villagerContainer.setCurrentRecipeIndex(this.currentRecipeIndex);
			VillagerQuestsMod.instance.netHandler.sendToServer(new PacketSetRecipe(this.currentRecipeIndex));
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.questMode ? questBackground : tradeBackground);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		if (this.questMode)
		{
			int k = this.guiLeft + 8;
			int l = this.guiTop + 21;
			
			for (Quest quest : this.theVillager.getQuests())
			{
				GL11.glColor4f(1F, 1F, 1F, 1F);
				this.mc.getTextureManager().bindTexture(questBackground);
				int color;
				
				if (quest.isRewarded())
				{
					this.drawTexturedModalRect(k, l, 0, 185, 108, 19);
					color = 0xA4A4A4;
				}
				else
				{
					if (this.func_146978_c(k, l, 108, 18, mouseX + this.guiLeft, mouseY + this.guiTop))
					{
						this.drawTexturedModalRect(k, l, 0, 223, 108, 19);
						color = 0xFFFF80;
					}
					else
					{
						this.drawTexturedModalRect(k, l, 0, 204, 108, 19);
						color = 0xFFFFFF;
					}
					
					if (quest.isCompleted())
					{
						color = 0x80FF80;
					}
				}
				
				float completion = quest.getCompletion();
				int completion1 = (int) (completion * 13);
				int completion2 = 13 - completion1;
				
				this.drawTexturedModalRect(k + 3, l + 3, 176, 116, 13, completion2);
				this.drawTexturedModalRect(k + 3, l + 3 + completion2, 189, 116 + completion2, 13, completion1);
				
				int icon = QuestType.rewardIcon(quest.getReward());
				if (icon != -1)
				{
					this.drawTexturedModalRect(k + 92, l + 4, 108 + icon * 12, 166, 12, 12);
				}
				
				this.fontRendererObj.drawStringWithShadow(I18n.getString(quest.getName()), k + 19, l + 5, color);
				l += 19;
			}
		}
		else
		{
			MerchantRecipeList merchantrecipelist = this.theVillager.getRecipes(this.mc.thePlayer);
			if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
			{
				int i1 = this.currentRecipeIndex;
				MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(i1);
				
				if (merchantrecipe.isRecipeDisabled())
				{
					this.mc.getTextureManager().bindTexture(tradeBackground);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_LIGHTING);
					this.drawTexturedModalRect(this.guiLeft + 55, this.guiTop + 21, 212, 0, 28, 21);
					this.drawTexturedModalRect(this.guiLeft + 55, this.guiTop + 51, 212, 0, 28, 21);
				}
			}
		}
	}
	
	public void drawQuestHoveringText(Quest quest, int x, int y)
	{
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime)
	{
		super.drawScreen(mouseX, mouseY, partialTickTime);
		
		if (!this.questMode)
		{
			MerchantRecipeList merchantrecipelist = this.theVillager.getRecipes(this.mc.thePlayer);
			if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
			{
				int i1 = this.currentRecipeIndex;
				
				MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(i1);
				ItemStack input1 = merchantrecipe.getItemToBuy();
				ItemStack input2 = merchantrecipe.getSecondItemToBuy();
				ItemStack output = merchantrecipe.getItemToSell();
				
				GL11.glPushMatrix();
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);
				GL11.glEnable(GL11.GL_LIGHTING);
				itemRender.zLevel = 100.0F;
				
				itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input1, this.guiLeft + 8, this.guiTop + 24);
				itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input1, this.guiLeft + 8, this.guiTop + 24);
				if (input2 != null)
				{
					itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input2, this.guiLeft + 34, this.guiTop + 24);
					itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input2, this.guiLeft + 34, this.guiTop + 24);
				}
				itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), output, this.guiLeft + 92, this.guiTop + 24);
				itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), output, this.guiLeft + 92, this.guiTop + 24);
				
				itemRender.zLevel = 0.0F;
				GL11.glDisable(GL11.GL_LIGHTING);
				
				if (this.func_146978_c(8, 24, 16, 16, mouseX, mouseY))
				{
					this.renderToolTip(input1, mouseX, mouseY);
				}
				else if (input2 != null && this.func_146978_c(34, 24, 16, 16, mouseX, mouseY))
				{
					this.renderToolTip(input2, mouseX, mouseY);
				}
				else if (this.func_146978_c(92, 24, 16, 16, mouseX, mouseY))
				{
					this.renderToolTip(output, mouseX, mouseY);
				}
				
				GL11.glPopMatrix();
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				RenderHelper.enableStandardItemLighting();
			}
		}
	}
	
	public IQuestProvider getVillager()
	{
		return this.theVillager;
	}
}
