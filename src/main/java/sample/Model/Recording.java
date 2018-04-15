package sample.Model;

import com.google.cloud.speech.v1p1beta1.SpeechClient;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import sample.Controllers.Controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * This class is recording the voice and stores it to local drive
 */
public class Recording {
    String path = "C:/Users/SAMIM/Desktop/Sac/OurProjec3/src/main/resources/assets/audio.wav";
    public Recording(){
        //empty constructor
    }

    @FXML private ImageView icrecord;
    //record time 60 minutes 3666666
    long seconds = 5000;

    //instantiateing the class Speech
    Speech speech = new Speech();

     TargetDataLine targetDataLine;

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void RecordingMethod() throws InterruptedException, LineUnavailableException {


        AudioFormat audioFormat = new AudioFormat(16000, 16, 1 , true, true );

        //this Dataline interface
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info)){
            System.out.println("Line is not supported!");
        }
         targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

        targetDataLine.open();

        System.out.println("Recording now..");

        targetDataLine.start();



        Thread thread = new Thread(() -> {

            //this is recording audio
            AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

            //This is where the recorded file will be saved

            File wavFile = new File(path);
            try {

                //Audio format. there is a lot of format but a few are supported by all systems one of is WAVE.
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,wavFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Controller controller = new Controller();
        //controller.setTimer();



        //how long do you want ot record the audio

            Thread.sleep(seconds);

            targetDataLine.stop();
            targetDataLine.close();
        System.out.println("Recording stopped");
        try {
            //speech.SpeechTransciber();
            speech.streamingRecognizeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*
    ----------------------------java------------------------------------
     */


    private static  Task<Object> StreamingMicRecognizeAsync(int seconds)
    {
        if(AudioSystem.isLineSupported(null)){
            System.out.println("No mic");
            return null;
        }
        try {
            SpeechClient speech = SpeechClient.create();



        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /*
    ----------------------------java------------------------------------
     */

}
