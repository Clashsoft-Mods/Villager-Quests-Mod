package clashsoft.mods.avi.client.gui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import clashsoft.mods.avi.AdvancedVillagerInteraction;
import clashsoft.mods.avi.api.IQuestProvider;
import clashsoft.mods.avi.entity.EntityVillager2;
import clashsoft.mods.avi.inventory.ContainerAdvancedVillager;
import clashsoft.mods.avi.network.PacketSetRecipe;
import clashsoft.mods.avi.network.PacketShuffleQuests;
import clashsoft.mods.avi.quest.Quest;
import clashsoft.mods.avi.quest.QuestType;

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
	
	public static ResourceLocation	questBackground	= new ResourceLocation("avi", "textures/gui/container/villager_quests.png");
	public static ResourceLocation	tradeBackground	= new ResourceLocation("avi", "textures/gui/container/villager_trading.png");
	
	public EntityVillager2			theVillager;
	public int						currentRecipeIndex;
	public String					name;
	
	public MerchantButton			nextRecipeButton;
	public MerchantButton			prevRecipeButton;
	public GuiButton				shuffleQuestsButton;
	public GuiButton				questReward;
	public GuiButtonTradeMode		switchModeButton;
	
	public GuiVillager2(InventoryPlayer inventory, EntityVillager2 merchant, World world, String name)
	{
		super(new ContainerAdvancedVillager(inventory, merchant, world));
		this.theVillager = merchant;
		this.name = name != null && name.length() >= 1 ? name : StatCollector.translateToLocal("entity.Villager.name");
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		
		this.buttonList.add(this.switchModeButton = new GuiButtonTradeMode(0, this.guiLeft + 116, this.guiTop + 6));
		this.switchModeButton.questMode = this.questMode;
		
		if (this.questMode)
		{
			this.buttonList.add(this.shuffleQuestsButton = new GuiButton(1, this.guiLeft + 125, this.guiTop + 35, 45, 20, "Shuffle"));
			this.buttonList.add(this.questReward = new GuiButton(2, this.guiLeft + 125, this.guiTop + 55, 45, 20, "Reward"));
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
		this.fontRendererObj.drawString(this.name, 60 - this.fontRendererObj.getStringWidth(this.name) / 2, this.questMode ? 6 : 10, 4210752);
		if (!this.questMode)
		{
			this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 94, 4210752);
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
			AdvancedVillagerInteraction.netHandler.sendToServer(new PacketShuffleQuests(this.theVillager));
		}
		
		if (flag)
		{
			((ContainerAdvancedVillager) this.inventorySlots).setCurrentRecipeIndex(this.currentRecipeIndex);
			AdvancedVillagerInteraction.netHandler.sendToServer(new PacketSetRecipe(this.currentRecipeIndex));
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
			int l = this.guiTop + 18;
			
			for (Quest quest : this.theVillager.getQuests())
			{
				this.fontRendererObj.drawStringWithShadow(quest.getName(), k, l, 0xFFFFFF);
				
				this.mc.getTextureManager().bindTexture(questBackground);
				int icon = QuestType.rewardIcon(quest.getReward());
				this.drawTexturedModalRect(k, l, 109 + icon * 12, 166, 12, 12);
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
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime)
	{
		super.drawScreen(mouseX, mouseY, partialTickTime);
		
		if (this.questMode)
		{
			
		}
		else
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
				
				itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input1, guiLeft + 8, guiTop + 24);
				itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input1, guiLeft + 8, guiTop + 24);
				if (input2 != null)
				{
					itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input2, guiLeft + 34, guiTop + 24);
					itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), input2, guiLeft + 34, guiTop + 24);
				}
				itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), output, guiLeft + 92, guiTop + 24);
				itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), output, guiLeft + 92, guiTop + 24);
				
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
