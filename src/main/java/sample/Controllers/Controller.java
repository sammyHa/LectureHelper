
/*
 * Copyright (c) 2018. by Samim Hakimi
 */

package sample.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.controlsfx.dialog.FontSelectorDialog;
import sample.Model.RealTimeSpeech;
import sample.Model.Recording;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    //vars
   private Timer timer;
   private TimerTask task;
   private int interval = 0;
   private Recording recordingVoice = new Recording();

   private RealTimeSpeech realTimeSpeech = new RealTimeSpeech();

   public Controller(){
       realTimeSpeech.mDuplex();
   }

    @FXML
    private Label timerlabel;
    @FXML
    private Button icrecord;
    @FXML
    private Button btnStop;
    @FXML
    private TextArea textArea;
    @FXML
    private Button savetofile;
    @FXML
    private Button fontstyle;
    @FXML
    private ColorPicker colorpicker;
    @FXML
    private Pane pane;
    @FXML
    private Text text;


    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public TimerTask getTask() {
        return task;
    }

    public void setTask(TimerTask task) {
        this.task = task;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }


    public Label getTimerlabel() {
        return timerlabel;
    }

    public void setTimerlabel(Label timerlabel) {
        this.timerlabel = timerlabel;
    }

    public Button getIcrecord() {
        return icrecord;
    }

    public void setIcrecord(Button icrecord) {
        this.icrecord = icrecord;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public Button getSavetofile() {
        return savetofile;
    }

    public void setSavetofile(Button savetofile) {
        this.savetofile = savetofile;
    }

    public Button getFontstyle() {
        return fontstyle;
    }

    public void setFontstyle(Button fontstyle) {
        this.fontstyle = fontstyle;
    }

    public ColorPicker getColorpicker() {
        return colorpicker;
    }

    public void setColorpicker(ColorPicker colorpicker) {
        this.colorpicker = colorpicker;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void StartRecording() throws LineUnavailableException, InterruptedException {
        realTimeSpeech.StartRecord();

//        recordingVoice.CaptureAudio();
//        btnStop.setDisable(false);
//        icrecord.setDisable(true);

    }

    public void mStopRecording() throws Exception {
            realTimeSpeech.stop();
//        recordingVoice.StopRecording();
//        btnStop.setDisable(true);
//        icrecord.setDisable(false);

    }


        //color picker
        public void ChangeColor() {
            //this will convert the values of the colorpicker to hex so we can use it from the css
            String color = "#" + Integer.toHexString(colorpicker.getValue().hashCode()).substring(0, 6).toUpperCase();
            textArea.setStyle("-fx-text-fill: " + color +";");
            textArea.setText("Color just changed ");
        }

        //Select font style method
    public void ChangeFontStyle(){
        /**
         * if the event handler is passed as a parameter than will need to click the button two times to execute
         * first will be for the action to be excuted and second time the method.
         * don't use the button.onActionClick(e->{}); instead call the method inside the button in fxml
         */
            FontSelectorDialog fontSelectorDialog = new FontSelectorDialog(null);
            fontSelectorDialog.setTitle("Select Font");
            fontSelectorDialog.showAndWait();
            textArea.setFont(fontSelectorDialog.getResult());

    }

    //save to file method
    public void SaveToFile(){


        if ( textArea.getText().trim().isEmpty()){
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setTitle("Error");
            message.setHeaderText(null);
            message.setContentText("There is no data to be saved!");
            message.show();

        }else {
            // Saves the document with current system date and time.
            String mDateAndTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(Calendar.getInstance().getTime());
            String path = "C:/Users/SAMIM/Desktop/";
            File filePath = new File(path + "Recorded on " + mDateAndTime +".doc");
            try {
                FileWriter fileWriter = new FileWriter(filePath,true);
                //PrintWriter printWriter = new PrintWriter(fileWriter);
                String text = textArea.getText();
                SaveFile(text,filePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Alert message = new Alert(Alert.AlertType.INFORMATION);
            message.setTitle("Information Box");
            message.setHeaderText(null);
            message.setContentText("Your document has been saved to \n" + filePath);
            message.show();
        }

    }

    private void SaveFile(String content, File file){

        try{
            FileWriter fileWriter;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }catch (IOException io){
            System.out.println("Error! " + io);
        }

    }


}
