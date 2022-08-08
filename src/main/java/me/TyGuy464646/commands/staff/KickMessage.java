package me.TyGuy464646.commands.staff;

import me.TyGuy464646.KickBot;
import me.TyGuy464646.commands.Category;
import me.TyGuy464646.commands.Command;
import me.TyGuy464646.data.GuildData;
import me.TyGuy464646.handlers.ConfigHandler;
import me.TyGuy464646.util.embeds.EmbedUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class KickMessage extends Command {

	public KickMessage(KickBot bot) {
		super(bot);
		this.name = "kickmessage";
		this.description = "Enable/Disable kick messages and edit which channel they show up in.";
		this.category = Category.STAFF;
		this.permission = Permission.MANAGE_SERVER;
		this.args.add(new OptionData(OptionType.BOOLEAN, "enablemessage", "Enable/Disable kick emssages."));
		this.args.add(new OptionData(OptionType.CHANNEL, "channel", "Set which channel you want the kick messages to appear."));
	}

	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		GuildData data = GuildData.get(event.getGuild());
		ConfigHandler configHandler = data.configHandler;

		OptionMapping enableMessage = event.getOption("enablemessage");
		OptionMapping channel = event.getOption("channel");

		if (enableMessage != null)
			configHandler.enableKickMessage(enableMessage.getAsBoolean());

		if (channel != null)
			configHandler.setKickChannel(channel.getAsLong());

		if (enableMessage == null && channel == null) {
			configHandler.enableKickMessage(false);
			configHandler.setKickChannel(null);
			event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Reset kick messages!")).queue();
			return;
		}

		event.getHook().sendMessageEmbeds(EmbedUtils.createSuccess("Use /current to see changes!")).queue();
	}
}
