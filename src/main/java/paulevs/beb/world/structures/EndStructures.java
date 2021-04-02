package paulevs.beb.world.structures;

import paulevs.beb.world.structures.placer.RandomStructureScatter;
import paulevs.beb.world.structures.trees.MossyGlowshroomFeature;

public class EndStructures {
	public static final EndStructure MOSSY_GLOWSHROOM = new EndStructure(new MossyGlowshroomFeature(), new RandomStructureScatter(2));
}
