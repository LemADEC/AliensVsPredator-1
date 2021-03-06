package org.avp.client.render.tile;

import org.avp.tile.TileEntitySupplyCrate;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.arisux.mdx.lib.client.render.OpenGL;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderSupplyCrate extends TileEntitySpecialRenderer<TileEntitySupplyCrate>
{
    @Override
    public void renderTileEntityAt(TileEntitySupplyCrate tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        OpenGL.pushMatrix();
        {
            float scale = 1F;
            GlStateManager.disableCull();
            OpenGL.translate(x + 0.5F, y + 1.5F, z + 0.5F);
            OpenGL.enable(GL12.GL_RESCALE_NORMAL);
            OpenGL.scale(scale, -scale, scale);
            OpenGL.enable(GL11.GL_ALPHA_TEST);
            OpenGL.disableCullFace();
            OpenGL.rotate(tile);
            
            tile.getType().getModel().bindTexture();
            tile.getType().getModel().getModel().drawCrate();
            OpenGL.enableCullFace();
        }
        OpenGL.popMatrix();
    }
}
