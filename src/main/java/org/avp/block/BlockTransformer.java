package org.avp.block;

import java.util.ArrayList;

import org.avp.AliensVsPredator;
import org.avp.packets.client.PacketRotateRotatable;
import org.avp.tile.TileEntityTransformer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockTransformer extends Block
{
    public BlockTransformer(Material material)
    {
        super(material);
        this.setTickRandomly(true);
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityTransformer();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te != null && te instanceof TileEntityTransformer)
        {
            TileEntityTransformer transformer = (TileEntityTransformer) te;

            ArrayList<EnumFacing> EnumFacings = new ArrayList<EnumFacing>();

            for (EnumFacing dir : EnumFacing.VALUES)
            {
                if (dir != EnumFacing.UP && dir != EnumFacing.DOWN)
                {
                    EnumFacings.add(dir);
                }
            }

            if (transformer.getRotationYAxis() != null)
            {
                int index = EnumFacings.indexOf(transformer.getRotationYAxis());

                if (index + 1 >= EnumFacings.size())
                {
                    index = -1;
                }

                if (EnumFacings.get(index + 1) != null)
                {
                    transformer.setRotationYAxis(EnumFacings.get(index + 1));
                }

                if (!world.isRemote)
                {
                    AliensVsPredator.network().sendToAll(new PacketRotateRotatable(transformer.getRotationYAxis().ordinal(), transformer.getPos().getX(), transformer.getPos().getY(), transformer.getPos().getZ()));
                }
            }

            transformer.getUpdatePacket();
        }
        return super.onBlockActivated(world, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }
}
