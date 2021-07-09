package anovaexpress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * ANOVA Express
 * @author Mikec
 * @version 1.0
 */
public class ANOVAExpress extends Application {
    private int nx = 0; 
    private int ny = 0;
    private HashMap<Integer, TextField> xindex = new HashMap<Integer, TextField>(); 
    private HashMap<Integer, TextField> yindex = new HashMap<Integer, TextField>(); 
    private HashMap<Integer, HashMap<Integer,TextField>> dindex = new HashMap<>();
    private HashMap<Integer, TextField> xavg = new HashMap<Integer,TextField>();
    private HashMap<Integer, TextField> yavg = new HashMap<Integer,TextField>();
    private HashMap<Integer, TextField> xalfa = new HashMap<Integer,TextField>();
    private HashMap<Integer, TextField> yalfa = new HashMap<Integer, TextField>(); 
    private TextField idxavg = new TextField();
    private TextField idyavg = new TextField(); 
    private TextField idxalpha = new TextField();
    private TextField idyalpha = new TextField(); 
    private ArrayList<Double> colavgs = new ArrayList<>(); 
    private ArrayList<Double> rowavgs = new ArrayList<>();
    private Double avg;
    private TextField tavg = new TextField();
    private Double original[][]; 
    private Double cssa; 
    private Double rssa; 
    private TextField tcssa = new TextField();
    private TextField trssa = new TextField();
    private ArrayList<Double> sse = new ArrayList<Double>(); 
    private Double sst;
    
    private TextField ssa = new TextField(); 
    
    private Button plusCol = new Button();
    private Button minusCol = new Button();
    private Button plusRow = new Button(); 
    private Button minusRow = new Button(); 
    private Button once = new Button();
    private Button calc = new Button(); 
    private Button random = new Button();
    private Button go = new Button(); 
    private Button anova = new Button(); 
    
    
    private Button refresh = new Button(); 
    private Button errorsSSE = new Button(); 
    private Button errorsSST = new Button();

    private TextField goCol = new TextField(); 
    private TextField goRow = new TextField();
    private TextField point = new TextField();
    
    private ScrollPane root = new ScrollPane();
    private GridPane net = new GridPane();
    
    {
            ssa.setText("SSA");
            idxavg.setText("AVG");
            idyavg.setText("AVG");
            idxalpha.setText("Alpha");
            idyalpha.setText("Alpha");
            idxavg.setEditable(false);
            idyavg.setEditable(false);
            idxalpha.setEditable(false);
            idyalpha.setEditable(false);
            tavg.setEditable(false);
            ssa.setEditable(false);
            tcssa.setEditable(false);
            trssa.setEditable(false);
            idyavg.setStyle("-fx-background-color: transparent;");
            idxavg.setStyle("-fx-background-color: transparent;");
            idyalpha.setStyle("-fx-background-color: transparent;");
            idxalpha.setStyle("-fx-background-color: transparent;");
            ssa.setStyle("-fx-background-color: transparent;");
            tcssa.setStyle("-fx-background-color: red;");
            trssa.setStyle("-fx-background-color: red;");
            tavg.setStyle("-fx-background-color: gold;");
            idxavg.setPrefHeight(30);
            idxavg.setPrefWidth(100);
            idyavg.setPrefHeight(30);
            idyavg.setPrefWidth(100);
            idxalpha.setPrefHeight(30);
            idxalpha.setPrefWidth(100);
            idyalpha.setPrefHeight(30);
            idyalpha.setPrefWidth(100);
            tavg.setPrefHeight(30);
            tavg.setPrefWidth(100);
            ssa.setPrefHeight(30);
            ssa.setPrefWidth(100);
            tcssa.setPrefHeight(30);
            tcssa.setPrefWidth(100);
            trssa.setPrefHeight(30);
            trssa.setPrefWidth(100);
    }
    
