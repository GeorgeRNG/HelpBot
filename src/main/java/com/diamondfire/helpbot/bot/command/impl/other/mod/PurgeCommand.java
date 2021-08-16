package com.diamondfire.helpbot.bot.command.impl.other.mod;

import com.diamondfire.helpbot.bot.command.argument.ArgumentSet;
import com.diamondfire.helpbot.bot.command.argument.impl.types.*;
import com.diamondfire.helpbot.bot.command.help.*;
import com.diamondfire.helpbot.bot.command.impl.Command;
import com.diamondfire.helpbot.bot.command.permissions.Permission;
import com.diamondfire.helpbot.bot.command.reply.PresetBuilder;
import com.diamondfire.helpbot.bot.command.reply.feature.informative.*;
import com.diamondfire.helpbot.bot.events.CommandEvent;
import com.diamondfire.helpbot.sys.externalfile.ExternalFileUtil;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;

import java.io.File;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class PurgeCommand extends Command {
    
    @Override public String getName() { return "purge"; }
    
    @Override
    public HelpContext getHelpContext() {
        return new HelpContext()
                .description("Removes the most recent messages sent in a channel. Maximum 100 messages.")
                .category(CommandCategory.OTHER)
                .addArgument(
                        new HelpContextArgument()
                                .name("count")
                );
    }
    
    @Override
    protected ArgumentSet compileArguments() {
        return new ArgumentSet()
                .addArgument("count",
                        new IntegerArgument());
    }
    
    @Override
    public Permission getPermission() {
        return Permission.MODERATION;
    }
    
    @Override
    public void run(CommandEvent event) {
        PresetBuilder builder = new PresetBuilder();
        
        int messagesToRemove = event.getArgument("count");
        
        if (messagesToRemove > 100 || messagesToRemove < 2) {
            builder.withPreset(
                    new InformativeReply(InformativeReplyType.ERROR, "Message count not within 2 to 100.")
            );
            event.reply(builder);
        } else {
            TextChannel channel = event.getChannel();
            channel.getHistory().retrievePast(messagesToRemove).queue((messages) -> {
                // Adds the messages to the messageBuilder object
                StringBuilder stringBuilder = new StringBuilder();
                
                // Iterates through the message history and appends the values to the MessageBuilder.
                for (Message m : messages) {
                    stringBuilder.insert(0,
                                    String.format("[%s] (%s): %s",
                                            m.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                            m.getAuthor().getName(),
                                            m.getContentRaw())
                            );
                    if (!m.getAttachments().isEmpty()) {
                        for (Message.Attachment a : m.getAttachments()) {
                            stringBuilder.insert(0,
                                    String.format(" [ATTACHMENT: %s ]\n",
                                            a.getProxyUrl())
                            );
                        }
                    } else {
                        stringBuilder.insert(0,
                                "\n"
                        );
                    }
                }
    
                stringBuilder.insert(0, "Here are the messages you purged;\n");
                
                try {
                    File file = ExternalFileUtil.generateFile("purge_log.txt");
                    Files.writeString(file.toPath(), stringBuilder.toString(), StandardOpenOption.WRITE);
                    
                    event.getAuthor().openPrivateChannel().flatMap(
                            userChannel -> userChannel.sendFile(file)
                    ).queue();
                    
                } catch (Exception e) {
                    throw new IllegalStateException();
                }
                
                // Removes the messages.
                channel.deleteMessages(messages).queue();
            });
        }
    }
}