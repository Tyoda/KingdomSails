package org.tyoda.wurm.kingdomsails;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.tyoda.wurm.modelmeimpressed.ModelMeImpressed;

import java.util.Collections;
import java.util.List;

public class ChangeSailAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId = (short) ModActions.getNextActionId();
    private final ActionEntry actionEntry;

    public static final int[] shipIds = new int[]{
            491, /* sailing boa */ 540, /* cog */ 541, /* corbita */
            542, /* knarr */ 543  /* caravel */ /*490, /* rowing boat */
    };


    public ChangeSailAction() {
        this.actionEntry = ActionEntry.createEntry(this.actionId, "Change sail", "changing sail",
            new int[]{
                    Actions.ACTION_TYPE_IGNORERANGE,
                    Actions.ACTION_TYPE_NOMOVE,
                    Actions.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
            }
        );
        ModActions.registerAction(this.actionEntry);
    }

    public short getActionId() {
        return this.actionId;
    }

    public ActionEntry getActionEntry() {
        return this.actionEntry;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Item target) {
        if(performer != null && performer.isPlayer()
                && canChangeSail(target.getTemplateId())
                && KingdomSails.getInstance().sailItemsIds.contains(subject.getTemplateId())) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        if(performer != null && performer.isPlayer()
                && canChangeSail(target.getTemplateId())
                && KingdomSails.getInstance().sailItemsIds.contains(source.getTemplateId())) {
            return doAction(action, performer, source, target, counter);
        }
        return true;
    }

    private boolean doAction(Action action, Creature performer, Item source, Item target, float counter){

        if (counter == 1.0f) {
            float distance = Math.max(
                    Math.abs(performer.getPosX()-target.getPosX()),
                    Math.abs(performer.getPosY()-target.getPosY())
            );
            if(distance > 8) {
                performer.getCommunicator().sendNormalServerMessage("You are too far away to change the sails on that ship.");
                return true;
            }
            if(!target.mayManage(performer)){
                performer.getCommunicator().sendNormalServerMessage("You do not have permission to change sails on this ship.");
                return true;
            }

            int time = (performer.getPower() >= 4 ? 5 : 100);
            action.setTimeLeft(time);
            performer.sendActionControl("Applying new sail", true, time);
            performer.getCommunicator().sendNormalServerMessage("You start to apply the new sail.");
            Server.getInstance().broadCastAction(performer.getName() + " starts to apply the new sail.", performer, 5);
        }
        else {
            int time = action.getTimeLeft();

            if (counter * 10 > time) {
                String modelName = source.getTemplate().getModelName();
                String variant;
                if (modelName.endsWith(".")) {
                    variant = modelName.substring(0, modelName.length() - 1);
                    variant = modelName.substring(variant.lastIndexOf('.') + 1);
                } else {
                    variant = modelName.substring(modelName.lastIndexOf('.')) + '.';
                }
                ModelMeImpressed.setCustomModel(target, target.getTemplate().getModelName() + variant);

                if (performer.getPower() < 4 && KingdomSails.getInstance().gmsNoUseupSail) {
                    Items.destroyItem(source.getWurmId(), false, false);
                    performer.getCommunicator().sendNormalServerMessage("You apply the sail kit to the ship.");
                } else {
                    performer.getCommunicator().sendNormalServerMessage("Your godly powers mean you can keep the sail kit after using it.");
                }
                performer.getCommunicator().sendNormalServerMessage("The old sail was torn up as you took it off. You discard it.");

                return true;
            }
        }
        return false;
    }



    public static boolean canChangeSail(int templateId){
        switch (templateId){
            case ItemList.boatSailing:
            case ItemList.cog:
            case ItemList.corbita:
            case ItemList.knarr:
            case ItemList.caravel:
                return true;
        }
        return false;
    }
}
