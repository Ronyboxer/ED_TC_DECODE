package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

@TeleOp(name = "Artifact Detection (Color)", group = "Vision")
public class ArtifactDetectionOpenCV extends LinearOpMode {

    OpenCvCamera webcam;
    SimpleArtifactPipeline pipeline;

    @Override
    public void runOpMode() {
        telemetry.addLine("Initializing camera...");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources()
                .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pipeline = new SimpleArtifactPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Camera Error", errorCode);
                telemetry.update();
            }
        });

        telemetry.addLine("Camera ready. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (pipeline.artifactInView) {
                telemetry.addLine("✅ Artifact is in view");
                telemetry.addData("Color", pipeline.artifactColor);
            } else {
                telemetry.addLine("❌ Artifact not in view");
            }
            telemetry.update();
        }

        webcam.stopStreaming();
    }
}
