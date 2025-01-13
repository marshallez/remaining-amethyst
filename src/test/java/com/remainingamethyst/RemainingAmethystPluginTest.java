package com.remainingamethyst;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RemainingAmethystPluginTest {

    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(RemainingAmethystPlugin.class);
        RuneLite.main(args);
    }
}
