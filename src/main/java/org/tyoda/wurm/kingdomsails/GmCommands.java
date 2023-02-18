package org.tyoda.wurm.kingdomsails;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;
import org.tyoda.wurm.modelmeimpressed.ModelMeImpressed;

import java.util.StringTokenizer;
import java.util.logging.Level;

public class GmCommands {
    private static void createShip(int templateId, Item inventory, String identifier, boolean rename){
        try {
            Item item = ItemFactory.createItem(templateId, 99f, (byte)0, (byte)0, -10L, null);
            if(rename){
                item.setName(item.getName()+" ("+identifier+')');
            }
            inventory.insertItem(item, true, false, false);
            ModelMeImpressed.setCustomModel(item, item.getTemplate().getModelName() + identifier + '.');
        } catch (NoSuchTemplateException | FailedException e){
            KingdomSails.logger.log(Level.SEVERE, "Failed while creating ship.", e);
        }
    }

    private static void createAllCorbitas(Player player, boolean rename){
        for(String identifier : KingdomSails.getInstance().sailsIdentifiers) {
            createShip(ItemList.corbita, player.getInventory(), identifier, rename);
        }
    }

    private static void createAllKnarrs(Player player, boolean rename){
        for(String identifier : KingdomSails.getInstance().sailsIdentifiers) {
            createShip(ItemList.knarr, player.getInventory(), identifier, rename);
        }
    }

    private static void createAllCogs(Player player, boolean rename){
        for(String identifier : KingdomSails.getInstance().sailsIdentifiers) {
            createShip(ItemList.cog, player.getInventory(), identifier, rename);
        }
    }

    private static void createAllSailingShips(Player player, boolean rename){
        for(String identifier : KingdomSails.getInstance().sailsIdentifiers) {
            createShip(ItemList.boatSailing, player.getInventory(), identifier, rename);
        }
    }

    private static void createAllCaravels(Player player, boolean rename){
        for(String identifier : KingdomSails.getInstance().sailsIdentifiers) {
            createShip(ItemList.caravel, player.getInventory(), identifier, rename);
        }
    }

    private static void createAllSails(Player player){
        for(int templateId : KingdomSails.getInstance().sailItemsIds) {
            try {
                Item item = ItemFactory.createItem(templateId, 99f, (byte)0, (byte)0, -10L, null);
                player.getInventory().insertItem(item, true, false, false);
            } catch (NoSuchTemplateException | FailedException e){
                KingdomSails.logger.log(Level.SEVERE, "Failed while creating sail.", e);
            }
        }
    }

    public static MessagePolicy handle(Communicator communicator, String message) {
        if (message.toLowerCase().startsWith("#kingdomsails")) {
            final StringTokenizer tokens = new StringTokenizer(message);
            tokens.nextToken();
            if (tokens.hasMoreTokens()) {
                String cmd = tokens.nextToken().trim().toLowerCase();
                boolean rename = tokens.hasMoreTokens() && tokens.nextToken().trim().equalsIgnoreCase("rename");
                switch (cmd) {
                    case "sail":
                    case "sails":
                        communicator.sendAlertServerMessage("Creating all sails.");
                        createAllSails(communicator.player);
                        return MessagePolicy.DISCARD;
                    case "sailingship":
                    case "sailingships":
                        communicator.sendAlertServerMessage("Creating all sailing ships.");
                        createAllSailingShips(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                    case "cog":
                    case "cogs":
                        communicator.sendAlertServerMessage("Creating all cogs.");
                        createAllCogs(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                    case "corbita":
                    case "corbitas":
                        communicator.sendAlertServerMessage("Creating all corbitas.");
                        createAllCorbitas(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                    case "knarr":
                    case "knarrs":
                        communicator.sendAlertServerMessage("Creating all knarrs.");
                        createAllKnarrs(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                    case "caravel":
                    case "caravels":
                        communicator.sendAlertServerMessage("Creating all caravels.");
                        createAllCaravels(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                    case "allships":
                        communicator.sendAlertServerMessage("Creating all ships of all kinds.");
                        createAllSailingShips(communicator.player, rename);
                        createAllCogs(communicator.player, rename);
                        createAllCorbitas(communicator.player, rename);
                        createAllKnarrs(communicator.player, rename);
                        createAllCaravels(communicator.player, rename);
                        return MessagePolicy.DISCARD;
                }
            }
            communicator.sendAlertServerMessage("Usage:");
            communicator.sendAlertServerMessage(" #kingdomsails sails");
            communicator.sendAlertServerMessage(" #kingdomsails allships");
            communicator.sendAlertServerMessage(" #kingdomsails sailings");
            communicator.sendAlertServerMessage(" #kingdomsails cogs");
            communicator.sendAlertServerMessage(" #kingdomsails corbitas");
            communicator.sendAlertServerMessage(" #kingdomsails knarrs");
            communicator.sendAlertServerMessage(" #kingdomsails caravels");
            communicator.sendAlertServerMessage(" and");
            communicator.sendAlertServerMessage(" #kingdomsails allships rename");
            communicator.sendAlertServerMessage(" #kingdomsails sailings rename");
            communicator.sendAlertServerMessage(" #kingdomsails cogs rename");
            communicator.sendAlertServerMessage(" #kingdomsails corbitas rename");
            communicator.sendAlertServerMessage(" #kingdomsails knarrs rename");
            communicator.sendAlertServerMessage(" #kingdomsails caravels rename");
            return MessagePolicy.DISCARD;
        }
        return MessagePolicy.PASS;
    }
}