    public void clearCalc(GridPane net){
        net.getChildren().remove(idxavg); 
        net.getChildren().remove(idyavg);
        net.getChildren().remove(idxalpha); 
        net.getChildren().remove(idyalpha);
        net.getChildren().remove(tavg);
        net.getChildren().remove(ssa);
        net.getChildren().remove(tcssa);
        net.getChildren().remove(trssa);
        net.getChildren().removeAll(xavg.values());
        net.getChildren().removeAll(yavg.values());
        net.getChildren().removeAll(xalfa.values());
        net.getChildren().removeAll(yalfa.values());
        xavg.clear();
        yavg.clear();
        xalfa.clear();
        yalfa.clear();
        clearAvg();
    }
    
    
    public void clearAvg(){
        sse = new ArrayList<>();
        colavgs = new ArrayList<>();
        rowavgs = new ArrayList<>();
        avg = null; 
        original = null;
        cssa = null; 
        rssa = null;
        sst = null;
    }
    
    public void calcAvg(){
        double av = 0; 
        original = new Double[nx][ny];
        ArrayList<Double> ravg = new ArrayList<>();
        ArrayList<Double> cavg = new ArrayList<>();
        try{
            for(int i=0; i<ny; i++){
                ravg.add(0d);
            }
            for(int i=0; i<nx; i++){
                double x = 0; 
                HashMap<Integer,TextField> hm = dindex.get(i);
                for(int j=0; j<ny; j++){
                    TextField tf = hm.get(j); 
                    double r = Double.parseDouble(tf.getText());
                    x+=r;
                    av+=r; 
                    
                    original[i][j]=r;
                    ravg.set(j, ravg.get(j)+r);
                    
                }
                x/=ny;
                cavg.add(x);
            }
            for(int j=0; j<ny; j++){
                ravg.set(j, ravg.get(j)/nx);
            }
            av/=nx*ny; 
        }catch(Exception ex){
            return; 
        }
        colavgs = cavg; 
        rowavgs = ravg;
        avg = av; 
    }
    
    public void setCalc(GridPane net){
        calcAvg();
        net.add(idxavg,1,ny+2);
        net.add(idyavg,nx+2,1);
        net.add(idxalpha,1,ny+3);
        net.add(idyalpha,nx+3,1);
        cssa = 0d; 
        rssa = 0d; 
        for(int i=0; i<nx; i++){
            TextField text = new TextField(); 
            if(colavgs.size()==nx)text.setText(colavgs.get(i).toString());
            text.setEditable(false);
            text.setStyle("-fx-background-color: yellow;");
            text.setPrefHeight(30);
            text.setPrefWidth(100);
            xavg.put(i,text);
            net.add(text,i+2,ny+2);
            
            text = new TextField(); 
            if(colavgs.size()==nx) text.setText(((Double)(colavgs.get(i)-avg)).toString());
            if(colavgs.size()==nx) cssa += (colavgs.get(i)-avg)*(colavgs.get(i)-avg);
            text.setEditable(false);
            text.setStyle("-fx-background-color: orange;");
            text.setPrefHeight(30);
            text.setPrefWidth(100);
            xalfa.put(i,text);
            net.add(text,i+2,ny+3);
        }
        for(int j=0; j<ny; j++){
            TextField text = new TextField(); 
            if(rowavgs.size()==ny)text.setText(rowavgs.get(j).toString());
            text.setEditable(false);
            text.setStyle("-fx-background-color: yellow;");
            text.setPrefHeight(30);
            text.setPrefWidth(100);
            yavg.put(j,text);
            net.add(text,nx+2,j+2);
            
            text = new TextField(); 
            if(rowavgs.size()==ny)text.setText(((Double)(rowavgs.get(j)-avg)).toString());
            if(rowavgs.size()==ny)rssa += (rowavgs.get(j)-avg)*(rowavgs.get(j)-avg);
            text.setEditable(false);
            text.setStyle("-fx-background-color: orange;");
            text.setPrefHeight(30);
            text.setPrefWidth(100);
            yalfa.put(j,text);
            net.add(text,nx+3,j+2);
        }
        tavg.setText((avg==null)?"":avg.toString());
        net.add(tavg, nx+2,ny+2);
        net.add(ssa,nx+3,ny+3);
        cssa*=nx;
        rssa*=ny; 
        if(cssa!=null) tcssa.setText(cssa.toString());
        if(rssa!=null) trssa.setText(rssa.toString());
        net.add(tcssa,nx+2,ny+3);
        net.add(trssa,nx+3,ny+2);
    }
    
