package org.avp.client.render.tile;

import org.avp.AliensVsPredator;
import org.avp.tile.TileEntityTransformer;

import com.arisux.mdx.lib.client.render.OpenGL;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;


public class RenderTransformer extends TileEntitySpecialRenderer<TileEntityTransformer>
{
    @Override
    public void renderTileEntityAt(TileEntityTransformer tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (tile != null && tile instanceof TileEntityTransformer)
        {
            TileEntityTransformer transformer = (TileEntityTransformer) tile;

            OpenGL.pushMatrix();
            {
                GlStateManager.disableCull();
                OpenGL.translate(x, y, z);
                OpenGL.scale(1F, -1F, 1F);
                OpenGL.translate(0.5F, -1.5F, 0.5F);

                if (transformer.getRotationYAxis() == EnumFacing.EAST)
                {
                    OpenGL.rotate(90F, 0F, 1F, 0F);
                }

                if (transformer.getRotationYAxis() == EnumFacing.WEST)
                {
                    OpenGL.rotate(-90F, 0F, 1F, 0F);
                }

                if (transformer.getRotationYAxis() == EnumFacing.NORTH)
                {
                    OpenGL.rotate(180F, 0F, 1F, 0F);
                }

                if (transformer.getRotationYAxis() == EnumFacing.SOUTH)
                {
                    OpenGL.rotate(0F, 0F, 1F, 0F);
                }

                AliensVsPredator.resources().models().TRANSFORMER.draw(transformer);
            }
            OpenGL.popMatrix();
        }
    }
}
