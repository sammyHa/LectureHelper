
package sample.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.controlsfx.dialog.FontSelectorDialog;
import sample.Model.Recording;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    @FXML
    private Label timerlabel;
    @FXML
    private Button icrecord;
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

    public Recording getRecordingVoice() {
        return recordingVoice;
    }

    public void setRecordingVoice(Recording recordingVoice) {
        this.recordingVoice = recordingVoice;
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

    /**
     * getter and setters
     *
     */































    public void clickme() throws LineUnavailableException, InterruptedException {
        recordingVoice.RecordingMethod();
    }


    public void setTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                interval++;
                //String timers = String.valueOf(interval);
                //timerlabel.setText(("Timer: " + timers));
                System.out.println("Timer: " + interval);
            }
        };

        timer.scheduleAtFixedRate(task,1000,1000);

    }

        //color picker

        public void ChangeColor(ActionEvent event) {
           // Color color = colorpicker.getValue();


            //this will convert the values of the colorpicker to hex so we can use it from the css
            String color = "#" + Integer.toHexString(colorpicker.getValue().hashCode()).substring(0, 6).toUpperCase();
            textArea.setStyle("-fx-text-fill: " + color +";");
            textArea.setText("Good its changed! ");
        }

        //Select font style method
    public void ChangeFontStyle(){
        // iff add the event handler will need to click the button two times to excute
        // first will be for the action to be excuted and second time the method.
        // dont put the button.onActionClick(e->{});
            FontSelectorDialog fontSelectorDialog = new FontSelectorDialog(null);
            fontSelectorDialog.setTitle("Select Font");
            fontSelectorDialog.showAndWait();
            textArea.setFont(fontSelectorDialog.getResult());

    }

    //save to file method

    public void SaveToFile(){

        //this will save the file for the same day it was recorded and automatically date the file.
        String mDateAndTime = new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(Calendar.getInstance().getTime());
        String path = "C:/Users/SAMIM/Desktop/";
            File filePath = new File(path + "Recorded on " + mDateAndTime +".doc");
            try {
                FileWriter fileWriter = new FileWriter(filePath,true);
                PrintWriter printWriter = new PrintWriter(fileWriter);
               String text = textArea.getText();
                SaveFile(text,filePath);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Saved");

    }

    private void SaveFile(String content, File file)throws IOException{

        FileWriter fileWriter = null;
        fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();

    }
}
