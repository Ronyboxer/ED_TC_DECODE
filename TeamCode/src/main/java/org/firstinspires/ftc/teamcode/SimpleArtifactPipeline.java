package org.firstinspires.ftc.teamcode;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.List;

public class SimpleArtifactPipeline extends OpenCvPipeline {

    public boolean artifactInView = false;
    public String artifactColor = "None";

    @Override
    public Mat processFrame(Mat input) {
        // Convert to HSV color space (better for color detection)
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Create masks for green and purple
        Mat greenMask = new Mat();
        Mat purpleMask = new Mat();

        // Tune these HSV ranges under your lighting
        Core.inRange(hsv, new Scalar(35, 50, 50), new Scalar(85, 255, 255), greenMask);    // Green
        Core.inRange(hsv, new Scalar(125, 50, 50), new Scalar(155, 255, 255), purpleMask); // Purple

        // Find contours for each color
        List<MatOfPoint> greenContours = new ArrayList<>();
        List<MatOfPoint> purpleContours = new ArrayList<>();

        Imgproc.findContours(greenMask, greenContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(purpleMask, purpleContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Calculate largest contour area for each color
        double maxGreenArea = 0;
        for (MatOfPoint contour : greenContours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxGreenArea) maxGreenArea = area;
        }

        double maxPurpleArea = 0;
        for (MatOfPoint contour : purpleContours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxPurpleArea) maxPurpleArea = area;
        }

        // Check which color is visible
        double areaThreshold = 500.0; // Adjust depending on distance/size
        if (maxGreenArea > areaThreshold || maxPurpleArea > areaThreshold) {
            artifactInView = true;
            artifactColor = (maxGreenArea > maxPurpleArea) ? "Green" : "Purple";
        } else {
            artifactInView = false;
            artifactColor = "None";
        }

        return input; // Show original camera feed
    }
}
