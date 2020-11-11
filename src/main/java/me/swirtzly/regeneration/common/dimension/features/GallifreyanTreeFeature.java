package me.swirtzly.regeneration.common.dimension.features;

import com.mojang.datafixers.Dynamic;
import me.swirtzly.regeneration.Regeneration;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Random;
import java.util.function.Function;

/**
 * Created by Swirtzly
 * on 30/04/2020 @ 11:10
 */
public class GallifreyanTreeFeature extends Feature<NoFeatureConfig> {

    public GallifreyanTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    private final ResourceLocation[] TREE_LIST = new ResourceLocation[]{
            createTreeLocation("gal_normal_large"),
            createTreeLocation("gal_pine_normal"),
            createTreeLocation("gal_skinny_large"),
            createTreeLocation("gal_skinny_lollypop"),
            createTreeLocation("gal_small_lollypop"),
            createTreeLocation("gal_tree_pine_large")
    };

    public static ResourceLocation createTreeLocation(String name) {
        return new ResourceLocation(Regeneration.MODID, "gallifrey/trees/" + name);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (worldIn instanceof WorldGenRegion) {
            WorldGenRegion reg = (WorldGenRegion) worldIn;
            Template temp = reg.getWorld().getStructureTemplateManager().getTemplate(TREE_LIST[rand.nextInt(TREE_LIST.length)]);
            if (temp != null) {
                pos = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
                PlacementSettings set = new PlacementSettings().addProcessor(BlockIgnoreStructureProcessor.AIR);
                if (worldIn.getBlockState(pos.down()).getBlock() == Blocks.GRASS_BLOCK) {
                    temp.addBlocksToWorld(reg, pos, set);
                }
                return true;
            }
        }
        return false;
    }
}
