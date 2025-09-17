package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class BasicBotTeleop extends OpMode {

    private DcMotor leftBack, leftFront, rightBack, rightFront ;

    private DcMotor shooter;

    private CRServo intake;

    private CRServo geckoLeft, geckoRight;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class,"leftFront");
        leftBack = hardwareMap.get(DcMotor.class,"leftBack");
        rightFront = hardwareMap.get(DcMotor.class,"rightFront");
        rightBack = hardwareMap.get(DcMotor.class,"rightBack");

        shooter = hardwareMap.get(DcMotor.class,"shooter");

        intake = hardwareMap.get(CRServo.class,"intake");

        geckoLeft = hardwareMap.get(CRServo.class,"geckoLeft");

        geckoRight = hardwareMap.get(CRServo.class,"geckoRight");

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setDirection((DcMotorSimple.Direction.FORWARD));

        intake.setPower(1);
        geckoLeft.setPower(1);
        geckoRight.setPower(1);


    }

    @Override
    public void loop() {
        double x = -gamepad1.right_stick_x * 0.3; // This makes the robot strafe
        double y = -gamepad1.left_stick_x * 1.1; // This makes the robot turn left and right
        double rx = gamepad1.left_stick_y;// This makes the robot go forward and backward

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);

        if (gamepad1.right_trigger > 0.1) {
            intake.setPower(1);
            geckoLeft.setPower(-1);
            geckoRight.setPower(1);
        } else {
            intake.setPower(0);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);
        }

        if (gamepad1.left_trigger > 0.1) {
            shooter.setPower(0.7);
        } else if (gamepad1.left_bumper) {
            shooter.setPower(1.0);
        } else {
            shooter.setPower(0);
        }

    }
}
