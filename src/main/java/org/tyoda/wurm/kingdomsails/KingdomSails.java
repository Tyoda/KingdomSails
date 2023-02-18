package org.tyoda.wurm.kingdomsails;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

public class KingdomSails implements WurmServerMod, Versioned, ItemTemplatesCreatedListener, PreInitable, ServerStartedListener, Configurable, PlayerMessageListener {
    public static final Logger logger = Logger.getLogger(KingdomSails.class.getName());
    public static final String version = "1.0";

    public  boolean sailsCraftable = true;

    public  boolean gmsNoUseupSail = true;

    public  final ArrayList<Integer> sailItemsIds = new ArrayList<>();

    public  final ArrayList<String> sailsIdentifiers = new ArrayList<>();

    private static KingdomSails instance = null;

    public KingdomSails(){
        instance = this;
    }

    @Override
    public void configure(Properties properties) {
        sailsCraftable = Boolean.parseBoolean(properties.getProperty("sailsCraftable", String.valueOf(sailsCraftable)));
        gmsNoUseupSail = Boolean.parseBoolean(properties.getProperty("gmsNoUseupSail", String.valueOf(gmsNoUseupSail)));
    }

    @Override
    public void preInit() {
        ModActions.init();
    }

    @Override
    public void onItemTemplatesCreated() {
        addNewSail("jenn", "Jenn-Kellon sail");
        addNewSail("molr", "Mol-Rehan sail");
        addNewSail("hots", "Horde of the Summoned sail");
        addNewSail("zjen", "Dragon Kingdom sail");
        addNewSail("empi", "Empire of Mol-Rehan sail");
        addNewSail("blac", "Black Legion sail");
        addNewSail("ebon", "Ebonaura sail");
        addNewSail("king", "Kingdom of Sol sail");
        addNewSail("ther", "The Roman Republic sail");
        addNewSail("mace", "Macedonia sail");
        addNewSail("drea", "Dreadnought sail");
        addNewSail("thec", "The Crusaders sail");
        addNewSail("pand", "Pandemonium sail");
        addNewSail("legi", "Legion of Anubis sail");
        addNewSail("wurm", "Wurm University sail");
        addNewSail("yval", "Valhalla Descendants sail");
        addNewSail("apoc", "Apocalypse Order sail");
        addNewSail("abra", "Abralon sail");
        addNewSail("comm", "The Commonwealth sail");
        addNewSail("valh", "Valhalla sail");
        addNewSail("free", "Freedom sail");
    }

    /**
     * Add a new sail type.
     * @param identifier An identifier for the sail type. It should be a single alphaNumeric word.
     * @param name The name of the sail type.
     */
    public void addNewSail(String identifier, String name){
        int templateId = SailFactory.createSailItem(identifier, name);
        if(templateId != -10) {
            sailItemsIds.add(templateId);
            sailsIdentifiers.add(identifier);
        }
    }

    @Override
    public void onServerStarted() {
        ModActions.registerAction(new ChangeSailAction());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onPlayerMessage(Communicator communicator, String s) {
        return false;
    }

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        if (message.startsWith("#") && communicator.getPlayer().getPower() >= 4)
            return GmCommands.handle(communicator, message);
        return MessagePolicy.PASS;
    }

    public static KingdomSails getInstance(){
        return instance;
    }

    @Override
    public String getVersion(){
        return version;
    }
}