package sample.Model;

import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.api.gax.rpc.BidiStreamingCallable;
import com.google.cloud.speech.v1p1beta1.*;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.ByteString;
import sample.Controllers.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// [START speech_quickstart]
// Imports the Google Cloud client library
// [START speech_quickstart]
// Imports the Google Cloud client library
public class Speech {

    public  void SpeechTransciber() throws Exception {
        Controller controller = new Controller();

        // Instantiates a client
        try (SpeechClient speechClient = SpeechClient.create()) {

            // The path to the audio file to transcribe
            String fileName = "C:/Users/SAMIM/Desktop/Sac/Our Project - Copy/src/main/resources/assets/audio.wav";

            // Reads the audio file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                String print = alternative.getTranscript();

                System.out.printf("Transcription: %s%n", print);


            }
        }
    }

    /**
     * real time recognition from google website.
     *
     */
        public static void streamingRecognizeFile(String fileName) throws Exception, IOException {
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);

            // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
            try (SpeechClient speech = SpeechClient.create()) {

                // Configure request with local raw PCM audio
                RecognitionConfig recConfig = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                        .setLanguageCode("en-US")
                        .setSampleRateHertz(16000)
                        .setModel("default")
                        .build();
                StreamingRecognitionConfig config = StreamingRecognitionConfig.newBuilder()
                        .setConfig(recConfig)
                        .build();

                class ResponseApiStreamingObserver<T> implements ApiStreamObserver<T> {
                    private final SettableFuture<List<T>> future = SettableFuture.create();
                    private final List<T> messages = new java.util.ArrayList<T>();

                    @Override
                    public void onNext(T message) {
                        messages.add(message);
                    }

                    @Override
                    public void onError(Throwable t) {
                        future.setException(t);
                    }

                    @Override
                    public void onCompleted() {
                        future.set(messages);
                    }

                    // Returns the SettableFuture object to get received messages / exceptions.
                    public SettableFuture<List<T>> future() {
                        return future;
                    }
                }

                ResponseApiStreamingObserver<StreamingRecognizeResponse> responseObserver =
                        new ResponseApiStreamingObserver<>();

                BidiStreamingCallable<StreamingRecognizeRequest, StreamingRecognizeResponse> callable =
                        speech.streamingRecognizeCallable();

                ApiStreamObserver<StreamingRecognizeRequest> requestObserver =
                        callable.bidiStreamingCall(responseObserver);

                // The first request must **only** contain the audio configuration:
                requestObserver.onNext(StreamingRecognizeRequest.newBuilder()
                        .setStreamingConfig(config)
                        .build());

                // Subsequent requests must **only** contain the audio data.
                requestObserver.onNext(StreamingRecognizeRequest.newBuilder()
                        .setAudioContent(ByteString.copyFrom(data))
                        .build());

                // Mark transmission as completed after sending the data.
                requestObserver.onCompleted();

                List<StreamingRecognizeResponse> responses = responseObserver.future().get();

                for (StreamingRecognizeResponse response : responses) {
                    // For streaming recognize, the results list has one is_final result (if available) followed
                    // by a number of in-progress results (if iterim_results is true) for subsequent utterances.
                    // Just print the first result here.
                    StreamingRecognitionResult result = response.getResultsList().get(0);
                    // There can be several alternative transcripts for a given chunk of speech. Just use the
                    // first (most likely) one here.
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    System.out.printf("Transcript : %s\n", alternative.getTranscript());
                }
            }
        }

}
