package me.TyGuy464646.commands.staff;

import me.TyGuy464646.KickBot;
import me.TyGuy464646.commands.Category;
import me.TyGuy464646.commands.Command;
import me.TyGuy464646.data.GuildData;
import me.TyGuy464646.data.cache.Config;
import me.TyGuy464646.handlers.ConfigHandler;
import me.TyGuy464646.util.embeds.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.TimeUnit;

public class Setup extends Command {

	public Setup(KickBot bot) {
		super(bot);
		this.name = "setup";
		this.description = "Set up the bot with time delay and required role";
		this.category = Category.STAFF;
		this.permission = Permission.MANAGE_SERVER;
		this.args.add(new OptionData(OptionType.STRING, "timedelay", "The time delay in which a user gets kicked.", true));
		this.args.add(new OptionData(OptionType.ROLE, "requiredrole", "Role in which a user needs not to get kicked.", true));
	}

	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		GuildData data = GuildData.get(event.getGuild());
		ConfigHandler configHandler = data.configHandler;
		Config config = data.configHandler.getConfig();

		OptionMapping timeDelay = event.getOption("timedelay");
		OptionMapping requiredRole = event.getOption("requiredrole");

		parseTime(event, timeDelay.getAsString());
		configHandler.setRequiredRole(requiredRole.getAsLong());

		event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Time delay and required role have been updated!")).queue();
	}

	/**
	 * Parses the time given in the option.
	 *
	 * @param time  as a {@link String}.
	 * @param event the event in which to parse time.
	 */
	private void parseTime(SlashCommandInteractionEvent event, String time) {
		GuildData data = GuildData.get(event.getGuild());
		ConfigHandler configHandler = data.configHandler;
		Config config = data.configHandler.getConfig();

		char[] timeArr = time.toLowerCase().toCharArray();
		StringBuilder amount = new StringBuilder();
		int breakpoint = 0;

		for (int i = 0; i < timeArr.length; i++) {
			switch (timeArr[i]) {
				case 's' -> {
					configHandler.setTimeUnit(TimeUnit.SECONDS.toString());
					breakpoint = i;
				}
				case 'm' -> {
					configHandler.setTimeUnit(TimeUnit.MINUTES.toString());
					breakpoint = i;
				}
				case 'h' -> {
					configHandler.setTimeUnit(TimeUnit.HOURS.toString());
					breakpoint = i;
				}
				case 'd' -> {
					configHandler.setTimeUnit(TimeUnit.DAYS.toString());
					breakpoint = i;
				}
			}
		}

		// Append numbers before the breakpoint (char in timeArr[]) to a string and convert to an integer
		for (int i = 0; i < breakpoint; i++) {
			amount.append(timeArr[i]);
		}
		configHandler.setKickTime(Integer.parseInt(amount.toString()));
	}
}
