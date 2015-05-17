package net.vanillacraft.CoreFunctions.fanciful;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Minecraft 1.8 Title
 * 
 * @version 1.0.4
 * @author Maxim Van de Wynckel
 */
public class Title
{
	/* Title packet */
	private Class<?> packetTitle;
	/* Title packet actions ENUM */
	private Class<?> packetActions;
	/* Chat serializer */
	private Class<?> nmsChatSerializer;
	private Class<?> chatBaseComponent;
	/* Title text and color */
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;
	/* Subtitle text and color */
	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	/* Title timings */
	private int fadeInTime = -1;
	private int stayTime = -1;
	private int fadeOutTime = -1;
	private boolean ticks = false;

	private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title
	 */
	public Title(String title)
	{
		this.title = title;
		loadClasses();
	}

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title text
	 * @param subtitle
	 *            Subtitle text
	 */
	public Title(String title, String subtitle)
	{
		this.title = title;
		this.subtitle = subtitle;
		loadClasses();
	}

	/**
	 * Copy 1.8 title
	 * 
	 * @param title
	 *            Title
	 */
	public Title(Title title)
	{
		// Copy title
		this.title = title.title;
		this.subtitle = title.subtitle;
		this.titleColor = title.titleColor;
		this.subtitleColor = title.subtitleColor;
		this.fadeInTime = title.fadeInTime;
		this.fadeOutTime = title.fadeOutTime;
		this.stayTime = title.stayTime;
		this.ticks = title.ticks;
		loadClasses();
	}

	/**
	 * Create a new 1.8 title
	 * 
	 * @param title
	 *            Title text
	 * @param subtitle
	 *            Subtitle text
	 * @param fadeInTime
	 *            Fade in time
	 * @param stayTime
	 *            Stay on screen time
	 * @param fadeOutTime
	 *            Fade out time
	 */
	public Title(String title, String subtitle, int fadeInTime, int stayTime,
			int fadeOutTime)
	{
		this.title = title;
		this.subtitle = subtitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
		loadClasses();
	}

	/**
	 * Load spigot and NMS classes
	 */
	private void loadClasses()
	{
		packetTitle = getNMSClass("PacketPlayOutTitle");
		packetActions = getNMSClass("EnumTitleAction");
		chatBaseComponent = getNMSClass("IChatBaseComponent");
		nmsChatSerializer = getNMSClass("ChatSerializer");
	}

	/**
	 * Set title text
	 * 
	 * @param title
	 *            Title
	 */
	public Title setTitle(String title)
	{
		this.title = title;
		return this;
	}

