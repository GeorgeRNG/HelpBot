package com.diamondfire.helpbot.command.impl.filespitter;

import com.diamondfire.helpbot.command.help.*;
import com.diamondfire.helpbot.command.permissions.Permission;
import com.diamondfire.helpbot.components.codedatabase.db.CodeDatabase;
import com.diamondfire.helpbot.events.CommandEvent;

public class SoundListCommand extends AbstractFileListCommand {

    @Override
    public String getName() {
        return "sounds";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"soundlist"};
    }

    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Generates a file that contains all sounds.")
                .category(CommandCategory.OTHER);
    }

    @Override
    public Permission getPermission() {
        return Permission.USER;
    }

    @Override
    public void run(CommandEvent event) {
        super.generate(event, CodeDatabase.getSounds());
    }

}
