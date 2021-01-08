import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import java.io.IOException;
import java.util.List;
import java.awt.*;

import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.model.Character;
import org.osbot.rs07.api.model.NPC;

@ScriptManifest(author = "here", info = "#2, do i know what im doing?", name = "Kharid Fisher", version = 0, logo = "")
public class KharidFisher extends Script { 
    
    @Override
    public void onStart() {
        log("LETS GOOOOOOOOOO");
    }
    
    private State getState() {
        if (inventory.getEmptySlotCount() == 0 || inventory.getItem("Small fishing net") == null) {
            return State.BANK;
        }
        return State.FISH;
    }
   
    @Override
    public int onLoop() throws InterruptedException {
        switch (getState()) {
            case BANK:
                walkToBank();
                bank();
                break;
            case FISH:
                if (myPlayer().isAnimating()) {
                    log("Patience");
                    
                    while(myPlayer().isAnimating()) {
                        sleep(1000);
                    }
                    break;
                }
                goFish();
                break;
        }
        return random(200,300);
    }
    
    private void walkToBank() throws InterruptedException {
        Position bankPosition = new Position(3270, 3167,0);  
        WebWalkEvent runToBank = new WebWalkEvent(bankPosition);
        runToBank.setEnergyThreshold(20);
        log("Running to bank");
        execute(runToBank);
        sleep(random(200,500));
    }
    
    private void walkToClose() throws InterruptedException {
        Position closePosition = new Position(3273, 3148,0); 
        WebWalkEvent runToClose = new WebWalkEvent(closePosition);
        runToClose.setEnergyThreshold(20);
        log("Running to closer fishing spots");
        execute(runToClose);
        sleep(random(200,500));
    }

    private void walkToFar() throws InterruptedException {
        Position farPosition = new Position(3280, 3143,0); 
        WebWalkEvent runToFar = new WebWalkEvent(farPosition);
        runToFar.setEnergyThreshold(20);
        log("Running to farther fishing spots");
        execute(runToFar);
        sleep(random(200,500));
    }
    
    private void bank() throws InterruptedException {
        log("banking");
        Entity booth = objects.closest("Bank Booth");
        booth.interact("Bank");
        sleep(1000);
        bank.depositAll();
        sleep(1000);
        bank.withdraw("Small fishing net", 1);
        sleep(1000);
        bank.close();
    }
    
    private void goFish() throws InterruptedException {
        Entity spot = getNpcs().closest("Fishing spot");
        if (spot != null) {
            spot.interact("Net");
            log("starting to fish");
            sleep(random(500,1000));
        } else {
            //do  {
                walkToClose();
                spot =  getNpcs().closest("Fishing spot");
                if (spot != null) {
                    spot.interact("Net");
                    log("starting to fish");
                    sleep(random(500,1000));
                } else {
                    walkToFar();
                    spot = getNpcs().closest("Fishing spot");
                    if (spot != null) {
                        spot.interact("Net");
                        log("starting to fish");
                        sleep(random(500,1000));
                    }
                }
            //} while(spot == null);
        }
    }
    
    @Override
        public void onExit() {
        log("L8R SK8R");
    }
    
    private enum State {
        BANK, FISH
    }

        @Override
        public void onPaint(Graphics2D g) { //GUI IS FOR SKRABS

    }
    
}
    