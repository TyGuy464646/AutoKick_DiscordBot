package me.TyGuy464646.commands.utility;

import com.mongodb.client.model.Filters;
import me.TyGuy464646.KickBot;
import me.TyGuy464646.commands.Category;
import me.TyGuy464646.commands.Command;
import me.TyGuy464646.data.GuildData;
import me.TyGuy464646.data.cache.Config;
import me.TyGuy464646.util.embeds.EmbedColor;
import me.TyGuy464646.util.embeds.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bson.conversions.Bson;

/**
 * Command that displays the current settings used in /setup
 * @author TyGuy464646
 */
public class Current extends Command {

	public Current(KickBot bot) {
		super(bot);
		this.name = "current";
		this.description = "Shows current values used in /setup";
		this.category = Category.UTILITY;
	}

	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		GuildData data = GuildData.get(event.getGuild());
		Config config = data.configHandler.getConfig();

		StringBuilder text = new StringBuilder();
		if (config.getTimeUnit() != null && config.getRequiredRole() != null) {
			text.append("-- Current Time Delay: **").append(config.getKickTime()).append(" ").append(config.getTimeUnit().toString()).append("**\n");
			text.append("-- Current Required Role: <@&").append(config.getRequiredRole()).append(">\n\n");
			text.append("-- Join messages enabled: ").append(config.isJoinMessage()).append("\n");
			if (config.getJoinChannel() != null)
				text.append("-- Join messages channel: <#").append(config.getJoinChannel()).append(">\n");
			text.append("-- Kick messages enabled: ").append(config.isKickMessage()).append("\n");
			if (config.getKickChannel() != null)
				text.append("-- Kick messages channel: <#").append(config.getKickChannel()).append(">\n");

			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setTitle("Current set Variables");
			embedBuilder.setColor(EmbedColor.DEFAULT.color);
			embedBuilder.setDescription(text.toString());
			event.getHook().sendMessageEmbeds(embedBuilder.build()).queue();
		} else {
			event.getHook().sendMessageEmbeds(EmbedUtils.createError("Please use /setup before running this command.")).queue();
		}
	}
}
