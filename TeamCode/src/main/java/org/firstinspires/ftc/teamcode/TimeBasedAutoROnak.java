package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="BlueLaunchAuto_Loop", group="Auto")
public class TimeBasedAutoROnak extends LinearOpMode {

    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private DcMotor shooter;
    private CRServo geckoLeft, geckoRight;
    private CRServo intake;

    @Override
    public void runOpMode() {
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");

        shooter    = hardwareMap.get(DcMotor.class, "shooter");
        geckoLeft  = hardwareMap.get(CRServo.class, "geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class, "geckoRight");
        intake     = hardwareMap.get(CRServo.class, "intake");

        // motor directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // stop everything
        shooter.setPower(0);
        geckoLeft.setPower(0);
        geckoRight.setPower(0);
        intake.setPower(0);

        telemetry.addData("Status", "Ready for start");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        // --- LOOP: Drive → Shoot → Return ---
        while (opModeIsActive()) {

            drive(0.4, 0.4, 0.4, 0.4, 800);

            drive(0.6, -0.6, -0.6, 0.6, 1500);
            shooter.setPower(0.67);
            sleep(2000);


            geckoLeft.setPower(1.0);
            geckoRight.setPower(-1.0);
            intake.setPower(1.0);
            sleep(1000);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);
            intake.setPower(0);

            // 5️⃣ Stop shooter
            shooter.setPower(0);

            // 6️⃣ Strafe back left to Loading Zone (simulate reload)
            drive(-0.6, 0.6, 0.6, -0.6, 1500);

            // 7️⃣ Pause briefly before next loop
            sleep(1000);
        }
    }

    // helper function for timed motor control
    private void drive(double lf, double lb, double rf, double rb, int timeMs) {
        leftFront.setPower(lf);
        leftBack.setPower(lb);
        rightFront.setPower(rf);
        rightBack.setPower(rb);
        sleep(timeMs);
        stopDrive();
    }

    private void stopDrive() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
