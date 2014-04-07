package clashsoft.mods.avi.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class MerchantButton extends GuiButton
{
	private final boolean	field_146157_o;
	
	public MerchantButton(int id, int x, int y, boolean paramBoolean)
	{
		super(id, x, y, 12, 19, "");
		this.field_146157_o = paramBoolean;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (!(this.visible))
			return;
		
		mc.getTextureManager().bindTexture(GuiVillager2.tradeBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int i = ((mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width) && (mouseY < this.yPosition + this.height)) ? 1 : 0;
		int j = 0;
		int k = 176;
		
		if (!(this.enabled))
		{
			k += this.width * 2;
		}
		else if (i != 0)
		{
			k += this.width;
		}
		if (!(this.field_146157_o))
		{
			j += this.height;
		}
		
		this.drawTexturedModalRect(this.xPosition, this.yPosition, k, j, this.width, this.height);
	}
}
