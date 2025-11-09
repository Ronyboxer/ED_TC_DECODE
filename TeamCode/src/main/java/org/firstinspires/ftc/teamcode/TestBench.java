package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class TestBench {
    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private DcMotor shooterLeft, shooterRight;
    private DcMotor intake;
    private IMU imu;
    private double ticksPerRev;

    public void init(HardwareMap hwMap) {

        // Initialize all motors
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        shooterLeft = hwMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hwMap.get(DcMotor.class, "shooterRight");
        intake = hwMap.get(DcMotor.class, "intake");

        // Set directions (based on your teleop)
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        // Set zero power behavior
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use encoders for drive motors (optional)
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ticksPerRev = leftFront.getMotorType().getTicksPerRev();

        imu = hwMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );

        imu.initialize(new IMU.Parameters(revOrientation));
    }

    public void setDrivePower(double lf, double lb, double rf, double rb) {
        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
    }

    public void setShooterPower(double power) {
        shooterLeft.setPower(power);
        shooterRight.setPower(power);
    }

    public void setIntakePower(double power) {
        intake.setPower(power);
    }

    // ---------- UTILITIES ----------
    public double getMotorRevs() {
        return leftFront.getCurrentPosition() / ticksPerRev;
    }

    public YawPitchRollAngles getOrientation() {
        return imu.getRobotYawPitchRollAngles();
    }


    public double getDistanceFromTag(double ta) {
        if (ta <= 0) {
            return Double.POSITIVE_INFINITY;
        }

        double scale = 30665.95; // based on calibration table from Limelight docs
        return Math.sqrt(scale / ta);
    }
}
