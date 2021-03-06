package org.avp.world.dimension.varda;

import java.util.Random;

import org.avp.AliensVsPredator;
import org.avp.entities.living.EntityDeaconShark;
import org.avp.world.dimension.BiomeGenLV;
import org.avp.world.dimension.GenerationFilters;
import org.avp.world.dimension.WorldGenSurfaceBlock;
import org.avp.world.dimension.varda.gen.TerrainFormation;
import org.avp.world.dimension.varda.gen.TerrainFormation1;
import org.avp.world.dimension.varda.gen.VardaTallTreeGenerator;
import org.avp.world.dimension.varda.gen.VardaTree2Generator;
import org.avp.world.dimension.varda.gen.VardaTree3Generator;
import org.avp.world.dimension.varda.gen.VardaTreeGenerator;
import org.avp.world.dimension.varda.gen.WorldGenSustainableOnDirt;

import com.arisux.mdx.lib.world.Worlds;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeVarda extends BiomeGenLV
{
    public static BiomeVarda vardaBadlands = new BiomeVarda(new BiomeProperties(AliensVsPredator.dimensions().BIOME_NAME_VARDA_BADLANDS).setBaseHeight(1.0F).setHeightVariation(2.0F).setRainDisabled().setWaterColor(0xFFFF66));
    public static BiomeVarda vardaForest   = new BiomeVarda(new BiomeProperties(AliensVsPredator.dimensions().BIOME_NAME_VARDA_FOREST).setBaseHeight(1.0F).setHeightVariation(8.0F).setTemperature(0.7F).setRainfall(0.1F).setWaterColor(0xFFFF66));

    public BiomeVarda(BiomeProperties properties)
    {
        super(properties);
        this.topBlock = AliensVsPredator.blocks().unidirt.getDefaultState();
        this.fillerBlock = AliensVsPredator.blocks().unistone.getDefaultState();
        this.spawnableWaterCreatureList.add(new Biome.SpawnListEntry(EntityDeaconShark.class, 1, 0, 1));
    }

    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return this.theBiomeDecorator = new BiomeDecoratorVarda();
    }

    public static class BiomeDecoratorVarda extends BiomeDecorator
    {
        @Override
        public void decorate(World world, Random seed, Biome biome, BlockPos pos)
        {
            if (this.decorating)
            {
                throw new RuntimeException("Already decorating");
            }
            else
            {
                this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(world.getWorldInfo().getGeneratorOptions()).build();
                this.chunkPos = pos;
                this.generateOres(world, seed);
                this.genDecorations(biome, world, seed);
                this.decorating = false;
            }
        }

        @Override
        protected void genDecorations(Biome biome, World world, Random seed)
        {
            WorldGenerator stalagmites = new WorldGenSurfaceBlock(AliensVsPredator.blocks().stalagmite.getDefaultState());
            WorldGenerator formation1 = new TerrainFormation();
            WorldGenerator formation2 = new TerrainFormation1();
            
            if (biome == BiomeVarda.vardaForest)
            {
                this.generateForest(biome, world, seed);
            }

            for (int idx = 0; idx < 64; idx++)
            {
                stalagmites.generate(world, seed, Worlds.randPos(seed, this.chunkPos, 8, 80));
            }

            for (int i = 0; i < 128; i++)
            {
                WorldGenerator landform = null;

                switch (seed.nextInt(2))
                {
                    case 0:
                        landform = formation1;
                        break;
                    case 1:
                        landform = formation2;
                        break;
                }

                landform.generate(world, seed, Worlds.randChunkPos(seed, this.chunkPos));
            }
        }

        @Override
        protected void generateOres(World world, Random seed)
        {
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().unidirt.getDefaultState(), 32, GenerationFilters.STONE), seed, 20, 0, 4, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().unisand.getDefaultState(), 32, GenerationFilters.STONE), seed, 20, 0, 128, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().oreBauxite.getDefaultState(), 4, GenerationFilters.STONE), seed, 20, 16, 128, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().oreCopper.getDefaultState(), 4, GenerationFilters.STONE), seed, 20, 0, 128, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().oreSilicon.getDefaultState(), 4, GenerationFilters.STONE), seed, 15, 0, 64, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(AliensVsPredator.blocks().oreLithium.getDefaultState(), 3, GenerationFilters.STONE), seed, 1, 1, 48, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(Blocks.COAL_ORE.getDefaultState(), 16, GenerationFilters.STONE), seed, 20, 0, 128, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(Blocks.IRON_ORE.getDefaultState(), 8, GenerationFilters.STONE), seed, 20, 0, 64, this.chunkPos);
            Worlds.generateInChunk(world, new WorldGenMinable(Blocks.DIAMOND_ORE.getDefaultState(), 3, GenerationFilters.STONE), seed, 1, 0, 16, this.chunkPos);
        }

        private void generateForest(Biome biome, World world, Random seed)
        {
            WorldGenerator saplings = new WorldGenSustainableOnDirt(AliensVsPredator.blocks().gigerSapling.getDefaultState());
            WorldGenerator tree1 = new VardaTreeGenerator(true);
            WorldGenerator tree2 = new VardaTree2Generator(true);
            WorldGenerator tree3 = new VardaTree3Generator(true);
            WorldGenerator treeTall = new VardaTallTreeGenerator(true);
            WorldGenerator gooPools = new WorldGenLakes(AliensVsPredator.blocks().blackgoo);
            
            for (int i = 0; i < 2; i++)
            {
                gooPools.generate(world, seed, Worlds.randChunkPos(seed, this.chunkPos));
            }

            for (int idx = 0; idx < 192; idx++)
            {
                WorldGenerator tree = null;

                switch (seed.nextInt(4))
                {
                    case 0:
                        tree = tree1;
                        break;
                    case 1:
                        tree = tree2;
                        break;
                    case 2:
                        tree = tree3;
                        break;
                    case 3:
                        tree = treeTall;
                        break;
                }

                if (tree != null)
                {
                    tree.generate(world, seed, Worlds.randChunkPos(seed, this.chunkPos));
                }
            }

            for (int idx = 0; idx < 64; idx++)
            {
                saplings.generate(world, seed, Worlds.randChunkPos(seed, this.chunkPos));
            }
        }
    }
}
