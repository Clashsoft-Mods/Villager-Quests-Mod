package clashsoft.mods.avi.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonTradeMode extends GuiButton
{
	public boolean	questMode;
	
	public GuiButtonTradeMode(int id, int x, int y)
	{
		super(id, x, y, 54, 29, "");
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (this.visible)
		{
			this.field_146123_n = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height));
			
			mc.getTextureManager().bindTexture(GuiVillager2.questBackground);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			int i = this.questMode ? 29 : 0;
			if (this.field_146123_n)
			{
				i += 58;
			}
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 176, i, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}
