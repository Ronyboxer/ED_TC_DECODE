package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class BasicBotTeleop2 extends OpMode {

    private DcMotor leftBack, leftFront, rightBack, rightFront;
    private DcMotor shooter;
    private CRServo geckoLeft, geckoRight;
    private CRServo intake;   // <–– Intake Added

    private ElapsedTime timer = new ElapsedTime();

    private boolean shooterStarted = false;
    private boolean geckosStarted = false;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class,"leftFront");
        leftBack = hardwareMap.get(DcMotor.class,"leftBack");
        rightFront = hardwareMap.get(DcMotor.class,"rightFront");
        rightBack = hardwareMap.get(DcMotor.class,"rightBack");

        shooter = hardwareMap.get(DcMotor.class,"shooter");

        geckoLeft = hardwareMap.get(CRServo.class,"geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class,"geckoRight");

        intake = hardwareMap.get(CRServo.class,"intake");   // <–– Intake Added

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // startup state
        geckoLeft.setPower(0);
        geckoRight.setPower(0);
        intake.setPower(0);   // <–– Intake Added
    }

    @Override
    public void loop() {
        // --- Driving controls ---
        double x = -gamepad2.right_stick_x * 0.3; // Strafe
        double y = gamepad2.left_stick_y * 1.1;   // Forward/backward
        double rx = gamepad2.left_stick_x;        // Turn

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);

        // --- Shooter + Gecko sequence ---
        if (gamepad1.right_trigger > 0.1) {
            if (!shooterStarted) {
                shooter.setPower(0.8);
                timer.reset();
                shooterStarted = true;
                geckosStarted = false;
            }

            if (shooterStarted && !geckosStarted && timer.seconds() >= 0.5) {
                geckoLeft.setPower(-1);
                geckoRight.setPower(1);
                geckosStarted = true;
            }

        } else {
            shooter.setPower(0);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);
            shooterStarted = false;
            geckosStarted = false;
        }

        // --- Intake controls ---
        if (gamepad1.a) {  // intake in
            intake.setPower(1);
        } else if (gamepad1.b) {  // reverse intake
            intake.setPower(-1);
        } else {
            intake.setPower(0);  // stop
        }
    }
}
