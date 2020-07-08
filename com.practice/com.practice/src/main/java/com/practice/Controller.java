package com.practice;


import com.jfoenix.controls.JFXSlider;
import com.practice.ml.vg16.PetType;
import com.practice.ml.vg16.VG16ForCat;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    private File selectedFile;
    private double accuracy;
    private VG16ForCat vg16ForCat;
    private boolean FLAG_FILE = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private JFXSlider slider;

    @FXML
    private URL location;

    @FXML
    private Button downButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Button youKnowButton;

    @FXML
    private Label result1;


    @FXML
    void initialize() {
        downButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
            fileChooser.setTitle("Open Document");//Заголовок диалога
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                String localUrl = null;
                try {
                    localUrl = selectedFile.toURI().toURL().toString();
                    Image image = new Image(localUrl);
                    imageView.setImage(image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            FLAG_FILE = true;
        });
        youKnowButton.setOnAction(event -> {
            accuracy = slider.getValue() / 100;
            try {
                if (FLAG_FILE) {
                    vg16ForCat = new VG16ForCat();
                    vg16ForCat.loadModel();
                    PetType petType = vg16ForCat.detectCat(selectedFile, accuracy);
                    if (petType == PetType.CAT) {
                        result1.setText("это кот");
                    } else if (petType == PetType.DOG) {
                        result1.setText("это собака");
                    } else {
                        result1.setText("не знаю");
                    }
                } else {
                    result1.setText("выберите картинку!");
                }
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        });
    }
}
