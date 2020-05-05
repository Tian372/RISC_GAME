package risc.client.model;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.SVGPath;
import javafx.stage.Popup;


public class TerritorySVG extends SVGPath implements Territory {

  private String name;
  private int terrID;

  public static class SVGBuilder {
    private String name;
    private int terrID;
    private String content;
    private Color fillColor;
    private Color strokeColor;

    private SVGBuilder() {}

    public static SVGBuilder newInstance() {
      return new SVGBuilder();
    }

    public  SVGBuilder terrID(int terrID) {
      this.terrID = terrID;
      return this;
    }
    public SVGBuilder name(String name) {
      this.name = name;
      return this;
    }
    public SVGBuilder strokeColor(Color color) {
      this.strokeColor = color;
      return this;
    }
    public SVGBuilder fillColor(Color color) {
      this.fillColor = color;
      return this;
    }
    public SVGBuilder content(String content) {
      this.content = content;
      return this;
    }
    public TerritorySVG create() {
      return new TerritorySVG(this);
    }
  }

  private TerritorySVG(SVGBuilder svgBuilder) {
    setName(svgBuilder.name);
    setTerrID(svgBuilder.terrID);

//    setFill(Color.TRANSPARENT);
    setFill(new ImagePattern(new Image("file:src/main/resources/risc/client/view/assets/territory.jpg"), -300, -300, 1500, 1500, false));

    setStroke(svgBuilder.strokeColor);
    setId(svgBuilder.name);
    this.terrID = svgBuilder.terrID;
    this.name = svgBuilder.name;
    setContent(svgBuilder.content);

    Tooltip tooltip = new Tooltip(svgBuilder.name);
    Tooltip.install(this, tooltip);

    setStrokeWidth(3);

    setOnMouseEntered(event -> {
      System.out.println("Mouse Enter");
      setStrokeWidth(6);
    });

    setOnMouseExited(event -> {
      System.out.println("Mouse Exited");
      setStrokeWidth(3);
    });







  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTerrID() {
    return terrID;
  }

  public void setTerrID(int terrID) {
    this.terrID = terrID;
  }
}