	/**
	 * Get title text
	 * 
	 * @return Title text
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * Set subtitle text
	 * 
	 * @param subtitle
	 *            Subtitle text
	 */
	public Title setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
		return this;
	}

	/**
	 * Get subtitle text
	 * 
	 * @return Subtitle text
	 */
	public String getSubtitle()
	{
		return this.subtitle;
	}

	/**
	 * Set the title color
	 * 
	 * @param color
	 *            Chat color
	 */
	public Title setTitleColor(ChatColor color)
	{
		this.titleColor = color;
		return this;
	}

	/**
	 * Set the subtitle color
	 * 
	 * @param color
	 *            Chat color
	 */
	public Title setSubtitleColor(ChatColor color)
	{
		this.subtitleColor = color;
		return this;
	}

	/**
	 * Set title fade in time
	 * 
	 * @param time
	 *            Time
	 */
	public Title setFadeInTime(int time)
	{
		this.fadeInTime = time;
		return this;
	}

	/**
	 * Set title fade out time
	 * 
	 * @param time
	 *            Time
	 */
	public Title setFadeOutTime(int time)
	{
		this.fadeOutTime = time;
		return this;
	}

	/**
	 * Set title stay time
	 * 
	 * @param time
	 *            Time
	 */
	public Title setStayTime(int time)
	{
		this.stayTime = time;
		return this;
	}

	/**
	 * Set timings to ticks
	 */
	public Title setTimingsToTicks()
	{
		ticks = true;
		return this;
	}

	/**
	 * Set timings to seconds
	 */
	public Title setTimingsToSeconds()
	{
		ticks = false;
		return this;
	}

	/**
	 * Send the title to a player
	 * 
	 * @param player
	 *            Player
	 */
	public Title send(Player player)
	{
		if (packetTitle != null)
		{
			// First reset previous settings
			resetTitle(player);
			try
			{
				// Send timings first
				Object handle = getHandle(player);
				Object connection = getField(handle.getClass(),
						"playerConnection").get(handle);
				Object[] actions = packetActions.getEnumConstants();
				Method sendPacket = getMethod(connection.getClass(),
						"sendPacket");
				Object packet = packetTitle.getConstructor(packetActions,
						chatBaseComponent, Integer.TYPE, Integer.TYPE,
						Integer.TYPE).newInstance(actions[2], null,
						fadeInTime * (ticks ? 1 : 20),
						stayTime * (ticks ? 1 : 20),
						fadeOutTime * (ticks ? 1 : 20));
				// Send if set
				if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1)
					sendPacket.invoke(connection, packet);

				// Send title
				Object serialized = getMethod(nmsChatSerializer, "a",
						String.class).invoke(
						null,
						"{text:\""
								+ ChatColor.translateAlternateColorCodes('&',
										title) + "\",color:"
								+ titleColor.name().toLowerCase() + "}");
				packet = packetTitle.getConstructor(packetActions,
						chatBaseComponent).newInstance(actions[0], serialized);
				sendPacket.invoke(connection, packet);
				if (subtitle != "")
				{
					// Send subtitle if present
					serialized = getMethod(nmsChatSerializer, "a", String.class)
							.invoke(null,
									"{text:\""
											+ ChatColor
													.translateAlternateColorCodes(
															'&', subtitle)
											+ "\",color:"
											+ subtitleColor.name()
													.toLowerCase() + "}");
					packet = packetTitle.getConstructor(packetActions,
							chatBaseComponent).newInstance(actions[1],
							serialized);
					sendPacket.invoke(connection, packet);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return this;
	}

	/**
	 * Broadcast the title to all players
	 */
	public Title broadcast()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			send(p);
		}
		return this;
	}

	/**
	 * Clear the title
	 * 
	 * @param player
	 *            Player
	 */
	public void clearTitle(Player player)
	{
		try
		{
			// Send timings first
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection")
					.get(handle);
			Object[] actions = packetActions.getEnumConstants();
			Method sendPacket = getMethod(connection.getClass(), "sendPacket");
			Object packet = packetTitle.getConstructor(packetActions,
					chatBaseComponent).newInstance(actions[3], null);
			sendPacket.invoke(connection, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Reset the title settings
	 * 
	 * @param player
	 *            Player
	 */
	public void resetTitle(Player player)
	{
		try
		{
			// Send timings first
			Object handle = getHandle(player);
			Object connection = getField(handle.getClass(), "playerConnection")
					.get(handle);
			Object[] actions = packetActions.getEnumConstants();
			Method sendPacket = getMethod(connection.getClass(), "sendPacket");
			Object packet = packetTitle.getConstructor(packetActions,
					chatBaseComponent).newInstance(actions[4], null);
			sendPacket.invoke(connection, packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Class<?> getPrimitiveType(Class<?> clazz)
	{
		return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES
				.get(clazz) : clazz;
	}

	private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes)
	{
		int a = classes != null ? classes.length : 0;
		Class<?>[] types = new Class<?>[a];
		for (int i = 0; i < a; i++)
			types[i] = getPrimitiveType(classes[i]);
		return types;
	}

	private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o)
	{
		if (a.length != o.length)
			return false;
		for (int i = 0; i < a.length; i++)
			if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
				return false;
		return true;
	}

	private Object getHandle(Object obj)
	{
		try
		{
			return getMethod("getHandle", obj.getClass()).invoke(obj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private Method getMethod(String name, Class<?> clazz,
			Class<?>... paramTypes)
	{
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);
		for (Method m : clazz.getMethods())
		{
			Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
			if (m.getName().equals(name) && equalsTypeArray(types, t))
				return m;
		}
		return null;
	}

	private String getVersion()
	{
		String name = Bukkit.getServer().getClass().getPackage().getName();
		String version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	private Class<?> getNMSClass(String className)
	{
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try
		{
			clazz = Class.forName(fullName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return clazz;
	}

	private Field getField(Class<?> clazz, String name)
	{
		try
		{
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private Method getMethod(Class<?> clazz, String name, Class<?>... args)
	{
		for (Method m : clazz.getMethods())
			if (m.getName().equals(name)
					&& (args.length == 0 || ClassListEqual(args,
							m.getParameterTypes())))
			{
				m.setAccessible(true);
				return m;
			}
		return null;
	}

	private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2)
	{
		boolean equal = true;
		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++)
			if (l1[i] != l2[i])
			{
				equal = false;
				break;
			}
		return equal;
	}
}
