/**
 * 
 */
package ca.bcit.cst.sheng;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


/**
 * @author Sheng Qin
 * @author Boann 
 *
 */
public class Calculator extends Application {
    /** array for buttons. */
    private Button[] buttons;

    /** String for all dia number and string. */
    private String dia;

    /** int number 12. */
    private final int num12 = 20;

    /** text for text to show. */
    private Text text1;

    /** string temp store string to show in the panel. */
    private String temp;
    private String history = "";
        
    private Button buttonsin;
    private Button buttoncos;
    private Button buttontan;
    private Button buttonsqrt;
    private Button buttondelete;
    private TextArea text = new TextArea();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    private LocalDateTime now;  
    
    /**
     * Creates and presents a GUI with 1 buttons that control the dial.
     * 
     * @param primaryStage a Stage
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final int three = 3;
        final int num300 = 850;
        final int num90 = 90;
        final int num200 = 600;
        GridPane gridPane = new GridPane();
        
        buttons = new Button[num12];
        temp = "";
        dia = "123/(456*)789-^0.=+C";

        for (int i = 0; i < num12; i++) {
            buttons[i] = new Button(String.valueOf(dia.charAt(i)));
        }

        text1 = new Text(temp);
        text.setPrefHeight(400);
        text.setPrefWidth(400);
        text.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        text.setWrapText(true);
//        text.setMaxWidth(num90);

        gridPane.add(text, 0, 0, 5, 1);
        //gridPane.add(myTf, 1, 0, 2 + 1, 1);

        for (int r = 0; r <= three; r++) {
            for (int c = 0; c <= 4; c++) {
                gridPane.add(buttons[r * 5 + c], c, r + 1);
                buttons[r * 5 + c].setMinSize(80, 50);
                buttons[r * 5 + c].setOnAction(this::processButton);
            }
        }
        buttons[17].setStyle("-fx-background-color: #00ff00");
        buttondelete = new Button("<Del");
        gridPane.add(buttondelete, 4, 5);
        buttondelete.setMinSize(80, 50);
        buttondelete.setOnAction(this::processButton);
        
        buttonsin = new Button("SIN");
        gridPane.add(buttonsin, 1, 5);
        buttonsin.setMinSize(80, 50);
        buttonsin.setOnAction(this::processButton);
        
        buttoncos = new Button("COS");
        gridPane.add(buttoncos, 2, 5);
        buttoncos.setMinSize(80, 50);
        buttoncos.setOnAction(this::processButton);
        
        buttontan = new Button("TAN");
        gridPane.add(buttontan, 3, 5);
        buttontan.setMinSize(80, 50);
        buttontan.setOnAction(this::processButton);
        
        buttonsqrt = new Button("Sqrt");
        gridPane.add(buttonsqrt, 0, 5);
        buttonsqrt.setMinSize(80, 50);
        buttonsqrt.setOnAction(this::processButton);
//        Group root = new Group(gridPane);  
       
        gridPane.setHgap(num12);
        gridPane.setVgap(num12);
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane, num200, num300);
        primaryStage.setTitle("Calculator_Sheng");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Determines which button was pressed and sets the pane text accordingly..
     * 
     * @param event
     *            invoked this method
     */
    public void processButton(ActionEvent event) {
        now = LocalDateTime.now();
        for (int i = 0; i < num12; i++) {
            if (event.getSource() == buttons[i] && event.getSource() != buttons[17]) {
                temp += String.valueOf(dia.charAt(i));
                text.setText("\n" +temp + "\n" + history);
            } 
        } 
        
        if (event.getSource() == buttonsin) {
            temp += "sin(";
            text.setText("\n" +temp + "\n" + history);
        } else if (event.getSource() == buttoncos) {
            temp += "cos(";
            text.setText("\n" +temp + "\n" + history);
        } else if (event.getSource() == buttontan) {
            temp += "tan(";
            text.setText("\n" +temp + "\n" + history);
        } else if (event.getSource() == buttonsqrt) {
            temp += "sqrt(";
            text.setText("\n" +temp + "\n" + history);
        } else if (event.getSource() == buttondelete) {
            temp = temp.substring(0,temp.length()-1);
            text.setText("\n" +temp + "\n" + history);
        }
        
        
        if (event.getSource() == buttons[17]) {
            history = "\n\n" + dtf.format(now) +":\nInput:\t"+ temp + "\nAnswer:\t" + eval(temp) + history;
            text.setText(history);
            temp = "";
        } else if(event.getSource() == buttons[19]) {
            temp = "";
            history = "";
            text.setText("");
        }
    }
    /**
     * Drives the program.
     * 
     * @param args used
     */
    public static void main(String[] args) {
        launch(args);
    }



public static double eval(final String str) {
    return new Object() {
        int pos = -1, ch;

        void nextChar() {
            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
        }

        boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        double parse() {
            nextChar();
            double x = parseExpression();
            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
            return x;
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        //        | number | functionName factor | factor `^` factor

        double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if      (eat('+')) x += parseTerm(); // addition
                else if (eat('-')) x -= parseTerm(); // subtraction
                else return x;
            }
        }

        double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if      (eat('*')) x *= parseFactor(); // multiplication
                else if (eat('/')) x /= parseFactor(); // division
                else return x;
            }
        }

        double parseFactor() {
            if (eat('+')) return parseFactor(); 
            if (eat('-')) return -parseFactor(); 

            double x;
            int startPos = this.pos;
            if (eat('(')) { // parentheses
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(str.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z') { // functions
                while (ch >= 'a' && ch <= 'z') nextChar();
                String func = str.substring(startPos, this.pos);
                x = parseFactor();
                if (func.equals("sqrt")) x = Math.sqrt(x);
                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                else throw new RuntimeException("Unknown function: " + func);
            } else {
                throw new RuntimeException("Unexpected: " + (char)ch);
            }

            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

            return x;
        }
    }.parse();
}
}
