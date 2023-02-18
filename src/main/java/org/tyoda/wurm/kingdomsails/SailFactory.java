package org.tyoda.wurm.kingdomsails;

import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.IconConstants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;
import java.util.logging.Level;

public class SailFactory {
    /**
     * Creates a new kingdom sail item template.
     * @param identifier the unique four letter identifier for the model name
     * @param name The name for the item
     * @return the template id of the new sail. -10 if it failed.
     */
    public static int createSailItem(String identifier, String name){
        String modelName = "mod.tyoda.kingdomsails.sail."+identifier+".";
        ItemTemplateBuilder builder = new ItemTemplateBuilder(modelName)
            .name(name, "sails", "A sail used to change a ship's sail into a colorful new version. Surprisingly it fits on any ship you try it on.")
            .modelName(modelName)
            .itemTypes(new short[]{
                ItemTypes.ITEM_TYPE_CLOTH,
                ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_DECORATION
            })
            .imageNumber((short)IconConstants.ICON_CLOTH_BOLT)
            .behaviourType(BehaviourList.itemBehaviour)
            .decayTime(3024000L)
            .dimensions(80, 30, 50)
            .difficulty(55.0F)
            .weightGrams(4300)
            .material(Materials.MATERIAL_COTTON);
        ItemTemplate template = null;

        try {
            template = builder.build();
        } catch (IOException e){
            KingdomSails.logger.log(Level.SEVERE, "Failed while creating "+name, e);
        }

        if(template != null){
            if(KingdomSails.getInstance().sailsCraftable) {
                CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.needleCopper, ItemList.clothYard, template.getTemplateId(), false, true, 0.0F, false, false, CreationCategories.SAILS);
                CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.needleIron,   ItemList.clothYard, template.getTemplateId(), false, true, 0.0F, false, false, CreationCategories.SAILS);
            }

            return template.getTemplateId();
        }else{
            return -10;
        }
    }
}
