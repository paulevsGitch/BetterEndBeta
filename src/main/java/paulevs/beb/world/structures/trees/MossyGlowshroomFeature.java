package paulevs.beb.world.structures.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.MathHelper;
import paulevs.beb.block.EndLuminophorBlock;
import paulevs.beb.block.EndOrganicBlock;
import paulevs.beb.block.EndWoodBlock;
import paulevs.beb.math.Vec3F;
import paulevs.beb.noise.OpenSimplexNoise;
import paulevs.beb.registry.EndBlocks;
import paulevs.beb.util.BlockState;
import paulevs.beb.util.BlocksHelper;
import paulevs.beb.util.MHelper;
import paulevs.beb.util.SplineHelper;
import paulevs.beb.util.sdf.SDF;
import paulevs.beb.util.sdf.operator.SDFBinary;
import paulevs.beb.util.sdf.operator.SDFCoordModify;
import paulevs.beb.util.sdf.operator.SDFFlatWave;
import paulevs.beb.util.sdf.operator.SDFScale;
import paulevs.beb.util.sdf.operator.SDFScale3D;
import paulevs.beb.util.sdf.operator.SDFSmoothUnion;
import paulevs.beb.util.sdf.operator.SDFSubtraction;
import paulevs.beb.util.sdf.operator.SDFTranslate;
import paulevs.beb.util.sdf.operator.SDFUnion;
import paulevs.beb.util.sdf.primitive.SDFCappedCone;
import paulevs.beb.util.sdf.primitive.SDFPrimitive;
import paulevs.beb.util.sdf.primitive.SDFSphere;

public class MossyGlowshroomFeature extends Structure {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Vec3F CENTER = new Vec3F();
	private static final SDFBinary FUNCTION;
	private static final SDFTranslate HEAD_POS;
	private static final SDFFlatWave ROOTS_ROT;
	
	private static final SDFPrimitive CONE1;
	private static final SDFPrimitive CONE2;
	private static final SDFPrimitive CONE_GLOW;
	private static final SDFPrimitive ROOTS;
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockBase block = BlockBase.BY_ID[level.getTileId(x, y - 1, z)];
		if (!BlocksHelper.isTerrain(block)) {
			return false;
		};
		
		BlockState cap = new BlockState(EndBlocks.getBlock("end_organic"), EndOrganicBlock.getVariant("mossy_glowshroom_cap"));
		BlockState wood = new BlockState(EndBlocks.getBlock("end_wood"), EndWoodBlock.getVariant("mossy_glowshroom"));
		BlockState hymenophore = new BlockState(EndBlocks.getBlock("end_luminophor"), EndLuminophorBlock.getVariant("mossy_glowshroom_hymenophore"));
		
		CONE1.setBlock(cap);
		CONE2.setBlock(cap);
		CONE_GLOW.setBlock(hymenophore);
		ROOTS.setBlock(wood);
		
