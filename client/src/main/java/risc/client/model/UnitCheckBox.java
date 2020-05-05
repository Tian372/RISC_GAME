package risc.client.model;

import com.jfoenix.controls.JFXCheckBox;

public class UnitCheckBox extends JFXCheckBox {

    int unitID;

    public UnitCheckBox(int unitID) {
        this.unitID = unitID;
    }

    public UnitCheckBox(String text, int unitID) {

        super(text);
        this.unitID = unitID;
        this.setStyle(" -jfx-checked-color: #2980b9; -jfx-unchecked-color: #2980b9;");
    }

    public int getUnitID() {
        return unitID;
    }
}
