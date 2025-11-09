package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class CustomBotTeleopLimelight extends OpMode {

    // Drive and mechanism motors
    private DcMotor leftBack, leftFront, rightBack, rightFront;
    private DcMotor shooterLeft, shooterRight;
    private DcMotor intake;

    // Limelight and IMU handler
    private Limelight3A limelight;
    private TestBench bench = new TestBench();

    // Distance tracking
    private double distanceCm = 0.0;

    @Override
    public void init() {
        // Initialize all motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        intake = hardwareMap.get(DcMotor.class, "intake");

        // Motor directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterLeft.setPower(0);
        shooterRight.setPower(0);
        intake.setPower(0);

        bench.init(hardwareMap);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5);
        limelight.start();

        telemetry.addLine("Initialized successfully!");
        telemetry.update();
    }

    @Override
    public void loop() {

        // Drive control
        double x = gamepad2.right_stick_x * 0.6;
        double y = gamepad2.left_stick_y * 1.1;
        double rx = -gamepad2.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);

        // Limelight + IMU updates
        YawPitchRollAngles orientation = bench.getOrientation();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            double ta = result.getTa();
            distanceCm = getDistanceFromTag(ta);
        }

        // Shooter auto power control
        double shooterPower;
        if (gamepad1.left_trigger > 0.1) {
            shooterPower = calculateShooterPower(distanceCm);
        } else {
            shooterPower = 0;
        }

        shooterLeft.setPower(shooterPower);
        shooterRight.setPower(shooterPower);

        // Intake control
        if (gamepad1.b) {
            intake.setPower(1);
        } else if (gamepad1.right_trigger > 0.1) {
            intake.setPower(-1);
        } else if (gamepad1.a) {
            intake.setPower(0.5);
        } else if (gamepad1.dpad_up) {
            intake.setPower(1);
        } else {
            intake.setPower(0);
        }

        telemetry.addData("Distance (cm)", "%.2f", distanceCm);
        telemetry.addData("Shooter Power", "%.2f", shooterPower);
    }

    private double getDistanceFromTag(double ta) {
        if (ta <= 0) return Double.POSITIVE_INFINITY;
        double scale = 30665.95; // Adjust using calibration
        return Math.sqrt(scale / ta);
    }

    // --- Added function ---
    private double calculateShooterPower(double distanceCm) {
        double shooterPower;

        if (distanceCm < 50) {
            shooterPower = 0.65;
        } else if (distanceCm > 50 && distanceCm < 100) {
            shooterPower = 0.7;
        } else if (distanceCm > 100 && distanceCm < 150) {
            shooterPower = 0.75;
        } else if (distanceCm > 150 && distanceCm < 200) {
            shooterPower = 0.8;
        } else if (distanceCm > 200 && distanceCm < 250) {
            shooterPower = 0.85;
        } else if (distanceCm > 250) {
            shooterPower = 0.9;
        } else {
            shooterPower = 0.0; // no valid reading
        }

        return shooterPower;
    }
}
