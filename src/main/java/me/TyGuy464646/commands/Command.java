package me.TyGuy464646.commands;

import me.TyGuy464646.KickBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a general slash command with properties.
 * @author TyGuy464646
 */
public abstract class Command {

	public KickBot bot;
	public String name;
	public String description;
	public Category category;
	public List<OptionData> args;
	public List<SubcommandData> subCommands;
	public Permission permission; // Permission user needs to execute this command
	public Permission botPermission; // Permission bot needs to execute this command

	public Command(KickBot bot) {
		this.bot = bot;
		this.args = new ArrayList<>();
		this.subCommands = new ArrayList<>();
	}

	/**
	 * Code that will be executed upon running a command.
	 * @param event the event in which the command was ran.
	 */
	public abstract void execute(SlashCommandInteractionEvent event);
}
