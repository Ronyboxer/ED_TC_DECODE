package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.LimelightDistance;


@TeleOp
public class CustomBotTeleop extends OpMode {

    private DcMotor leftBack, leftFront, rightBack, rightFront;
    private DcMotor shooterLeft, shooterRight;
    private DcMotor intake;

    @Override
    public void init() {

        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");


        shooterLeft = hardwareMap.get(DcMotor.class, "shooterLeft");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");


        intake = hardwareMap.get(DcMotor.class, "intake");


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
    }

    @Override
    public void loop() {


        double x = gamepad2.right_stick_x * 0.6;
        double y = gamepad2.left_stick_y * 1.1;
        double rx = -gamepad2.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);



        double shooterPower = gamepad1.left_trigger * -0.67;


        shooterLeft.setPower(shooterPower);
        shooterRight.setPower(shooterPower);



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
    }
}
