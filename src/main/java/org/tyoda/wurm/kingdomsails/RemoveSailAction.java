package org.tyoda.wurm.kingdomsails;

import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.tyoda.wurm.modelmeimpressed.ModelMeImpressed;

import java.util.Collections;
import java.util.List;

public class RemoveSailAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId = (short) ModActions.getNextActionId();
    private final ActionEntry actionEntry;

    public RemoveSailAction() {
        this.actionEntry = ActionEntry.createEntry(this.actionId, "Remove sail", "removing sail",
                new int[]{
                        Actions.ACTION_TYPE_IGNORERANGE,
                        Actions.ACTION_TYPE_NOMOVE
                }
        );
        ModActions.registerAction(this.actionEntry);
    }

    public short getActionId() {
        return this.actionId;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Item target) {
        return getBehavioursFor(performer, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        if(performer != null && performer.isPlayer()
                && ChangeSailAction.canChangeSail(target.getTemplateId())
                && ModelMeImpressed.getCustomModel(target) != null) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        if(performer != null && performer.isPlayer()
                && ChangeSailAction.canChangeSail(target.getTemplateId())
                && ModelMeImpressed.getCustomModel(target) != null) {
            return doAction(action, performer, target, counter);
        }
        return true;
    }

    private boolean doAction(Action action, Creature performer, Item target, float counter){

        if (counter == 1.0f) {
            float distance = Math.max(
                    Math.abs(performer.getPosX()-target.getPosX()),
                    Math.abs(performer.getPosY()-target.getPosY())
            );
            if(distance > 8) {
                performer.getCommunicator().sendNormalServerMessage("You are too far away to remove the sails from ship.");
                return true;
            }
            if(!target.mayManage(performer)){
                performer.getCommunicator().sendNormalServerMessage("You do not have permission to change the sails on this ship.");
                return true;
            }

            int time = (performer.getPower() >= 4 ? 5 : 100);
            action.setTimeLeft(time);
            performer.sendActionControl("Removing sail", true, time);
            performer.getCommunicator().sendNormalServerMessage("You start to remove the sail.");
            Server.getInstance().broadCastAction(performer.getName() + " starts to remove the sail.", performer, 5);
        }
        else {
            int time = action.getTimeLeft();

            if (counter * 10 > time) {
                ModelMeImpressed.deleteEntry(target.getWurmId());
                return true;
            }
        }
        return false;
    }
}
