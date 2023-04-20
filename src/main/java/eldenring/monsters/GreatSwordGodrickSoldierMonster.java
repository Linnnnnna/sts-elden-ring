package eldenring.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateJumpAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eldenring.EldenRingSTS;

public class GreatSwordGodrickSoldierMonster extends BaseMonster {
    public final static String NAME = "Godrick Soldier GreatSword";
    public final static String FAKE_ID = "GodrickSoldierGreatSword";
    public final static String ID = EldenRingSTS.makeID(FAKE_ID);

    private int slash = 10;
    private int stomp = 12;
    private int block = 5;
    private int turnMove = 0;

    public GreatSwordGodrickSoldierMonster(float offX, float offY) {
        super(NAME, ID, 26, 0.0F, 0.0F, 130.0F, 248.0F, EldenRingSTS.monsterPath("GodrickSoldier"), offX, offY);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.stomp += 3;
            this.slash += 2;
        }
    }

    @Override
    public void takeTurn() {
        switch (this.turnMove) {
            case 0 :
                stomp();
                calcNextMove();
                break;
            case 1:
                slash();
                calcNextMove();
                break;
            case 2:
                defense();
                calcNextMove();
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        calcNextMove();
    }

    private void calcTurn(){
        int nextMov = (int) (Math.random() * 2);
        if(nextMov == this.turnMove) {
            calcTurn();
        }
        this.turnMove = nextMov;
    }

    private void stomp(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.stomp), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private void slash(){
        AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.slash), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    private void defense(){
        AbstractDungeon.actionManager.addToBottom(new AnimateJumpAction(this));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, block));
    }

    private void calcNextMove(){
        calcTurn();
        switch (this.turnMove) {
            case 0:
                this.setMove((byte) 1, Intent.ATTACK_DEBUFF, stomp);
                break;
            case 1:
                this.setMove((byte) 2, Intent.ATTACK, slash);
                break;
            case 2:
                this.setMove((byte) 3, Intent.DEFEND, block);
                break;
        }
    }
}
