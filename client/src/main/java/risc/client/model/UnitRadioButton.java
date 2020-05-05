package risc.client.model;

import javafx.scene.control.RadioButton;

public class UnitRadioButton extends RadioButton {

  int unitID;

  public UnitRadioButton(int unitID) {
    this.unitID = unitID;
  }

  public UnitRadioButton(String text, int unitID) {
    super(text);
    this.unitID = unitID;
  }

  public int getUnitID() {
    return unitID;
  }

  public void setUnitID(int unitID) {
    this.unitID = unitID;
  }
}
