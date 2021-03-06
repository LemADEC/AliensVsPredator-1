package org.avp.world.dimension.acheron;

import java.util.Random;

import org.avp.AliensVsPredator;
import org.avp.world.dimension.BiomeGenLV;

import com.arisux.mdx.lib.world.Worlds;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BiomeAcheron extends BiomeGenLV
{
    public static final BiomeAcheron acheron = new BiomeAcheron(new BiomeProperties("Acheron").setBaseHeight(0.1F).setHeightVariation(0.4F).setTemperature(0.5F).setRainDisabled());

    public BiomeAcheron(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = AliensVsPredator.blocks().unidirt.getDefaultState();
        this.fillerBlock = AliensVsPredator.blocks().unistone.getDefaultState();
    }
    
    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return this.theBiomeDecorator = new BiomeDecoratorAcheron();
    }
    
    public static class BiomeDecoratorAcheron extends BiomeDecorator
    {
        @Override
        public void decorate(World world, Random random, Biome biome, BlockPos pos)
        {
            if (this.decorating)
            {
                throw new RuntimeException("Already decorating");
            }
            else
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
                this.chunkPos = pos;
                this.generateOres(world, random);
                this.genDecorations(biome, world, random);
                this.decorating = false;
            }
        }

        @Override
        protected void genDecorations(Biome biome, World world, Random random)
        {
            ;
        }

        @Override
        protected void generateOres(World world, Random random)
        {
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().unidirt.getDefaultState(), 32), random, 20, 0, 128, chunkPos);
        }
    }
}
