package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="MentorKickoff")
//@Disabled

public class MentorKickoff extends LinearOpMode
{
    int launchPos = 0;
    int launchValue = 100;

    @Override
    public void runOpMode() throws InterruptedException
    {
        KickoffHardwaremap hardware = new KickoffHardwaremap(hardwareMap, telemetry);

        waitForStart();

        while (!isStopRequested() && opModeIsActive()){

            hardware.mecanumDrive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, 1);

            if (gamepad1.right_trigger > 0.5){
                hardware.intake.setPower(0.6);
            }
            else if (gamepad1.left_trigger > 0.5){
                hardware.intake.setPower(-0.6);
            }
            else {
                hardware.intake.setPower(0);
            }

            if (gamepad1.a){
                launchPos = 1;
            }
            if (gamepad1.b){
                launchPos = 0;
            }

            if (gamepad1.dpad_up){

            }


            switch (launchPos){
                case 1:
                    hardware.setLaunch(100, 1);
                    break;

                case 0:
                    hardware.setLaunch(-8, 0.3);
            }

            telemetry.addData("launch power", hardware.launchR.getPower());
            telemetry.update();
        }
    }
}