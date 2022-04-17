package dk.easv;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ImageViewerWindowController
{
    private final List<MyImage> images = new ArrayList<>();
    public Label lblInProgress;

    private int currentImageIndex = 0;

    private boolean isSlideShowRunning = false;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    @FXML
    Parent root;
    @FXML
    private ImageView imageView;
    @FXML
    private Button slideshowButton;
    @FXML
    private Slider timerSlider;
    @FXML
    private Label lblImageName;
    @FXML
    private Label lblMixed;
    @FXML
    private Label lblBlue;
    @FXML
    private Label lblGreen;
    @FXML
    private Label lblRed;

    @FXML
    private void handleBtnLoadAction()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new MyImage(f.getName(),new Image(f.toURI().toString())));
            });
            displayImage();
            countPixelColors();
        }
    }

    @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex).getImage());
            MyImage currentImage = images.get(currentImageIndex);
            Runnable runnable = () -> {
                lblImageName.setText(currentImage.getName());
                lblRed.setText(String.valueOf(currentImage.getRedPixels()));
                lblBlue.setText(String.valueOf(currentImage.getBluePixels()));
                lblGreen.setText(String.valueOf(currentImage.getGreenPixels()));
                lblMixed.setText(String.valueOf(currentImage.getMixedPixels()));
            };
            Platform.runLater(runnable);
        }
    }

    @FXML
    private void handleBtnSlideshow() {
        if(!isSlideShowRunning){
            isSlideShowRunning = true;
            slideshowButton.setText("Stop Slideshow");

            Runnable task = () -> {
                while(isSlideShowRunning) {
                    handleBtnNextAction();

                    try {
                        TimeUnit.SECONDS.sleep((long) timerSlider.getValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            es.submit(task);
        }
        else{
            isSlideShowRunning = false;
            slideshowButton.setText("Start Slideshow");
            es.shutdown();
        }
    }

    private void countPixelColors(){
        //lblInProgress.setText("In progress...");
        ExecutorService pixelChecker = Executors.newCachedThreadPool();
        CompletionService<Result> completionService = new ExecutorCompletionService<>(pixelChecker);
        int numberOfTasks = 0;

        for (MyImage myImage : images){
            Image image = myImage.getImage();

            for (int x = 0; x <= image.getWidth(); x++){
                for (int y = 0; y <= image.getHeight(); y++){
                    PixelColorReader task = new PixelColorReader(x, y, image, images.indexOf(myImage));
                    completionService.submit(task);
                    numberOfTasks++;
                }
            }
        }

        int finalNumberOfTasks = numberOfTasks;

        Thread thread = new Thread(){
            public void run(){
                int completedTasks = 0;

                while (completedTasks < finalNumberOfTasks){
                    try {
                        Future<Result> future = completionService.take();
                        Result result = future.get();
                        if (result.getPixelColor() == PixelColor.RED)
                            images.get(result.getImageIndex()).addRedPixel();
                        else if (result.getPixelColor() == PixelColor.GREEN)
                            images.get(result.getImageIndex()).addGreenPixel();
                        else if (result.getPixelColor() == PixelColor.BLUE)
                            images.get(result.getImageIndex()).addBluePixel();
                        else
                            images.get(result.getImageIndex()).addMixedPixel();

                        completedTasks++;

                    } catch (ExecutionException | InterruptedException ignored) {
                    }
                }
            }
        };

        thread.start();


    }
}