package org.cyclops.capabilityproxy.gametest;

import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.TestFunction;
import net.neoforged.neoforge.gametest.GameTestHolder;
import org.cyclops.cyclopscore.gametest.GameTestLoaderHelpers;
import org.cyclops.capabilityproxy.Reference;

import java.util.Collection;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
public class GameTestsLoaderNeoForge extends GameTestsCommon {
    @GameTestGenerator
    public Collection<TestFunction> generateCommonTests() throws InstantiationException, IllegalAccessException {
        return GameTestLoaderHelpers.generateCommonTests(Reference.MOD_ID, new Class[]{
                GameTestsCommon.class
        });
    }
}
