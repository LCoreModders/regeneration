package me.swirtzly.regeneration.common.world.structures;

import java.util.Random;
import java.util.function.Function;

import org.apache.logging.log4j.Level;

import com.mojang.datafixers.Dynamic;

import me.swirtzly.regeneration.Regeneration;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class HutStructure extends Structure<ProbabilityConfig>{

    
    public HutStructure(Function<Dynamic<?>, ? extends ProbabilityConfig> p_i51427_1_) {
        super(p_i51427_1_);
    }

    @Override
    public IStartFactory getStartFactory() {
        return HutStructure.Start::new;
    }
    
    /*
     * The structure name to show in the /locate command. 
     * 
     * Make sure this matches what the resourcelocation of your structure will be
     * because if you don't add the MODID: part, Minecraft will put minecraft: in front 
     * of the name instead and we don't want that. We want our structure to have our
     *  mod's ID rather than Minecraft so people don't get confused.
     */
    @Override
    public String getStructureName() {
        return Regeneration.MODID + ":gallifrey_shack";
    }

    @Override
    public int getSize() {
        return 0;
    }
    
    /*
    * This is used so that if two structure's has the same spawn location algorithm, 
    * they will not end up in perfect sync as long as they have different seed modifier.
    * 
    * So make this a big random number that is unique only to this structure.
    */
   protected int getSeedModifier(){
       return 123456789;
   }
   
   /**
    * This method is used for determining if the chunk coordinates are valid, if certain other 
    * structures are too close or not, or some other restrictive condition.
    * <br> DO NOT do dimension checking here. If you do and another mod's dimension
    * is trying to spawn your structure, the locate command will make minecraft hang forever and break the game.
    */
   @Override
   public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
       ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);

       //Checks to see if current chunk is valid to spawn in.
       if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z)
       {
           //Checks if the biome can spawn this structure.
           Biome biome = chunkGen.getBiomeProvider().getBiome(new BlockPos((chunkPosX << 4) + 9, 0, (chunkPosZ << 4) + 9));
           if (chunkGen.hasStructure(biome, this))
           {
               return true;
           }
       }

       return false;
   }
   
   public static class Start extends StructureStart{
       public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox mutableBoundingBox, int referenceIn, long seed) {
           super(structureIn, chunkX, chunkZ, biomeIn, mutableBoundingBox, referenceIn, seed);
       }


       @Override
       public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn){
           /** Check out vanilla's WoodlandMansionStructure for how they offset the x and z
            * so that they get the y value of the land at the mansion's entrance, no matter
            * which direction the mansion is rotated.
            * <br>However, for most purposes, getting the y value of land with the default x and z is good enough. 
            */
           Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

           //Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
           int x = (chunkX << 4) + 7;
           int z = (chunkZ << 4) + 7;

           //Finds the y value of the terrain at location.
           int surfaceY = generator.func_222531_c(x, z, Heightmap.Type.WORLD_SURFACE_WG);
           BlockPos blockpos = new BlockPos(x, surfaceY, z);

           //Now adds the structure pieces to this.components with all details such as where each part goes 
           //so that the structure can be added to the world by worldgen.
           HutStructurePieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

           //Sets the bounds of the structure. 
           this.recalculateStructureSize();
           Regeneration.LOG.log(Level.DEBUG, "Gallifrey Hut at: " + (blockpos.getX()) + " " + blockpos.getY() + " " + (blockpos.getZ()));
       }

   }

}
