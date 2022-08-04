package me.TyGuy464646.commands;

/**
 * Categories in which commands fall in to.
 * @author TyGuy464646
 */
public enum Category {

	STAFF(":computer:", "Staff"),
	UTILITY(":tools:", "Utility");

	public final String emoji;
	public final String name;

	Category(String emoji, String name) {
		this.emoji = emoji;
		this.name = name;
	}
}
