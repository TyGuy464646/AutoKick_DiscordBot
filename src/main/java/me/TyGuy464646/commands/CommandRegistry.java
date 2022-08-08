package me.TyGuy464646.commands;

import me.TyGuy464646.KickBot;
import me.TyGuy464646.commands.staff.JoinMessage;
import me.TyGuy464646.commands.staff.KickMessage;
import me.TyGuy464646.commands.staff.Setup;
import me.TyGuy464646.commands.utility.Current;
import me.TyGuy464646.data.GuildData;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Registers, listens, and executes commands.
 * @author TyGuy464646
 */
public class CommandRegistry extends ListenerAdapter {

	/**
	 * CommandRegistry {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);

	/**
	 * {@link ArrayList} of commands in the exact order registered.
	 */
	public static final List<Command> commands = new ArrayList<>();

	/**
	 * {@link HashMap} of command names to command objects.
	 */
	public static final Map<String, Command> commandsMap = new HashMap<>();

	/**
	 * Adds commands to a global list and registers them as event listeners.
	 * @param bot An instance of {@link KickBot}.
	 */
	public CommandRegistry(KickBot bot) {
		mapCommand(
				// Staff commands
				new JoinMessage(bot),
				new KickMessage(bot),
				new Setup(bot),

				// Utility commands
				new Current(bot)
		);
	}

	/**
	 * Adds a {@link Command} to the static list and map.
	 * @param cmds a spread list of {@link Command} objects.
	 */
	private void mapCommand(Command... cmds) {
		for (Command cmd : cmds) {
			commandsMap.put(cmd.name, cmd);
			commands.add(cmd);
		}
	}

	/**
	 * Creates a list of {@link CommandData} for all commands.
	 * @return a list of {@link CommandData} to be used for registration.
	 */
	public static List<CommandData> unpackCommandData() {
		// Register slash commands
		List<CommandData> commandData = new ArrayList<>();
		for (Command command : commands) {
			SlashCommandData slashCommand = Commands.slash(command.name, command.description).addOptions(command.args);
			if (command.permission != null)
				slashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.permission));
			if (!command.subCommands.isEmpty())
				slashCommand.addSubcommands(command.subCommands);
			commandData.add(slashCommand);
		}
		return commandData;
	}

	/**
	 * Runs whenever a {@link SlashCommandInteractionEvent} is run in Discord.
	 * @param event the event in which the slash command was ran.
	 */
	@Override
	public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
		// Get command by name
		Command cmd = commandsMap.get(event.getName());
		if (cmd != null) {
			// Check for required bot permissions
			Role botRole = Objects.requireNonNull(event.getGuild()).getBotRole();
			if (cmd.botPermission != null) {
				if (!botRole.hasPermission(cmd.botPermission) && !botRole.hasPermission(Permission.ADMINISTRATOR)) {
					String text = "I need the `" + cmd.botPermission.getName() + "` permission to execute that command.";
					event.reply(text).setEphemeral(true).queue();
					return;
				}
			}
			// Run command
			cmd.execute(event);
		}
	}

	/**
	 * Registers slash commands as guild commands. NOTE: May chance to global commands on release.
	 * @param event executes when a guild is ready.
	 */
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		// Get GuildData from database
		GuildData.get(event.getGuild());

		// Register slash command
		event.getGuild().updateCommands().addCommands(unpackCommandData()).queue(succ -> {}, fail -> {});
		LOGGER.info("Guild commands have been updated.");
	}
}
