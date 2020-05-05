package risc.client.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;


public class TerritoryCircle extends Circle implements Territory {

  private String name;
  private int terrID;

  public static class CircleBuilder {
    private String name;
    private int terrID;
    private double centerX;
    private double centerY;
    private double radius;
    private Color color;

    private CircleBuilder() {}

    public static CircleBuilder newInstance() {
      return new CircleBuilder();
    }

    public  CircleBuilder terrID(int terrID) {
      this.terrID = terrID;
      return this;
    }
    public CircleBuilder name(String name) {
      this.name = name;
      return this;
    }
    public CircleBuilder centerX(double centerX) {
      this.centerX = centerX;
      return this;
    }
    public CircleBuilder centerY(double centerY) {
      this.centerY = centerY;
      return this;
    }
    public CircleBuilder radius(double radius) {
      this.radius = radius;
      return this;
    }
    public CircleBuilder color(Color color) {
      this.color = color;
      return this;
    }

    public TerritoryCircle create() {
      return new TerritoryCircle(this);
    }
  }

  private TerritoryCircle(CircleBuilder circleBuilder) {
    setCenterX(circleBuilder.centerX);
    setCenterY(circleBuilder.centerY);
    setRadius(circleBuilder.radius);
    setFill(circleBuilder.color);
    setStroke(Color.BLACK);
    setTerrID(circleBuilder.terrID);
    setId(circleBuilder.name);
    this.terrID = circleBuilder.terrID;
    this.name = circleBuilder.name;
    setOnMouseEntered(event -> {
      System.out.println("Mouse Enter");
      setStrokeWidth(5);
    });

    setOnMouseExited(event -> {
      System.out.println("Mouse Exited");
      setStrokeWidth(1);
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
