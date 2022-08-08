package me.TyGuy464646.listeners;

import me.TyGuy464646.KickBot;
import me.TyGuy464646.commands.CommandRegistry;
import me.TyGuy464646.data.GuildData;
import me.TyGuy464646.data.cache.Config;
import me.TyGuy464646.handlers.ConfigHandler;
import me.TyGuy464646.util.embeds.EmbedColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuildListener extends ListenerAdapter {

	private static final ScheduledExecutorService expireScheduler = Executors.newScheduledThreadPool(10);

	private final KickBot bot;

	/**
	 * Messages used on member join.
	 */
	public static String[] messages = {
			"[member] joined. You must construct additional pylons.",
			"Hey! Listen! [member] has joined!",
			"Ha! [member] has joined! You activated my trap card!",
			"We've been expecting you, [member].",
			"It's dangerous to go alone, take [member]!",
			"Swooooosh. [member] just landed.",
			"Brace yourselves. [member] just joined the server.",
			"A wild [member] appeared."
	};

	public GuildListener(KickBot bot) {
		this.bot = bot;
	}

	/**
	 * Registers slash commands as guild commands to guilds that join after startup. NOTE: May change to
	 * global commands on release.
	 *
	 * @param event executes when a guild is ready.
	 */
	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		// Get GuildData from database
		GuildData.get(event.getGuild());
		// Register slash commands
		event.getGuild().updateCommands().addCommands(CommandRegistry.unpackCommandData()).queue();
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
		Guild guild = event.getGuild();
		Member member = event.getMember();
		GuildData data = GuildData.get(guild);
		ConfigHandler configHandler = data.configHandler;
		Config config = configHandler.getConfig();

		Random rand = new Random();
		int number = rand.nextInt(messages.length);

		EmbedBuilder join = new EmbedBuilder();
		join.setColor(EmbedColor.DEFAULT.color);
		join.setThumbnail(member.getEffectiveAvatarUrl());

		// Check if /setup was used otherwise set description not including kick information
		if (configHandler.getTimeUnit() != null && config.getRequiredRole() != null)
			join.setDescription(messages[number].replace("[member]", member.getAsMention()) + "\nYou have **" + config.getKickTime() + " " + config.getTimeUnit() + "** to get the <@&" + config.getRequiredRole() + "> role.");
		else join.setDescription(messages[number].replace("[member]", member.getAsMention()));

		if (config.getTimeUnit() != null && config.getRequiredRole() != null) {
			beginTimer(event, configHandler, member, config.getKickTime(), configHandler.getTimeUnit());
		}

		if (config.getJoinChannel() != null && config.isJoinMessage())
			event.getGuild().getTextChannelById(config.getJoinChannel()).sendMessageEmbeds(join.build()).queue();
		else if (config.getJoinChannel() == null && config.isJoinMessage())
			event.getGuild().getDefaultChannel().sendMessageEmbeds(join.build()).queue();
	}

	private void beginTimer(GuildMemberJoinEvent event, ConfigHandler configHandler, Member target, int time, TimeUnit unit) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				Role requiredRole = event.getGuild().getRoleById(configHandler.getRequiredRole());
				if (!target.getRoles().contains(requiredRole)) {
					target.kick().queue();

					if (configHandler.getConfig().getKickChannel() != null && configHandler.getConfig().isKickMessage())
						event.getGuild().getTextChannelById(configHandler.getConfig().getKickChannel()).sendMessage(target.getAsMention() + " was kicked.").queue();
					else if (configHandler.getConfig().getKickChannel() == null && configHandler.getConfig().isKickMessage())
						event.getGuild().getDefaultChannel().sendMessage(target.getAsMention() + " was kicked.").queue();
				}
			}
		};

		expireScheduler.schedule(task, time, unit);
	}
}
