package com.practice;

import com.practice.ml.ui.ProgressBar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;


/**
 * JavaFX App
 */
public class App extends Application {
    private static final String MODEL_URL = "https://dl.dropboxusercontent.com/s/djmh91tk1bca4hz/RunEpoch_class_2_soft_10_32_1800.zip?dl=0";    private static Scene scene;
    private static File model;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("sample"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    private static void downloadModelForFirstTime() throws IOException {
        JFrame mainFrame = new JFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ProgressBar progressBar = new ProgressBar(mainFrame, false);
        model = new File("resources/model.zip");
        if (!model.exists() || FileUtils.checksum(model, new Adler32()).getValue() != 3082129141l) {
            model.delete();
            progressBar.showProgressBar("Downloading model for the first time 500MB!");
            URL modelURL = new URL(MODEL_URL);

            try {
                FileUtils.copyURLToFile(modelURL, model);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to download model");
                throw new RuntimeException(e);
            } finally {
                progressBar.setVisible(false);
                mainFrame.dispose();
            }

        }
    }
    public static void main(String[] args) {
        try {
            downloadModelForFirstTime();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(model.exists()){
            launch(args);
        }
    }

}