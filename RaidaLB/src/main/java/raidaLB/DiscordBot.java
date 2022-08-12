package raidaLB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class DiscordBot extends ListenerAdapter {

    private final JDA jda;


    public DiscordBot(final String token) throws LoginException {

        final JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.competing("TNA leaderboards"));
        jda = builder.build();
        jda.addEventListener(this);

        // Set the slash commands
        final List<CommandData> commands = new ArrayList<CommandData>();
        commands.add(Commands.slash("stats", "get player stats").addOption(
            OptionType.STRING, "ign", "This Player's in-game name", true));

        commands.add(Commands.slash("lb", "get a leaderboard").addOption(
            OptionType.STRING, "filter",
            "filter for raids or class, or \"all\" for no filter", true));

        for (final CommandData cmd : commands)
            jda.upsertCommand(cmd).queue();
    }


    @Override
    public void onSlashCommandInteraction(
        final SlashCommandInteractionEvent event) {
        try {

            // Send bot thinking message
            event.deferReply().queue();

            if (event.getName().equals("stats")) {
                final String ign = event.getOption("ign").getAsString();
                final String result = CommandManager.run("stats", new String[] {
                    ign });
                event.getHook().sendMessage(makeCode(result)).queue();
            }

            if (event.getName().equals("lb")) {
                final String filter = event.getOption("filter").getAsString();
                final String result = CommandManager.run("lb", new String[] {
                    filter });
                event.getHook().sendMessage(makeCode(result)).queue();
            }

        }
        catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static String makeCode(final String str) {
        return "```" + str + "```";

    }

}
