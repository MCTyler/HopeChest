/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopechestv2.utilities.reflection;

import me.parozzz.hopechestv2.utilities.Utils;
import static me.parozzz.hopechestv2.utilities.reflection.Packets.getStringSerialized;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.bukkit.entity.Player;

public class ActionBar 
{
    private static Object nmsChatMessageType;
    private static Constructor<?> PacketPlayOutChat;
    protected static void init() 
    {
        nmsChatMessageType = (byte)2;
        
        Class<?> ChatMessageType=byte.class;
        if(Utils.bukkitVersion("1.12"))
        {
            ChatMessageType=ReflectionUtils.getNMSClass("ChatMessageType");
            nmsChatMessageType=Arrays.stream(ChatMessageType.getEnumConstants()).filter(o -> o.toString().equals("GAME_INFO")).findFirst().orElseThrow(() -> new NullPointerException("ActionBar init problem"));
        }
        
        try 
        {
            PacketPlayOutChat=ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), ChatMessageType);
        }
        catch(NoSuchMethodException | SecurityException ex) 
        {
            ex.printStackTrace(); 
        }
    }
    
    public static void send(final Player p, final String message) 
    {
        try 
        {
            Packets.sendPacket(p, PacketPlayOutChat.newInstance(getStringSerialized(message), nmsChatMessageType));
        } 
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException ex)  
        { 
            ex.printStackTrace(); 
        }
    }
}