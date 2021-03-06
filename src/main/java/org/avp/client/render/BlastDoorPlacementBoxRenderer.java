package org.avp.client.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.avp.tile.TileEntityBlastdoor;

import com.arisux.mdx.lib.game.Game;
import com.arisux.mdx.lib.world.entity.Entities;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlastDoorPlacementBoxRenderer
{
    public static final BlastDoorPlacementBoxRenderer instance = new BlastDoorPlacementBoxRenderer();

    public BlastDoorPlacementBoxRenderer()
    {
        ;
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        EntityPlayer p = Minecraft.getMinecraft().player;
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buff = tess.getBuffer();

        if (Game.minecraft().gameSettings.thirdPersonView == 0)
        {
            ItemStack stack = Game.minecraft().player.getHeldItemMainhand();

            if (stack != null)
            {
                Block block = Block.getBlockFromItem(stack.getItem());

                if (block != null && block.hasTileEntity(block.getDefaultState()))
                {
                    TileEntity t = block.createTileEntity(Game.minecraft().world, block.getDefaultState());

                    if (t instanceof TileEntityBlastdoor)
                    {
                        TileEntityBlastdoor bd = (TileEntityBlastdoor) t;
                        List<BlockPos> blocks = new ArrayList<BlockPos>(Arrays.asList(bd.setFor(Entities.getEntityFacingRotY(p))));
                        blocks.add(new BlockPos(0, 0, 0));

                        Vec3d hitVec = Game.minecraft().objectMouseOver.hitVec;

                        double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * (double) event.getPartialTicks();
                        double y = p.lastTickPosY + (p.posY - p.lastTickPosY) * (double) event.getPartialTicks();
                        double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double) event.getPartialTicks();

                        double locationX = Math.floor(hitVec.xCoord);
                        double locationY = Math.floor(hitVec.yCoord);
                        double locationZ = Math.floor(hitVec.zCoord);

                        double placeX = locationX - x;
                        double placeY = locationY - y;
                        double placeZ = locationZ - z;

                        float r = 1.0F;
                        float g = 0.0F;
                        float b = 0.2F;
                        float a = 0.6F;

                        boolean validPlacementPosition = true;

                        for (BlockPos pos : blocks)
                        {
                            if (p.world.getBlockState(pos.add(locationX, locationY, locationZ)).getBlock() != Blocks.AIR)
                            {
                                validPlacementPosition = false;
                            }
                        }

                        if (validPlacementPosition)
                        {
                            r = 0.4F;
                            g = 1.0F;
                            b = 0.0F;
                            a = 0.6F;
                        }

                        GlStateManager.disableTexture2D();
                        GlStateManager.glLineWidth(2.0F);
                        GlStateManager.enableBlend();

                        for (BlockPos pos : blocks)
                        {
                            double cubeX = placeX + pos.getX();
                            double cubeY = placeY + pos.getY();
                            double cubeZ = placeZ + pos.getZ();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX, cubeY + 0.0D, cubeZ).color(r, g, b, a).endVertex();
                            buff.pos(cubeX, cubeY + 1.0D, cubeZ).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 0.0D).color(r, g, b, a).endVertex();
                            tess.draw();

                            buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                            buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(r, g, b, a).endVertex();
                            tess.draw();
                        }

                        GlStateManager.glLineWidth(1.0F);
                        GlStateManager.enableTexture2D();
                    }
                }
            }
        }
    }
}
