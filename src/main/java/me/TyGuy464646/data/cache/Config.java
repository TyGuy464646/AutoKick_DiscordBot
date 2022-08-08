package me.TyGuy464646.data.cache;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;

/**
 * POJO object that stores config data for a guild.
 *
 * @author TyGuy464646
 */
public class Config {

	private long guild;

	@BsonProperty("kick_message")
	private boolean kickMessage;

	@BsonProperty("join_message")
	private boolean joinMessage;

	@BsonProperty("kick_channel")
	private Long kickChannel;

	@BsonProperty("join_channel")
	private Long joinChannel;

	@BsonProperty("kick_time")
	private int kickTime;

	@BsonProperty("time_unit")
	private String timeUnit;

	@BsonProperty("required_role")
	private Long requiredRole;

	public Config() {
	}

	public Config(long guild) {
		this.guild = guild;
		this.kickMessage = false;
		this.joinMessage = false;
		this.kickChannel = null;
		this.joinChannel = null;
		this.kickTime = 0;
		this.timeUnit = null;
		this.requiredRole = null;
	}

	// Getters and Setters
	public long getGuild() {
		return guild;
	}

	public void setGuild(long guild) {
		this.guild = guild;
	}

	public boolean isKickMessage() {
		return kickMessage;
	}

	public void setKickMessage(boolean kickMessage) {
		this.kickMessage = kickMessage;
	}

	public boolean isJoinMessage() {
		return joinMessage;
	}

	public void setJoinMessage(boolean joinMessage) {
		this.joinMessage = joinMessage;
	}

	public Long getKickChannel() {
		return kickChannel;
	}

	public void setKickChannel(Long kickChannel) {
		this.kickChannel = kickChannel;
	}

	public Long getJoinChannel() {
		return joinChannel;
	}

	public void setJoinChannel(Long joinChannel) {
		this.joinChannel = joinChannel;
	}

	public int getKickTime() {
		return kickTime;
	}

	public void setKickTime(int kickTime) {
		this.kickTime = kickTime;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Long getRequiredRole() {
		return requiredRole;
	}

	public void setRequiredRole(Long requiredRole) {
		this.requiredRole = requiredRole;
	}
}
