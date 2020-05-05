package risc.client.controller.services;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class AnimationService {

    static int totalSteps=100;
    static public void moveAnimation(Pane connectPane,double x1,double y1, double x2, double y2){
        totalSteps=100;

        //final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        Image ufo=new Image("risc/client/view/Image/ufo.png");
        final ImageView ufoView = new ImageView(ufo);

        //let the alteration of height and width preserve the original ratio
        ufoView.setPreserveRatio(true);
        ufoView.setFitHeight(50);

        final double origX=x1-ufoView.getFitWidth()/2;
        final double origY=y1-ufoView.getFitHeight()/2;
        final double nextX=x2-ufoView.getFitWidth()/2;
        final double nextY=y2-ufoView.getFitHeight()/2;
        final double dX=(x2-x1)/(double)totalSteps;
        final double dY=(y2-y1)/(double)totalSteps;

        final GridPane gp = new GridPane();
        gp.add(ufoView,0,0);
        gp.setLayoutX(origX);
        gp.setLayoutY(origY);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        connectPane.getChildren().add(gp);
        KeyFrame kf = new KeyFrame(Duration.seconds(.02), event -> {
            if(totalSteps>=0) {
                gp.setLayoutX(gp.getLayoutX() + dX);
                gp.setLayoutY(gp.getLayoutY() + dY);
                totalSteps--;
            }
            else if(totalSteps>=-24){
                ufoView.setFitHeight(ufoView.getFitHeight()-2);
                gp.setLayoutX(gp.getLayoutX()+1.5);
                gp.setLayoutY(gp.getLayoutY()+1);
                totalSteps--;
            }
            else{
                connectPane.getChildren().remove(gp);
                timeline.stop();
            }
        });



        timeline.getKeyFrames().add(kf);
        timeline.play();
    }


    static int outerTimes=-1;
    static int upgradeState[]=new int [100];
    static int quickCount[]= new int [100];
    static public void upgradeAnimation(Pane connectPane,int prevLevel,int nextLevel,double x, double y){
        if(outerTimes>=99||outerTimes<0){
            outerTimes=0;
        }
        else{
            outerTimes++;
        }
        final int times=outerTimes;

        upgradeState[times]=0;
        quickCount[times]=0;
        Image prevImg=new Image("risc/client/view/Image/level"+prevLevel+".png");
        Image nextImg=new Image("risc/client/view/Image/level"+nextLevel+".png");

        ImageView prevView = new ImageView(prevImg);
        ImageView nextView = new ImageView(nextImg);

        //let the alteration of height and width preserve the original ratio
        prevView.setPreserveRatio(true);
        prevView.setFitHeight(150);
        nextView.setPreserveRatio(true);
        nextView.setFitHeight(150);

        final double origX=x;
        final double origY=y;

        final GridPane prevGp = new GridPane();
        prevGp.add(prevView,0,0);
        prevGp.setLayoutX(origX);
        prevGp.setLayoutY(origY);

        final GridPane nextGp = new GridPane();
        nextGp.add(nextView,0,0);
        nextGp.setLayoutX(origX);
        nextGp.setLayoutY(origY);


        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);


        //Make the image black and white, and convert it back at the end
        Effect origEffect=nextGp.getEffect();
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-1);
        prevGp.setEffect(colorAdjust);
        nextGp.setEffect(colorAdjust);

        connectPane.getChildren().add(prevGp);
        connectPane.getChildren().add(nextGp);
        prevGp.setOpacity(100);
        nextGp.setOpacity(0);
        //prev .05
        KeyFrame kf = new KeyFrame(Duration.seconds(.05), event -> {

            if(quickCount[times]>=12) {
                if (upgradeState[times] <= 30) {
                    if (upgradeState[times] % 2 == 0) {
                        prevGp.setOpacity(0);
                        nextGp.setOpacity(100);

                        /*
                        connectPane.getChildren().remove(prevGp);
                        connectPane.getChildren().add(nextGp);
                        nextGp.toFront();

                         */
                    } else {
                        prevGp.setOpacity(100);
                        nextGp.setOpacity(0);

//                        connectPane.getChildren().remove(nextGp);
//                        connectPane.getChildren().add(prevGp);
//                        prevGp.toFront();
                    }
                }

                if (upgradeState[times] == 31) {
                    prevGp.setEffect(origEffect);
                    nextGp.setEffect(origEffect);
                }
                if (upgradeState[times] == 50) {
                    connectPane.getChildren().remove(prevGp);
                    connectPane.getChildren().remove(nextGp);
                    timeline.stop();
                }
                upgradeState[times]++;
                quickCount[times]=0;
            }

            if(upgradeState[times]<=5){
                quickCount[times]++;
            }
            else if(upgradeState[times]<=10){
                quickCount[times]+=2;
            }
            else if(upgradeState[times]<=20){
                quickCount[times]+=3;
            }
            else {
                quickCount[times]+=6;
            }



        });



        timeline.getKeyFrames().add(kf);
        timeline.play();
    }


    static int fireCount=0;
    public static void boomAnimation(Pane connectPane,double x, double y){
        fireCount=0;
        //final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        Image fire[]=new Image[9];
        ImageView fireView[]=new ImageView[9];
        GridPane gp[]=new GridPane[9];
        for(int i=0;i<9;i++){
            fire[i]=new Image("risc/client/view/Image/fire"+i+".png");
            fireView[i]=new ImageView(fire[i]);

            fireView[i].setPreserveRatio(true);
            fireView[i].setFitHeight(150);

            gp[i]=new GridPane();
            gp[i].add(fireView[i],0,0);
            gp[i].setLayoutX(x);
            gp[i].setLayoutY(y);
        }



        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);



        connectPane.getChildren().add(gp[fireCount]);
        fireCount++;
        KeyFrame kf = new KeyFrame(Duration.seconds(.15), event -> {
            if(fireCount<=8) {
                connectPane.getChildren().remove(gp[fireCount-1]);
                connectPane.getChildren().add(gp[fireCount]);
                fireCount++;
            }
            else{
                connectPane.getChildren().remove(gp[fireCount-1]);
                timeline.stop();
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }


    static int currentMinIdx=0;
    public static void attackAnimation(Pane connectPane,ArrayList<Integer> attackLevel, double x , double y, double destX){


        final ImageView soldierView[]=new ImageView[attackLevel.size()];

        GridPane gp[]=new GridPane[attackLevel.size()];
        for(int i=0;i<attackLevel.size();i++){
            soldierView[i]=new ImageView(new Image("risc/client/view/Image/level"+attackLevel.get(i)+".png"));
            soldierView[i].setPreserveRatio(true);
            soldierView[i].setFitHeight(80);
            gp[i]=new GridPane();
            gp[i].add(soldierView[i],0,0);
            gp[i].setLayoutX(x-50*i);
            gp[i].setLayoutY(y);
            connectPane.getChildren().add(gp[i]);
        }

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        final int sz=attackLevel.size();
        currentMinIdx=0;
        KeyFrame kf = new KeyFrame(Duration.seconds(.02), event -> {
            for(int i=currentMinIdx;i<sz;i++) {
                if(gp[i].getLayoutX()>=destX){
                    connectPane.getChildren().remove(gp[i]);
                    currentMinIdx=i+1;
                    break;
                }
            }

            if(currentMinIdx<sz) {
                for(int i=currentMinIdx;i<sz;i++) {
                    gp[i].setLayoutX(gp[i].getLayoutX() + 2);
                }
            }
            else{
                timeline.stop();
                boomAnimation(connectPane,destX,y-50);
            }

        });

        timeline.getKeyFrames().add(kf);
        timeline.play();


    }


    static int winCount=0;
    public static void winAnimation(Pane connectPane,double x, double y){
        winCount=0;
        //final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        Image win1[]=new Image[8];
        ImageView winView1[]=new ImageView[8];
        GridPane gp1[]=new GridPane[8];

        Image win2[]=new Image[8];
        ImageView winView2[]=new ImageView[8];
        GridPane gp2[]=new GridPane[8];

        Image win3[]=new Image[8];
        ImageView winView3[]=new ImageView[8];
        GridPane gp3[]=new GridPane[8];

        AnimationService.setSeveralFirework(true,win1,winView1,gp1,x-50,y-50);
        AnimationService.setSeveralFirework(true,win2,winView2,gp2,x+70,y-40);
        AnimationService.setSeveralFirework(true,win3,winView3,gp3,x,y);



        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);



        connectPane.getChildren().add(gp1[winCount]);
        connectPane.getChildren().add(gp2[winCount]);
        connectPane.getChildren().add(gp3[winCount]);
        winCount++;
        KeyFrame kf = new KeyFrame(Duration.seconds(.25), event -> {
            if(winCount<=6) {
                connectPane.getChildren().remove(gp1[winCount - 1]);
                connectPane.getChildren().add(gp1[winCount]);
                connectPane.getChildren().remove(gp2[winCount - 1]);
                connectPane.getChildren().add(gp2[winCount]);
                connectPane.getChildren().remove(gp3[winCount - 1]);
                connectPane.getChildren().add(gp3[winCount]);
                winCount++;
            }
            else if (winCount==7){
                connectPane.getChildren().remove(gp1[winCount-1]);
                connectPane.getChildren().add(gp1[winCount]);
                connectPane.getChildren().remove(gp2[winCount-1]);
                connectPane.getChildren().remove(gp3[winCount-1]);
                winCount++;
                //timeline.stop();
            }
            else if(winCount<=12){
                winCount++;
            }
            else{
                connectPane.getChildren().remove(gp1[7]);
                connectPane.getChildren().add(gp1[0]);
                connectPane.getChildren().add(gp2[0]);
                connectPane.getChildren().add(gp3[0]);
                winCount=1;
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }



    public static void loseAnimation(Pane connectPane,double x, double y){
        winCount=0;
        //final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
        Image win1[]=new Image[8];
        ImageView winView1[]=new ImageView[8];
        GridPane gp1[]=new GridPane[8];

        Image win2[]=new Image[8];
        ImageView winView2[]=new ImageView[8];
        GridPane gp2[]=new GridPane[8];

        Image win3[]=new Image[8];
        ImageView winView3[]=new ImageView[8];
        GridPane gp3[]=new GridPane[8];

        AnimationService.setSeveralFirework(false,win1,winView1,gp1,x-50,y-50);
        AnimationService.setSeveralFirework(false,win2,winView2,gp2,x+70,y-40);
        AnimationService.setSeveralFirework(false,win3,winView3,gp3,x,y);



        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);



        connectPane.getChildren().add(gp1[winCount]);
        connectPane.getChildren().add(gp2[winCount]);
        connectPane.getChildren().add(gp3[winCount]);
        winCount++;
        KeyFrame kf = new KeyFrame(Duration.seconds(.25), event -> {
            if(winCount<=6) {
                connectPane.getChildren().remove(gp1[winCount - 1]);
                connectPane.getChildren().add(gp1[winCount]);
                connectPane.getChildren().remove(gp2[winCount - 1]);
                connectPane.getChildren().add(gp2[winCount]);
                connectPane.getChildren().remove(gp3[winCount - 1]);
                connectPane.getChildren().add(gp3[winCount]);
                winCount++;
            }
            else if (winCount==7){
                connectPane.getChildren().remove(gp1[winCount-1]);
                connectPane.getChildren().add(gp1[winCount]);
                connectPane.getChildren().remove(gp2[winCount-1]);
                connectPane.getChildren().remove(gp3[winCount-1]);
                winCount++;
                //timeline.stop();
            }
            else if(winCount<=12){
                winCount++;
            }
            else{
                connectPane.getChildren().remove(gp1[7]);
                connectPane.getChildren().add(gp1[0]);
                connectPane.getChildren().add(gp2[0]);
                connectPane.getChildren().add(gp3[0]);
                winCount=1;
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
    public static void setSeveralFirework(boolean bol,Image win[],
            ImageView winView[],
            GridPane gp[],double x, double y){
        for(int i=0;i<7;i++){
            win[i]=new Image("risc/client/view/Image/firework"+i+".png");
            winView[i]=new ImageView(win[i]);

            winView[i].setPreserveRatio(true);
            winView[i].setFitHeight(350);

            gp[i]=new GridPane();
            gp[i].add(winView[i],0,0);
            gp[i].setLayoutX(x);
            gp[i].setLayoutY(y);
        }
        int i=7;
        if(bol){
            win[i]=new Image("risc/client/view/Image/win.png");
        }
        else {
            win[i]=new Image("risc/client/view/Image/lose.png");
        }

        winView[i]=new ImageView(win[i]);

        winView[i].setPreserveRatio(true);
        winView[i].setFitHeight(100);

        gp[i]=new GridPane();
        gp[i].add(winView[i],0,0);
        gp[i].setLayoutX(x+30);
        gp[i].setLayoutY(y+50);
    }
}


