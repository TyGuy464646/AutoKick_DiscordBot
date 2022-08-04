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

/**
 * Command that enables/disables join messages and the channel they appear in.
 *
 * @author TyGuy464646
 */
public class JoinMessage extends Command {

	public JoinMessage(KickBot bot) {
		super(bot);
		this.name = "joinmessage";
		this.description = "Enable/Disable join messages and edit which channel they show up in.";
		this.category = Category.STAFF;
		this.permission = Permission.MANAGE_SERVER;
		this.args.add(new OptionData(OptionType.BOOLEAN, "enablemessage", "Enable/Disable join messages."));
		this.args.add(new OptionData(OptionType.CHANNEL, "channel", "Set which channel you want the join messages to appear."));
	}

	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		GuildData data = GuildData.get(event.getGuild());
		ConfigHandler configHandler = data.configHandler;
		Config config = data.configHandler.getConfig();

		String text = "";
		OptionMapping enableMessage = event.getOption("enablemessage");
		OptionMapping channel = event.getOption("channel");

		if (enableMessage != null) {
			configHandler.enableJoinMessage(enableMessage.getAsBoolean());
		}
		if (channel != null) {
			configHandler.setJoinChannel(channel.getAsLong());
			event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Updated join messages to " + config.isJoinMessage() + " and join messages channel!")).queue();
		}

		if (enableMessage != null && channel == null) {
			event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Updated join messages to " + config.isJoinMessage() + "!")).queue();
		}

		if (enableMessage == null && channel == null) {
			configHandler.enableJoinMessage(false);
			configHandler.setJoinChannel(null);
			event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Reset join messages!")).queue();
		}
	}
}
