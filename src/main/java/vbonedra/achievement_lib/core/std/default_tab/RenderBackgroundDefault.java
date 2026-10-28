package vbonedra.achievement_lib.core.std.default_tab;

import java.util.Random;

import net.minecraft.Block;
import net.minecraft.Icon;

import vbonedra.achievement_lib.core.render.render_abstract.RenderBackgroundAbstract;

/**
 * Vanilla background: sand, dirt, stone, ores, bedrock.
 */
public class RenderBackgroundDefault extends RenderBackgroundAbstract {

    @Override
    protected Icon pickIcon(Random random, int worldRow) {
        int depthRoll = random.nextInt(1 + worldRow) + worldRow / 2;

        if (depthRoll > 37 || worldRow == 35) {
            return Block.bedrock.getIcon(0, 0);
        }
        if (depthRoll == 22) {
            return random.nextInt(2) == 0 ? Block.oreDiamond.getIcon(0, 0) : Block.oreRedstone.getIcon(0, 0);
        }
        if (depthRoll == 10) {
            return Block.oreIron.getIcon(0, 0);
        }
        if (depthRoll == 8) {
            return Block.oreCoal.getIcon(0, 0);
        }
        if (depthRoll > 4) {
            return Block.stone.getIcon(0, 0);
        }
        if (depthRoll > 0) {
            return Block.dirt.getIcon(0, 0);
        }
        return Block.sand.getIcon(0, 0);
    }
}
