package com.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Controller1 implements initialize {

    @FXML
    private ImageView img;
    @FXML
    private Button button0, button1;
    @FXML 
    private Button botonStop;
    // ImageViews
    @FXML
    private ImageView img0, img1, img2, img3, img4, img5, img6, img7;
    @FXML
    private ImageView img8, img9, img10, img11, img12, img13, img14, img15;
    @FXML
    private ImageView img16, img17, img18, img19, img20, img21, img22, img23;

    // array con los ImageView
    private List<ImageView> imageViews = new ArrayList<>();
    // ArrayList con los future
    private List<LoadImage> LoadImageObjecteList = new ArrayList<>();

    @FXML
    private AnchorPane container;
    @FXML
    private Label loading;
    @FXML
    private Rectangle lowerRectangle, upperRectangle;

    private double totalBarSize;
    private int totalImagesLoaded = 0;

    @FXML
    private Label statusLabel;
    @FXML
    public void initialize() {
        loading.setVisible(false);
        Collections.addAll(imageViews, img0, img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, 
        img11, img12, img13, img14, img15, img16, img17, img18, img19, img20, img21, img22, img23);
        totalBarSize = lowerRectangle.getWidth();
    }

    @FXML
    private void animateToView0(ActionEvent event) {
        UtilsViews.setViewAnimating("View0");
    }

    @FXML
    private void loadImage() {
        // Limpiar lista anterior y resetear numero para calcular barra
        if (!LoadImageObjecteList.isEmpty()) {
            for (LoadImage li : LoadImageObjecteList) {
                li.getImageSlot().setImage(null);
            }
            LoadImageObjecteList.clear();
        }
        statusLabel.setText("Progress - 0/24:");
        totalImagesLoaded = 0;
        changeBarSize(upperRectangle, totalBarSize/24, totalImagesLoaded);
        // Ocultamos el boton de començar si el proceso ya esta en marcha
        button1.setVisible(false);

        for (int i = 0; i != imageViews.size(); i++) {
            int delay = (int) (5 + Math.random() * 46);

            String imagePath = "/assets/imagenes/"+i+".png";

            LoadImageObjecteList.add(new LoadImage(imageViews.get(i), imagePath, delay));
        }
    }

    @FXML
    private void stopThreads() {
        for (LoadImage loadobj : LoadImageObjecteList) {
            loadobj.getImagen().cancel(true);
            loadobj.getImageSlot().setImage(null);
        }
        //LoadImageObjecteList.clear();
        totalImagesLoaded = 0;
        changeBarSize(upperRectangle, totalBarSize/24, totalImagesLoaded);
        statusLabel.setText("Progress - 0/24:");
        button1.setVisible(true);
        System.out.println("Stopped threads");
    }

    public void loadImageBackground(Consumer<Image> callBack) {
        // Use a thread to avoid blocking the UI
        CompletableFuture<Image> futureImage = CompletableFuture.supplyAsync(() -> {
            try {
                // Wait a second to simulate a long loading time
                Thread.sleep(1000);

                // Load the data from the assets file
                Image image = new Image(getClass().getResource("/assets/image.png").toString());
                return image;

            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        })
        .exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        futureImage.thenAcceptAsync(result -> {
            callBack.accept(result);
        }, Platform::runLater);
    }

    private class LoadImage {
        private ImageView imageSlot;
        private String imageLocation;
        private int delayTime;
        private CompletableFuture<Image> imagen;

        public LoadImage(ImageView imageSlot, String imageLocation, int delayTime) {
            this.imageSlot = imageSlot;
            this.imageLocation = imageLocation;
            this.delayTime = delayTime;

            this.imagen = CompletableFuture.supplyAsync(() -> {
            try {
                // Load the data from the assets file
                Image image = new Image(getClass().getResource(this.imageLocation).toString());
                // Wait a second to simulate a long loading time
                Thread.sleep(1000 * this.delayTime);
                return image;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            });
            this.imagen.thenAccept(this::setDownloadedImage);
        }

        void setDownloadedImage(Image image) {
            Platform.runLater(() -> {
                this.imageSlot.setImage(image);
                totalImagesLoaded += 1;
                changeBarSize(upperRectangle, totalBarSize/24, totalImagesLoaded);
                statusLabel.setText("Progress - "+totalImagesLoaded+"/24:");

                // Volvemos a hacer el boton de començar visible
                if (totalImagesLoaded == 24) {
                    button1.setVisible(true);
                }
            });
        }

        public CompletableFuture<Image> getImagen() {
            return imagen;
        }
        public ImageView getImageSlot() {
            return imageSlot;
        }
    }

    private void changeBarSize(Rectangle rectangle, double percentageEquivalent, int percentage) {
        Double newWidght = percentageEquivalent * percentage;
        double newX = rectangle.getX() - (newWidght - totalBarSize);

        rectangle.setWidth(newWidght);
        rectangle.setX(newX);
        
    }
}

