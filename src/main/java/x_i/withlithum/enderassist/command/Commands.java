package x_i.withlithum.enderassist.command;

import x_i.withlithum.enderassist.Game;

public final class Commands {
    private Commands() {}

    public static void init() {
        var dispatcher = Game.getServer().getCommands().getDispatcher();

        PingCommand.register(dispatcher);
        MetaCommand.register(dispatcher);
        BedCommand.register(dispatcher);
    }
}
