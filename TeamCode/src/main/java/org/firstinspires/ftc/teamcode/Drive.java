package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Drive")
@Disabled

public class Drive extends LinearOpMode
{

    @Override
    public void runOpMode() throws InterruptedException
    {
        BasicHardwaremap hardware = new BasicHardwaremap(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive() && !isStopRequested())
        {
            hardware.mecanumDrive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, 0.5);
        }
    }
}