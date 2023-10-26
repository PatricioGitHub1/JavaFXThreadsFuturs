package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class Controller0 {

    @FXML
    private Button button0;
    @FXML
    private AnchorPane container;
    @FXML
    private Label percentatge0, percentatge1, percentatge2;
    @FXML
    private Rectangle lowerbar0, lowerbar1, lowerbar2;
    @FXML
    private Button ButtonStartStop0, ButtonStartStop1, ButtonStartStop2;
    private ExecutorService executor = Executors.newFixedThreadPool(3); // Creem una pool de dos fils

    @FXML
    private Rectangle staticbar1;

    @FXML
    private void animateToView1(ActionEvent event) {
        UtilsViews.setViewAnimating("View1");
    }

    private boolean isTaskOn00 = false;
    private boolean isTaskOn01 = false;
    private boolean isTaskOn02 = false;

    private double barSize;

    @FXML
    private void handleClick(ActionEvent evt) {
        Button srcButton = (Button)evt.getSource();
        if (srcButton.equals(ButtonStartStop0)) {
            isTaskOn00 = booleanModifier(isTaskOn00, 0);
            changeButtonText(srcButton, isTaskOn00);
        } else if (srcButton.equals(ButtonStartStop1)) {
            isTaskOn01 = booleanModifier(isTaskOn01, 1);
            changeButtonText(srcButton, isTaskOn01);
        } else if (srcButton.equals(ButtonStartStop2)) {
            isTaskOn02 = booleanModifier(isTaskOn02, 2);
            changeButtonText(srcButton, isTaskOn02);
        }
    }

    // Para controlar el estado de los processos
    private boolean booleanModifier(boolean currentStatus, int index) {
        if (!currentStatus) {
            backgroundTask(index);
            return true;
        }
        return false;
    }

    private void backgroundTask(int index) {
        barSize = staticbar1.getWidth();
        // Executar la tasca
        executor.submit(() -> {
            try {
                int[] totalValue = {0};
                for (int i = 0; i <= 100; i++) {
                    if (index == 0) {
                        // Actualitzar el Label en el fil d'aplicació de l'UI
                        if (!isTaskOn00) {
                            break;
                        }
                        totalValue[0] = i;
                        Platform.runLater(() -> {
                            percentatge0.setText(String.valueOf(totalValue[0]) + "%");
                            changeBarSize(lowerbar0, barSize/100.0, totalValue[0]);
                        });
                        //Si acaba hacer que el proceso sea False
                        if (totalValue[0] >= 100) {
                            isTaskOn00 = false;
                            changeButtonText(ButtonStartStop0,false);
                            break;
                        }
                        Thread.sleep(1000);
                    }

                    if (index == 1) {
                        // Actualitzar el Label en el fil d'aplicació de l'UI
                        if (!isTaskOn01) {
                            break;
                        }
                        //Crear nuevo valor=====================================
                        //int tiempoEspera = (int) (3 + Math.random() * 3);
                        int valorAleatorioExtra = (int) (2 + Math.random() * 3);
                        totalValue[0] = totalValue[0] + 1 + valorAleatorioExtra;
                        //======================================================
                        Platform.runLater(() -> {
                            if (totalValue[0] > 100) {
                                totalValue[0] = 100;
                            }
                            percentatge1.setText(String.valueOf(totalValue[0]) + "%");
                            changeBarSize(lowerbar1, barSize/100.0, totalValue[0]);
                        });
                        //Si acaba hacer que el proceso sea False
                        if (totalValue[0] >= 100) {
                            isTaskOn01 = false;
                            changeButtonText(ButtonStartStop1,false);
                            break;
                        }
                        //Thread.sleep(1000 * tiempoEspera);
                        Thread.sleep(1000);
                    }

                    if (index == 2) {
                        // Actualitzar el Label en el fil d'aplicació de l'UI
                        if (!isTaskOn02) {
                            break;
                        }
                        //Crear nuevo valor=====================================
                        //int tiempoEspera = (int) (4 + Math.random() * 3);
                        int valorAleatorioExtra = (int) (3 + Math.random() * 6);
                        totalValue[0] = totalValue[0] + 1 + valorAleatorioExtra;
                        //======================================================
                        Platform.runLater(() -> {
                            if (totalValue[0] > 100) {
                                totalValue[0] = 100;
                            }
                            percentatge2.setText(String.valueOf(totalValue[0]) + "%");
                            changeBarSize(lowerbar2, barSize/100.0, totalValue[0]);
                        });
                        //Si acaba hacer que el proceso sea False
                        if (totalValue[0] >= 100) {
                            isTaskOn02 = false;
                            changeButtonText(ButtonStartStop2,false);
                            break;
                        }
                        //Thread.sleep(1000 * tiempoEspera);
                        Thread.sleep(1000);
                    }

                    System.out.println("Updating label: " + index + ", Value: " + totalValue[0]);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    
    private void changeBarSize(Rectangle rectangle, double onePercentEquivalent, int percentage) {
        Double newWidght = onePercentEquivalent * percentage;
        double newX = rectangle.getX() - (newWidght - barSize);

        rectangle.setWidth(newWidght);
        rectangle.setX(newX);
        
    }

    private void changeButtonText(Button button, boolean taskStatus) {
        String text;
        if (taskStatus) {
            text = "Aturar";
        } else {
            text = "Iniciar";
        }
        Platform.runLater(() -> {
            button.setText(text);
        });
    }

    // Aquesta funció la cridaries quan vulguis tancar l'executor (per exemple, quan tanquis la teva aplicació)
    public void stopExecutor() {
        executor.shutdown();
    }

}
