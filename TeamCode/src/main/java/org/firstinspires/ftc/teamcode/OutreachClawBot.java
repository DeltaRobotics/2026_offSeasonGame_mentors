package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="OutreachClawTeleop")
@Disabled

public class OutreachClawBot extends LinearOpMode
{

    @Override
    public void runOpMode() throws InterruptedException
    {
        OutreachHardwaremap hardware = new OutreachHardwaremap(hardwareMap, telemetry);

        boolean[] buttonArray = new boolean[20];

        hardware.arm.setPosition(0);
        hardware.wrist.setPosition(0.9);
        hardware.claw.setPosition(0.85);

        waitForStart();

//        follower.startTeleopDrive();
//        follower.update();

        while (opModeIsActive() && !isStopRequested())
        {
            hardware.mecanumDrive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, 0.5);

            //buttonArray[0] = hardware.servoFineAdjust(hardware.arm, gamepad1.a, gamepad1.b, buttonArray[0]);
            //buttonArray[1] = hardware.servoFineAdjust(hardware.wrist, gamepad1.x, gamepad1.y, buttonArray[1]);
            //buttonArray[2] = hardware.servoFineAdjust(hardware.claw, gamepad1.dpad_left, gamepad1.dpad_right, buttonArray[2]);
            if (gamepad1.a) {
                hardware.arm.setPosition(0);
                hardware.wrist.setPosition(0.9);
            }
            if (gamepad1.b) {
                hardware.arm.setPosition(0.4);
                hardware.wrist.setPosition(0.1);
            }
            if (gamepad1.right_bumper) {
                hardware.claw.setPosition(0.6);
            }
            if (gamepad1.left_bumper) {
                hardware.claw.setPosition(0.85);
            }
            telemetry.addData("arm pos: ", hardware.arm.getPosition());
            telemetry.addData("wrist pos: ", hardware.wrist.getPosition());
            telemetry.addData("claw pos: ", hardware.claw.getPosition());


            telemetry.update();
        }
    }
}