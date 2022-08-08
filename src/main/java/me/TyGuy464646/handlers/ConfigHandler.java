package me.TyGuy464646.handlers;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import me.TyGuy464646.KickBot;
import me.TyGuy464646.data.cache.Config;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Handles config data for the guild and various modules.
 *
 * @author TyGuy464646
 */
public class ConfigHandler {

	private final Guild guild;
	private final KickBot bot;
	private final Bson filter;
	private Config config;

	public ConfigHandler(KickBot bot, Guild guild) {
		this.bot = bot;
		this.guild = guild;

		// Get POJO object from database
		this.filter = Filters.eq("guild", guild.getIdLong());
		this.config = bot.database.config.find(filter).first();
		if (this.config == null) {
			this.config = new Config(guild.getIdLong());
			bot.database.config.insertOne(config);
		}
	}

	/**
	 * Access the config cache.
	 *
	 * @return a chache instance of the Config from database.
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * Check if guild has kick messages turned on.
	 *
	 * @return true if on, otherwise false.
	 */
	public boolean isKickMessage() {
		boolean kickMessage = bot.database.config.find(filter).first().isKickMessage();
		return kickMessage;
	}

	/**
	 * Sets kick message to local cache and database.
	 *
	 * @param choice the boolean that is to be set.
	 */
	public void enableKickMessage(boolean choice) {
		config.setKickMessage(choice);
		bot.database.config.updateOne(filter, Updates.set("kick_message", choice));
	}

	/**
	 * Check if guild has join messages turned on.
	 *
	 * @return true if on, otherwise false.
	 */
	public boolean isJoinMessage() {
		boolean joinMessage = bot.database.config.find(filter).first().isJoinMessage();
		return joinMessage;
	}

	/**
	 * Sets join message to local cache and database.
	 *
	 * @param choice the boolean that is to be set.
	 */
	public void enableJoinMessage(boolean choice) {
		config.setJoinMessage(choice);
		bot.database.config.updateOne(filter, Updates.set("join_message", choice));
	}

	/**
	 * Check if guild has kick channel set.
	 *
	 * @return channel.
	 */
	public Long getKickChannel() {
		Long kickChannel = bot.database.config.find(filter).first().getKickChannel();
		return kickChannel;
	}

	/**
	 * Sets the kick channel to local cache and database.
	 *
	 * @param id the id of the kick channel being set.
	 */
	public void setKickChannel(Long id) {
		config.setKickChannel(id);
		if (id != null)
			bot.database.config.updateOne(filter, Updates.set("kick_channel", id));
		else bot.database.config.updateOne(filter, Updates.unset("kick_channel"));
	}

	/**
	 * Checks if guild has join channel set.
	 *
	 * @return channel.
	 */
	public Long getJoinChannel() {
		Long joinChannel = bot.database.config.find(filter).first().getJoinChannel();
		return joinChannel;
	}

	/**
	 * Sets the join channel to local cache and database.
	 *
	 * @param id the id of the join channel being set.
	 */
	public void setJoinChannel(Long id) {
		config.setJoinChannel(id);
		if (id != null)
			bot.database.config.updateOne(filter, Updates.set("join_channel", id));
		else bot.database.config.updateOne(filter, Updates.unset("join_channel"));
	}

	/**
	 * Gets kick time set by guild.
	 *
	 * @return the kick time.
	 */
	public int getKickTime() {
		int kickTime = bot.database.config.find(filter).first().getKickTime();
		return kickTime;
	}

	/**
	 * Sets the kick time to local cache and database.
	 *
	 * @param time the time that is set.
	 */
	public void setKickTime(int time) {
		config.setKickTime(time);
		bot.database.config.updateOne(filter, Updates.set("kick_time", time));
	}

	/**
	 * Gets the {@link TimeUnit} set by the guild.
	 *
	 * @return the {@link TimeUnit}
	 */
	public TimeUnit getTimeUnit() {
		String timeUnit = bot.database.config.find(filter).first().getTimeUnit();
		if (timeUnit.equals(TimeUnit.SECONDS.toString()))
			return TimeUnit.SECONDS;
		else if (timeUnit.equals(TimeUnit.MINUTES.toString()))
			return TimeUnit.MINUTES;
		else if (timeUnit.equals(TimeUnit.HOURS.toString()))
			return TimeUnit.HOURS;
		else if (timeUnit.equals(TimeUnit.DAYS.toString()))
			return TimeUnit.DAYS;
		else return null;
	}

	/**
	 * Sets the time unit to local cache and database.
	 *
	 * @param timeUnit the {@link TimeUnit} being set.
	 */
	public void setTimeUnit(String timeUnit) {
		config.setTimeUnit(timeUnit);
		bot.database.config.updateOne(filter, Updates.set("time_unit", timeUnit));
	}

	/**
	 * Gets the required role set by the guild.
	 *
	 * @return the required role.
	 */
	public Long getRequiredRole() {
		Long role = bot.database.config.find(filter).first().getRequiredRole();
		return role;
	}

	/**
	 * Sets the required role to local cache and database.
	 *
	 * @param role the role being set.
	 */
	public void setRequiredRole(Long role) {
		config.setRequiredRole(role);
		bot.database.config.updateOne(filter, Updates.set("required_role", role));
	}

	/**
	 * Resets all config settings to their default;
	 */
	public void resetConfig() {
		config.setKickMessage(false);
		config.setJoinMessage(false);
		config.setKickChannel(null);
		config.setJoinChannel(null);
		config.setKickTime(0);
		config.setTimeUnit(null);
		config.setRequiredRole(null);
		bot.database.config.replaceOne(filter, config);
	}
}
