package me.syntaxerror.snowballswoop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack Snowball;
    public static ItemStack ExplosiveSnowball;
    public static ItemStack TripleShot;
    public static ItemStack HealingPotion;
    public static ItemStack SnowWall;

    public static void init(){
        createSnowball();
        createExplosiveSnowball();
        createTripleShot();
        createHealingPotion();
        createSnowWall();
    }

    private static void createSnowball(){
        ItemStack item = new ItemStack(Material.SNOWBALL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Snowball");
        List<String> lore = new ArrayList<>();
        lore.add("§eRight click to launch!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        Snowball = item;
    }
    private static void createExplosiveSnowball(){
        ItemStack item = new ItemStack(Material.TNT, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Explosive Snowball");
        List<String> lore = new ArrayList<>();
        lore.add("§7When used, all snowballs shot");
        lore.add("§7will explode terrain on impact.");
        lore.add("§7Does not deal splash damage.");
        lore.add("§eLasts for 5 seconds");
        lore.add("§eRight click to activate!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        ExplosiveSnowball = item;
    }
    private static void createTripleShot(){
        ItemStack item = new ItemStack(Material.CROSSBOW, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Triple Shot");
        List<String> lore = new ArrayList<>();
        lore.add("§7When used, shoot three snowballs");
        lore.add("§7at a time.");
        lore.add("§eLasts for 5 seconds");
        lore.add("§eRight click to activate!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        TripleShot = item;
    }
    private static void createHealingPotion(){
        ItemStack item = new ItemStack(Material.POTION, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Healing Potion");
        List<String> lore = new ArrayList<>();
        lore.add("§7Restores 1 life.");
        lore.add("§eOne time use");
        lore.add("§eRight click to use!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        HealingPotion = item;
    }
    private static void createSnowWall(){
        ItemStack item = new ItemStack(Material.BRICKS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Snow Wall");
        List<String> lore = new ArrayList<>();
        lore.add("§7Creates a 5 block wide 3 block tall");
        lore.add("§7wall of snow in front of you.");
        lore.add("§eOne time use");
        lore.add("§eRight click to use!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        SnowWall = item;
    }
}
