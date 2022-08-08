package me.TyGuy464646;

import io.github.cdimascio.dotenv.Dotenv;
import me.TyGuy464646.commands.CommandRegistry;
import me.TyGuy464646.data.Database;
import me.TyGuy464646.data.GuildData;
import me.TyGuy464646.listeners.GuildListener;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * KickBot is a bot that will auto kick members that don't have a role within a custom time frame.
 * @author TyGuy464646
 */
public class KickBot {

	private static final Logger LOGGER = LoggerFactory.getLogger(KickBot.class);

	public final @NotNull Dotenv config;
	public final @NotNull ShardManager shardManager;

	public final @NotNull Database database;

	/**
	 * Builds bot shards and registers commands and moduels.
	 * @throws LoginException throws if bot token is invalid.
	 */
	public KickBot() throws LoginException {
		// Setup Database
		config = Dotenv.configure().ignoreIfMissing().load();
		database = new Database(config.get("DATABASE"));

		// Build JDA shards
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
		builder.setStatus(OnlineStatus.ONLINE);
		builder.setActivity(Activity.watching("people get kicked"));
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
				GatewayIntent.GUILD_MESSAGES,
				GatewayIntent.GUILD_MESSAGE_REACTIONS);
		builder.addEventListeners(new CommandRegistry(this));
		shardManager = builder.build();
		GuildData.init(this);

		shardManager.addEventListener(
				new GuildListener(this)
		);
	}

	public static void main(String[] args) throws LoginException {
		new KickBot();
	}
}