    @Override
    public void start(Stage primaryStage) { 
        plusCol.setText("Додај колону");
        minusCol.setText("Уклони колону");
        plusRow.setText("Додај ред");
        minusRow.setText("Уклони ред");
        calc.setText("Израчунај");
        once.setText("Јединице");
        random.setText("Случајно");
        go.setText("Позиционирај");
        once.setText("Јединице");
        goCol.setText("0");
        goRow.setText("0");
        
        refresh.setText("Освежи");
        errorsSSE.setText("Грешке SSE");
        errorsSST.setText("Грешке SST");
        
        anova.setText("ANOVA");
        
        point.setText("М\\А");
        point.setEditable(false);
        point.setStyle("-fx-background-color: transparent;"); 
        
        plusCol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                TextField text = new TextField(); 
                text.setPrefHeight(30);
                text.setPrefWidth(100);
                text.setText(""+(nx+1));
                text.setStyle("-fx-background-color: transparent;");
                text.setEditable(false);
                xindex.put(nx,text);
                net.add(text, nx+2, 1); 
                HashMap<Integer,TextField> col= new HashMap<>();
                for(int i=0; i<ny; i++){
                    TextField txt = new TextField(); 
                    txt.setPrefHeight(30);
                    txt.setPrefWidth(100);
                    txt.setText("1");
                    final int y = i;
                    final int fnx = nx+1;
                    txt.focusedProperty().addListener((e,o,n)->{
                        primaryStage.setTitle("Anova Express ["+(fnx)+","+(y+1)+"]");
                    });
                    col.put(i,txt);
                    net.add(txt, nx+2, i+2);
                }
                dindex.put(nx,col);
                ++nx; 
            }
        });
        minusCol.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                if(nx>0){
                    net.getChildren().remove(xindex.remove(nx-1));
                    for(TextField t : dindex.remove(nx-1).values()){
                        net.getChildren().remove(t);
                    }
                    --nx;
                }
            }
        });
        minusRow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                if(ny>0){
                    net.getChildren().remove(yindex.remove(ny-1));
                    for(int i=0; i<nx; i++){
                        TextField t = dindex.get(i).remove(ny-1);
                        net.getChildren().remove(t);
                    }
                    --ny;
                }
            }
        });
        plusRow.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                TextField text = new TextField(); 
                text.setPrefHeight(30);
                text.setPrefWidth(100);
                text.setText(""+(ny+1));
                text.setStyle("-fx-background-color: transparent;");
                text.setEditable(false);
                yindex.put(ny,text);
                net.add(text, 1,ny+2);
                for(int i=0; i<nx; i++){
                    TextField txt = new TextField(); 
                    txt.setPrefHeight(30);
                    txt.setPrefWidth(100);
                    txt.setText("1");
                    final int x = i;
                    final int fny = ny+1; 
                    txt.focusedProperty().addListener((e,o,n)->{
                        primaryStage.setTitle("Anova Express ["+(x+1)+","+(fny)+"]");
                    });
                    dindex.get(i).put(ny,txt);
                    net.add(txt, i+2, ny+2);
                }
                ++ny; 
                
            }
        });
        calc.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                setCalc(net);
            }
        });
        random.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                 clearCalc(net);
                 for(HashMap<Integer,TextField> hm : dindex.values())
                    for(TextField tf : hm.values()){
                        tf.setText(""+((double)Math.abs(new Random().nextInt()%100000))/1000);
                    }
            }
        });
        once.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                clearCalc(net);
                for(HashMap<Integer,TextField> hm : dindex.values())
                    for(TextField tf : hm.values()){
                        tf.setText("1");
                    }
            }
        });
        go.setOnAction(new EventHandler<ActionEvent>() {            
            @Override
            public void handle(ActionEvent event) {
                try{
                    Integer x = Integer.parseInt(goCol.getText());
                    Integer y = Integer.parseInt(goRow.getText());
                    if(x<1 || x>nx || y<1 || y>ny){
                        throw new RuntimeException();
                    }
                    TextField tf = dindex.get(x-1).get(y-1);
                    root.setHvalue(tf.getLayoutX());
                    root.setVvalue(tf.getLayoutY());
                    Platform.runLater(()->tf.requestFocus());
                }catch(Exception ex){
                    Alert err = new Alert(AlertType.ERROR);
                    err.setTitle("ANOVA Express");
                    err.setHeaderText(null);
                    err.setContentText("Није могуће позиционирати се на наведену позицију.");
                    err.showAndWait(); 
                }
            }
        });
        
        refresh.setOnAction(new EventHandler<ActionEvent>() {    
            @Override
            public void handle(ActionEvent event) {
                if(original==null){
                    Alert err = new Alert(AlertType.ERROR);
                    err.setTitle("ANOVA Express");
                    err.setHeaderText("Оригинали нису доступни.");
                    err.setContentText("После рачунања су доступни до прве модификације операцијом.");
                    err.showAndWait(); 
                }else{
                    for(int i=0; i<nx; i++){
                        HashMap<Integer, TextField> hm = dindex.get(i); 
                        for(int j=0; j<ny; j++){
                             TextField tf = hm.get(j);
                              if(original!=null && original[i][j]!=null)
                                 tf.setText(((Double)(original[i][j])).toString());
                             else 
                                 tf.setText("");
                       }
                    }
                }
            }
        });
        
        
        errorsSSE.setOnAction(new EventHandler<ActionEvent>() {    
            @Override
            public void handle(ActionEvent event) {
                if(original==null){
                    Alert err = new Alert(AlertType.ERROR);
                    err.setTitle("ANOVA Express");
                    err.setHeaderText("Подаци и оригинални нису доступни.");
                    err.setContentText("После рачунања су доступни до прве модификације операцијом.");
                    err.showAndWait(); 
                }else{
                    for(int i=0; i<nx; i++){
                        HashMap<Integer, TextField> hm = dindex.get(i); 
                        for(int j=0; j<ny; j++){
                             TextField tf = hm.get(j);
                             if(original!=null && original[i][j]!=null)
                                 tf.setText(original[i][j].toString());
                             else 
                                 tf.setText("");
                       }
                    }
                    
                    Stage stage = new Stage();
                    ANOVAExpress ap = new ANOVAExpress();
                    stage.setOnCloseRequest((e)->{
                        if(!errorsSST.isDisabled() && !anova.isDisabled()){
                            plusCol.setDisable(false);
                            plusRow.setDisable(false);
                            minusCol.setDisable(false);
                            minusRow.setDisable(false);
                            calc.setDisable(false);
                            once.setDisable(false);
                            random.setDisable(false);
                        }
                        errorsSSE.setDisable(false);
                    });
                    ap.clearAvg();
                    ap.start(stage);
                    ap.disableAll();
                    ap.go.setDisable(false);
                    ap.goCol.setDisable(false);
                    ap.goRow.setDisable(false);
                    ap.point.setDisable(false);
                    ap.point.setText("E\\A");
                    plusCol.setDisable(true);
                    plusRow.setDisable(true);
                    minusCol.setDisable(true);
                    minusRow.setDisable(true);
                    calc.setDisable(true);
                    once.setDisable(true);
                    random.setDisable(true);
                    errorsSSE.setDisable(true);
                    for(int i=0; i<nx; i++)
                        ap.plusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.plusRow.getOnAction().handle(null);
                    sse.clear();
                    for(int i=0;i<nx; i++)
                        sse.add(0d);
                    for(int i=0; i<ap.nx; i++){
                        HashMap<Integer,TextField> mapa = ap.dindex.get(i);
                        for(int j=0; j<ap.ny; j++){
                            TextField tf = mapa.get(j);
                            tf.setEditable(false);
                            if(original!=null && original[i][j]!=null && colavgs.size()>0)
                                tf.setText(((Double)(original[i][j]-colavgs.get(i))).toString());
                            else{ 
                                tf.setText("");
                                sse.clear();
                            }
                            if(sse.size()!=0) sse.set(i,sse.get(i)+(original[i][j]-colavgs.get(i))*
                                    (original[i][j]-colavgs.get(i)));
                        }
                    }
                    Double ssse = 0.0; 
                    for(int i=0; i<ap.nx; i++){
                        TextField tf = new TextField();
                        tf.setStyle("-fx-background-color: skyblue;");
                        tf.setPrefHeight(30);
                        tf.setPrefWidth(100);
                        tf.setEditable(false);
                        if(sse.size()>0)tf.setText(sse.get(i).toString());
                        ap.net.add(tf,i+2, ny+2);
                        if(sse.size()>0)ssse+=sse.get(i);
                    }
                    TextField tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSEi");
                    ap.net.add(tf,1, ny+2);
                    
                     tf = new TextField();
                    tf.setStyle("-fx-background-color: royalblue;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(sse.size()>0)tf.setText(ssse.toString());
                    else tf.setText("");
                    ap.net.add(tf,2, ny+3);
                    
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSE");
                    ap.net.add(tf,1,ny+3);
                   
                }
            }
        });
        
        errorsSST.setOnAction(new EventHandler<ActionEvent>() {    
            @Override
            public void handle(ActionEvent event) {
                if(original==null){
                    Alert err = new Alert(AlertType.ERROR);
                    err.setTitle("ANOVA Express");
                    err.setHeaderText("Подаци и оригинали нису доступни.");
                    err.setContentText("После рачунања су доступни до прве модификације операцијом.");
                    err.showAndWait(); 
                }else{
                    for(int i=0; i<nx; i++){
                        HashMap<Integer, TextField> hm = dindex.get(i); 
                        for(int j=0; j<ny; j++){
                             TextField tf = hm.get(j);
                             if(original!=null && original[i][j]!=null)
                                 tf.setText(original[i][j].toString());
                             else 
                                 tf.setText("");
                       }
                    }
                    Stage stage = new Stage();
                    ANOVAExpress ap = new ANOVAExpress();
                    stage.setOnCloseRequest((e)->{
                        if(!errorsSSE.isDisabled() && !anova.isDisabled()){
                            plusCol.setDisable(false);
                            plusRow.setDisable(false);
                            minusCol.setDisable(false);
                            minusRow.setDisable(false);
                            calc.setDisable(false);
                            once.setDisable(false);
                            random.setDisable(false);
                        }
                        errorsSST.setDisable(false);
                        
                    });
                    ap.start(stage);
                    ap.disableAll();
                    ap.go.setDisable(false);
                    ap.goCol.setDisable(false);
                    ap.goRow.setDisable(false);
                    ap.point.setDisable(false);
                    ap.point.setText("T\\A");
                    plusCol.setDisable(true);
                    plusRow.setDisable(true);
                    minusCol.setDisable(true);
                    minusRow.setDisable(true);
                    calc.setDisable(true);
                    once.setDisable(true);
                    random.setDisable(true);
                    errorsSST.setDisable(true);
                    for(int i=0; i<nx; i++)
                        ap.plusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.plusRow.getOnAction().handle(null);
                    sst = 0d;
                    for(int i=0; i<ap.nx; i++){
                        HashMap<Integer,TextField> mapa = ap.dindex.get(i);
                        for(int j=0; j<ap.ny; j++){
                            TextField tf = mapa.get(j);
                            tf.setEditable(false);
                            if(original!=null && original[i][j]!=null && avg!=null){
                                 tf.setText(((Double)(original[i][j]-avg)).toString());
                                 sst+=(original[i][j]-avg)*(original[i][j]-avg);
                            }
                            else{ 
                                 tf.setText("");
                                 sst = null; 
                            }
                        }
                    }
                    
                    TextField tf = new TextField();
                    tf.setStyle("-fx-background-color: lime;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(sst!=null) tf.setText(sst.toString());
                    ap.net.add(tf,2, ny+2);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SST");
                    ap.net.add(tf,1, ny+2);
                }
            }
        });
        
        
        anova.setOnAction(new EventHandler<ActionEvent>() {    
            @Override
            public void handle(ActionEvent event) {
                if(original==null){
                    Alert err = new Alert(AlertType.ERROR);
                    err.setTitle("ANOVA Express");
                    err.setHeaderText("Подаци и оригинални нису доступни.");
                    err.setContentText("После рачунања су доступни до прве модификације операцијом.");
                    err.showAndWait(); 
                }else{
                    for(int i=0; i<nx; i++){
                        HashMap<Integer, TextField> hm = dindex.get(i); 
                        for(int j=0; j<ny; j++){
                             TextField tf = hm.get(j);
                             if(original!=null && original[i][j]!=null)
                                 tf.setText(original[i][j].toString());
                             else 
                                 tf.setText("");
                       }
                    }
                    
                    Stage stage = new Stage();
                    ANOVAExpress ap = new ANOVAExpress();
                    stage.setOnCloseRequest((e)->{
                        if(!errorsSST.isDisabled() && !errorsSSE.isDisabled()){
                            plusCol.setDisable(false);
                            plusRow.setDisable(false);
                            minusCol.setDisable(false);
                            minusRow.setDisable(false);
                            calc.setDisable(false);
                            once.setDisable(false);
                            random.setDisable(false);
                        }
                        anova.setDisable(false);
                    });
                    ap.clearAvg();
                    ap.start(stage);
                    ap.disableAll();
                    ap.go.setDisable(false);
                    ap.goCol.setDisable(false);
                    ap.goRow.setDisable(false);
                    ap.point.setDisable(false);
                    ap.point.setText("E\\A");
                    plusCol.setDisable(true);
                    plusRow.setDisable(true);
                    minusCol.setDisable(true);
                    minusRow.setDisable(true);
                    calc.setDisable(true);
                    once.setDisable(true);
                    random.setDisable(true);
                    anova.setDisable(true);
                    for(int i=0; i<nx; i++)
                        ap.plusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.plusRow.getOnAction().handle(null);
                    sse.clear();
                    for(int i=0;i<nx; i++)
                        sse.add(0d);
                    for(int i=0; i<ap.nx; i++){
                        HashMap<Integer,TextField> mapa = ap.dindex.get(i);
                        for(int j=0; j<ap.ny; j++){
                            TextField tf = mapa.get(j);
                            tf.setEditable(false);
                            if(original!=null && original[i][j]!=null && colavgs.size()>0)
                                tf.setText(((Double)(original[i][j]-colavgs.get(i))).toString());
                            else{ 
                                tf.setText("");
                                sse.clear();
                            }
                            if(sse.size()!=0) sse.set(i,sse.get(i)+(original[i][j]-colavgs.get(i))*
                                    (original[i][j]-colavgs.get(i)));
                        }
                    }
                    Double ssse = 0d; 
                    for(int i=0; i<ap.nx; i++){
                        TextField tf = new TextField();
                        tf.setStyle("-fx-background-color: skyblue;");
                        tf.setPrefHeight(30);
                        tf.setPrefWidth(100);
                        tf.setEditable(false);
                        if(sse.size()>0)tf.setText(sse.get(i).toString());
                        if(sse.size()>0) ssse+=sse.get(i); 
                        ap.net.add(tf, i+2, 1);
                    }
                    if(sse.size()==0) ssse = null;
                    TextField tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSEi");
                    ap.net.add(tf,1,1);
                    
                    for(int i=0; i<nx; i++)
                        ap.minusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.minusRow.getOnAction().handle(null);
                    
                    
                    ap.net.getChildren().remove(ap.point);
                    
                    
                    for(int i=0; i<nx; i++)
                        ap.plusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.plusRow.getOnAction().handle(null);
                    sst = 0d;
                    for(int i=0; i<ap.nx; i++){
                        HashMap<Integer,TextField> mapa = ap.dindex.get(i);
                        for(int j=0; j<ap.ny; j++){
                            TextField tf2 = mapa.get(j);
                            tf.setEditable(false);
                            if(original!=null && original[i][j]!=null && avg!=null){
                                 tf2.setText(((Double)(original[i][j]-avg)).toString());
                                 sst+=(original[i][j]-avg)*(original[i][j]-avg);
                            }
                            else{ 
                                 tf2.setText("");
                                 sst = null; 
                            }
                        }
                    }
                    
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: royalblue;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(ssse!=null) tf.setText(ssse.toString());
                    ap.net.add(tf,2,2);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSE");
                    ap.net.add(tf,1,2);
                    
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: lime;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(sst!=null) tf.setText(sst.toString());
                    ap.net.add(tf,2,3);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SST");
                    ap.net.add(tf,1,3);
                    
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: red;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(cssa!=null) tf.setText(cssa.toString());
                    ap.net.add(tf,2,4);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSA");
                    ap.net.add(tf,1,4);
                    
                    for(int i=0; i<nx; i++)
                        ap.minusCol.getOnAction().handle(null);
                    for(int i=0; i<ny; i++)
                        ap.minusRow.getOnAction().handle(null);
                    
                    ap.net.getChildren().remove(ap.point);
                    
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(cssa!=null) tf.setText(Integer.toString(nx-1));
                    ap.net.add(tf,2,5);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("df(SSA)");
                    ap.net.add(tf,1,5);
                    
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(ssse!=null) tf.setText(Integer.toString((nx-1)*ny));
                    ap.net.add(tf,2,6);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("df(SSE)");
                    ap.net.add(tf,1,6);
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    if(sst!=null) tf.setText(Integer.toString(nx*ny-1));
                    ap.net.add(tf,2,7);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("df(SST)");
                    ap.net.add(tf,1,7);
                    
                    Double sa2 = null; 
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setStyle("-fx-background-color: yellow;");
                    tf.setEditable(false);
                    if(rssa!=null && nx>1) tf.setText(Double.toString(sa2=cssa/(nx-1)));
                    ap.net.add(tf,2,8);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("Sa2");
                    ap.net.add(tf,1,8);
                    
                    Double se2 = null; 
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setStyle("-fx-background-color: yellow;");
                    tf.setEditable(false);
                    if(ssse!=null && nx>1) tf.setText(Double.toString(se2=ssse/((nx-1)*ny)));
                    ap.net.add(tf,2,9);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("Se2");
                    ap.net.add(tf,1,9);
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setStyle("-fx-background-color: orange;");
                    tf.setEditable(false);
                    if(se2!=null && sa2!=null) tf.setText(Double.toString(sa2/se2));
                    ap.net.add(tf,2,10);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("F");
                    ap.net.add(tf,1,10);
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setStyle("-fx-background-color: orange;");
                    tf.setEditable(false);
                    if(cssa!=null && sst!=null) tf.setText(Double.toString(cssa/sst));
                    ap.net.add(tf,2,11);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSA/SST");
                    ap.net.add(tf,1,11);
                    
                    tf = new TextField();
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setStyle("-fx-background-color: orange;");
                    tf.setEditable(false);
                    if(ssse!=null && sst!=null) tf.setText(Double.toString(ssse/sst));
                    ap.net.add(tf,2,12);
                    tf = new TextField();
                    tf.setStyle("-fx-background-color: transparent;");
                    tf.setPrefHeight(30);
                    tf.setPrefWidth(100);
                    tf.setEditable(false);
                    tf.setText("SSE/SST");
                    ap.net.add(tf,1,12);
               }
            }
        });
        
        calc.setPrefHeight(30);
        calc.setPrefWidth(100);
        minusCol.setPrefHeight(30);
        minusCol.setPrefWidth(100);
        plusCol.setPrefHeight(30);
        plusCol.setPrefWidth(100);
        minusRow.setPrefHeight(30);
        minusRow.setPrefWidth(100);
        plusRow.setPrefHeight(30);
        plusRow.setPrefWidth(100);
        random.setPrefHeight(30);
        random.setPrefWidth(100);
        once.setPrefHeight(30);
        once.setPrefWidth(100);
        go.setPrefHeight(30);
        go.setPrefWidth(100);
        goCol.setPrefHeight(30);
        goCol.setPrefWidth(100);
        goRow.setPrefHeight(30);
        goRow.setPrefWidth(100);
        
        refresh.setPrefHeight(30);
        refresh.setPrefWidth(100);
        errorsSSE.setPrefHeight(30);
        errorsSSE.setPrefWidth(100);
        errorsSST.setPrefHeight(30);
        errorsSST.setPrefWidth(100);
        
        anova.setPrefHeight(30);
        anova.setPrefWidth(100);
        
        point.setPrefHeight(30);
        point.setPrefWidth(100);
       
        root.setContent(net);
        
        net.add(calc,0,0);
        net.add(minusRow,0,1);
        net.add(minusCol,1,0);
        net.add(plusRow,0,2);
        net.add(plusCol,2,0);
        
        net.add(go,3,0);
        net.add(goCol,4,0);
        net.add(goRow,5,0);
        
        
        net.add(random,0,3);
        net.add(once,0,4);
        net.add(refresh,0,5);
        net.add(errorsSSE,0,6);
        net.add(errorsSST,0,7);
        
        net.add(anova,0,9);
        
        net.add(point,1,1);
        Scene scene = new Scene(root, 1000, 500);
        
        primaryStage.setTitle("Anova Express [0,0]");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void disableAll(){
        for(Node n: net.getChildren())
            n.setDisable(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
