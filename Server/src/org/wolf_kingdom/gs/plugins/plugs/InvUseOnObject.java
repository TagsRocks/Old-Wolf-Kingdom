package org.wolf_kingdom.gs.plugins.plugs;

import java.util.Arrays;

import org.wolf_kingdom.config.Formulae;
import org.wolf_kingdom.gs.event.SingleEvent;
import org.wolf_kingdom.gs.model.Bubble;
import org.wolf_kingdom.gs.model.GameObject;
import org.wolf_kingdom.gs.model.InvItem;
import org.wolf_kingdom.gs.model.Player;
import org.wolf_kingdom.gs.model.World;
import org.wolf_kingdom.gs.plugins.listeners.action.InvUseOnObjectListener;
import org.wolf_kingdom.gs.tools.DataConversions;

public class InvUseOnObject implements InvUseOnObjectListener {
	
	static int[] objectIDs;
	static {
		objectIDs = new int[] { 2, 466, 814, 48, 26, 86, 1130 };
		Arrays.sort(objectIDs);
	}
	@Override
	public void onInvUseOnObject(GameObject obj, InvItem item, Player owner) {
		if(Arrays.binarySearch(objectIDs, obj.getID()) >= 0) {
			handleRefill(owner, item);
			return;
		}
	 }
	 private void handleRefill(Player owner, final InvItem item) {
	    	if (!itemId(new int[] { 21, 140, 465 }, item) && !itemId(Formulae.potionsUnfinished, item) && !itemId(Formulae.potions1Dose, item) && !itemId(Formulae.potions2Dose, item) && !itemId(Formulae.potions3Dose, item)) {
				owner.getActionSender().sendMessage("Nothing interesting happens.");
				return;
			    }
		    if (owner.getInventory().remove(item) > -1) {
		    	owner.setBusy(true);
				showBubble(owner, item);
				owner.getActionSender().sendSound("filljug");
				switch (item.getID()) {
					case 21:
					    owner.getInventory().add(new InvItem(50));
					    break;
					case 140:
					    owner.getInventory().add(new InvItem(141));
					    break;
					default:
					    owner.getInventory().add(new InvItem(464));
					    break;
				}
				owner.getActionSender().sendMessage("You fill the " + item.getDef().getName());
				owner.getActionSender().sendInventory();
				if(owner.getInventory().hasItemId(item.getID())) {
					World.getWorld().getDelayedEventHandler().add(new SingleEvent(owner, 200) {
					    public void action() {
					    	owner.setBusy(false);
					    	handleRefill(owner, item);
					    }
					});
				}
				else {
					owner.setBusy(false);
				}
		    }
			    
	    }
	private boolean itemId(int[] ids, InvItem item) {
		return DataConversions.inArray(ids, item.getID());
	}
	private void showBubble(Player owner, InvItem item) {
		Bubble bubble = new Bubble(owner, item.getID());
		for (Player p : owner.getViewArea().getPlayersSectorA()) {
		    p.informOfBubble(bubble);
		}
                for (Player p : owner.getViewArea().getPlayersSectorB()) {
		    p.informOfBubble(bubble);
		}
                for (Player p : owner.getViewArea().getPlayersSectorC()) {
		    p.informOfBubble(bubble);
		}
                for (Player p : owner.getViewArea().getPlayersSectorD()) {
		    p.informOfBubble(bubble);
		}
	}
}
