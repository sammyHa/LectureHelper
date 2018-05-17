
/*
 * Copyright (c) 2018. by Samim Hakimi
 */

package sample.Model;
import javafx.scene.control.Alert;
import sample.Controllers.Controller;

import javax.sound.sampled.*;
import java.io.File;

/**
 * This class is recording the voice and stores it to local directory
 */
public class Recording {

    String path = "C:/Users/SAMIM/Desktop/audio.wav"; ///Sac/OurProjec3/src/main/resources/assets/

    public Recording(){
        //empty constructor
    }


    // reading the audio data using TargetDataLine Interface
     TargetDataLine targetDataLine;

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void setTargetDataLine(TargetDataLine targetDataLine) {
        this.targetDataLine = targetDataLine;
    }

//  get the audio format method
    public AudioFormat getAudioFormat() {
        float sampleRate = 16000.0f;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }


    public void initTargetDataLine()throws LineUnavailableException{

        //gets data from mic
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, getAudioFormat());

        if (!AudioSystem.isLineSupported(info)){
            System.out.println("No Mic!");
        }
        //gets the Audio format from system
        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

        // opens the line or mic
        targetDataLine.open(getAudioFormat());

        Alert recordingMessage = new Alert(Alert.AlertType.INFORMATION);
        recordingMessage.setTitle("Info Box");
        recordingMessage.setContentText("Recording now...");
        recordingMessage.setHeaderText(null);
        recordingMessage.show();

    }

    public void CaptureAudio() throws LineUnavailableException {


//      Audio format supported and buffer size
        getAudioFormat();

        initTargetDataLine();

        // Starts capturing audio from the mic
        new StartThread().start();

    }

    //Stops recording
    public void StopRecording() throws Exception {
        targetDataLine.stop();
        targetDataLine.close();
        Alert recordingMessage = new Alert(Alert.AlertType.INFORMATION);
        recordingMessage.setTitle("Info Box");
        recordingMessage.setContentText("Recording Stopped");
        recordingMessage.setHeaderText(null);
        recordingMessage.show();
        try {
            Controller controller = new Controller();
       // controller.Transcribe(path);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // class for the Thread
    class StartThread extends Thread {

        public void run(){

            AudioFileFormat.Type fileType;
            File audioFile;

            //Set the file type and the file extension
            fileType = AudioFileFormat.Type.WAVE;
            audioFile = new File(path);

            try{
                targetDataLine.open(getAudioFormat());
                targetDataLine.start();
                AudioSystem.write(
                        new AudioInputStream(targetDataLine),
                        fileType,
                        audioFile);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}