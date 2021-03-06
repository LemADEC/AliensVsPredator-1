package org.avp.block;

import org.avp.AliensVsPredator;
import org.avp.tile.TileEntityNetworkRack;

import com.arisux.mdx.lib.world.entity.Entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockNetworkRack extends Block
{
    public BlockNetworkRack(Material material)
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
        return new TileEntityNetworkRack();
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityNetworkRack)
        {
            TileEntityNetworkRack rack = (TileEntityNetworkRack) tile;

            if (rack.isChild())
            {
                if (rack.getParent() != null)
                {
                    world.setBlockToAir(rack.getParent().getPos());
                    rack.getParent().breakChildren(world);
                }
            }
            else
            {
                rack.breakChildren(world);
            }
        }

        world.removeTileEntity(pos);

        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityNetworkRack && placer != null)
        {
            TileEntityNetworkRack rack = (TileEntityNetworkRack) tile;

            rack.setRotationYAxis(Entities.getEntityFacingRotY(placer));

            if (!rack.setup(world, true))
            {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityNetworkRack)
        {
            TileEntityNetworkRack rackSubBlock = (TileEntityNetworkRack) tile;
            TileEntityNetworkRack rack = null;

            if (rackSubBlock.isChild() && rackSubBlock.getParent() != null)
            {
                rack = rackSubBlock.getParent();
            }
            else if (rackSubBlock.isParent())
            {
                rack = rackSubBlock;
            }

            showNetworkRackGUI(playerIn, rack);
        }

        return true;
    }

    public static void showNetworkRackGUI(EntityPlayer player, TileEntityNetworkRack rack)
    {
        if (!player.world.isRemote)
        {
            FMLNetworkHandler.openGui(player, AliensVsPredator.instance(), AliensVsPredator.interfaces().GUI_NETWORK_RACK, player.world, rack.getPos().getX(), rack.getPos().getY(), rack.getPos().getZ());
        }
    }
}