		float height = MHelper.randRange(10F, 25F, random);
		int count = MathHelper.floor(height / 4);
		List<Vec3F> spline = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, count);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.1F, 1.5F, (pos) -> {
			return wood;
		});
		Vec3F pos = spline.get(spline.size() - 1);
		float scale = MHelper.randRange(0.75F, 1.1F, random);
		
		Vec3i blockPos = new Vec3i(x, y, z);
		if (!SplineHelper.canGenerate(spline, scale, blockPos, level, REPLACE)) {
			return false;
		}
		level.setTile(x, y, z, 0);
		
		CENTER.set(blockPos.x, 0, blockPos.z);
		HEAD_POS.setTranslate(pos.x, pos.y, pos.z);
		ROOTS_ROT.setAngle(random.nextFloat() * MHelper.PI2);
		FUNCTION.setSourceA(sdf);
		
		new SDFScale().setScale(scale)
				.setSource(FUNCTION)
				.setReplaceFunction(REPLACE)
				/*.addPostProcess((info) -> {
					if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getState())) {
						if (random.nextBoolean() && info.getStateUp().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState().with(MossyGlowshroomCapBlock.TRANSITION, true));
							return info.getState();
						}
						else if (!EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateUp()) || !EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM.bark.getDefaultState());
							return info.getState();
						}
					}
					else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
						if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown().getBlock())) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState().with(MossyGlowshroomCapBlock.TRANSITION, true));
							return info.getState();
						}
						
						info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState());
						return info.getState();
					}
					else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
						for (Direction dir: BlocksHelper.HORIZONTAL) {
							if (info.getState(dir) == AIR) {
								info.setBlockPos(info.getPos().offset(dir), EndBlocks.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(FurBlock.FACING, dir));
							}
						}
						
						if (info.getStateDown().getBlock() != EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
							info.setBlockPos(info.getPos().down(), EndBlocks.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(FurBlock.FACING, Direction.DOWN));
						}
					}
					return info.getState();
				})*/
				.fillRecursive(level, blockPos);
		
		return true;
	}
	
	static {
		SDFCappedCone cone1 = new SDFCappedCone().setHeight(2.5F).setRadius1(1.5F).setRadius2(2.5F);
		SDFCappedCone cone2 = new SDFCappedCone().setHeight(3F).setRadius1(2.5F).setRadius2(13F);
		SDF posedCone2 = new SDFTranslate().setTranslate(0, 5, 0).setSource(cone2);
		SDF posedCone3 = new SDFTranslate().setTranslate(0, 12F, 0).setSource(new SDFScale().setScale(2).setSource(cone2));
		SDF upCone = new SDFSubtraction().setSourceA(posedCone2).setSourceB(posedCone3);
		SDF wave = new SDFFlatWave().setRaysCount(12).setIntensity(1.3F).setSource(upCone);
		SDF cones = new SDFSmoothUnion().setRadius(3).setSourceA(cone1).setSourceB(wave);
		
		CONE1 = cone1;
		CONE2 = cone2;
		
		SDF innerCone = new SDFTranslate().setTranslate(0, 1.25F, 0).setSource(upCone);
		innerCone = new SDFScale3D().setScale(1.2F, 1F, 1.2F).setSource(innerCone);
		cones = new SDFUnion().setSourceA(cones).setSourceB(innerCone);
		
		SDF glowCone = new SDFCappedCone().setHeight(3F).setRadius1(2F).setRadius2(12.5F);
		CONE_GLOW = (SDFPrimitive) glowCone;
		glowCone = new SDFTranslate().setTranslate(0, 4.25F, 0).setSource(glowCone);
		glowCone = new SDFSubtraction().setSourceA(glowCone).setSourceB(posedCone3);
		
		cones = new SDFUnion().setSourceA(cones).setSourceB(glowCone);
		
		OpenSimplexNoise noise = new OpenSimplexNoise(1234);
		cones = new SDFCoordModify().setFunction((pos) -> {
			float dist = MHelper.length(pos.x, pos.z);
			float y = pos.y + (float) noise.eval(pos.x * 0.1 + CENTER.x, pos.z * 0.1 + CENTER.z) * dist * 0.3F - dist * 0.15F;
			pos.set(pos.x, y, pos.z);
		}).setSource(cones);
		
		HEAD_POS = (SDFTranslate) new SDFTranslate().setSource(new SDFTranslate().setTranslate(0, 2.5F, 0).setSource(cones));
		
		SDF roots = new SDFSphere().setRadius(4F);
		ROOTS = (SDFPrimitive) roots;
		roots = new SDFScale3D().setScale(1, 0.7F, 1).setSource(roots);
		ROOTS_ROT = (SDFFlatWave) new SDFFlatWave().setRaysCount(5).setIntensity(1.5F).setSource(roots);
		
		FUNCTION = new SDFSmoothUnion().setRadius(4).setSourceB(new SDFUnion().setSourceA(HEAD_POS).setSourceB(ROOTS_ROT));
		
		REPLACE = (state) -> {
			if (BlocksHelper.isTerrain(state.getBlock())) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
