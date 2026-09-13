package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="Drive")
@Disabled

public class Drive extends LinearOpMode
{

    boolean resetExtension = true;
    double extensionPos;

    @Override
    public void runOpMode() throws InterruptedException
    {
        BasicHardwaremap hardware = new BasicHardwaremap(hardwareMap, telemetry);

        hardware.extend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionPos = 0.0;//starting pos
        hardware.extend.setPower(-0.25);
        sleep(100);

        //pull back lightly until we stop seeing encoder changes then stop
        while(resetExtension){
            sleep(100);
            if(extensionPos < hardware.extend.getCurrentPosition() + 25 && extensionPos > hardware.extend.getCurrentPosition() - 25) { // didn't move much
                hardware.extend.setPower(0.25);
                sleep(750);
                hardware.extend.setPower(0);
                hardware.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                resetExtension = false;
            } else {
                extensionPos = hardware.extend.getCurrentPosition();
            }
        }

        hardware.extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardware.extend.setTargetPosition(0);
        hardware.extend.setPower(1);

        waitForStart();

        while (opModeIsActive() && !isStopRequested())
        {
            hardware.mecanumDrive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, 0.5);
        }
    }
}