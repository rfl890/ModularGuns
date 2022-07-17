package net.modularmods.modularguns.excepts;


public class ExpectRegisterItemRenderer {

    @dev.architectury.injectables.annotations.ExpectPlatform
    public static void registerItemRenderer() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
