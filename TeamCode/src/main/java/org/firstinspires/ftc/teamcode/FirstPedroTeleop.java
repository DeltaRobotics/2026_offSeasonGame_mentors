package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="FirstPedroTeleop")
//@Disabled
public class FirstPedroTeleop extends LinearOpMode {

    public static Follower follower;

    boolean[] buttonArray = new boolean[20];

    @Override
    public void runOpMode() throws InterruptedException
    {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose());


        follower.startTeleopDrive();
        follower.update();

        FirstHardwaremap hardwaremap = new FirstHardwaremap(hardwareMap, telemetry);

        hardwaremap.bottomWrist.setPosition(0.5);
        hardwaremap.bottomClaw.setPosition(0.5);

        waitForStart();

        while (opModeIsActive() && !isStopRequested())
        {
            follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            follower.update();

            buttonArray[0] = hardwaremap.servoFineAdjust(hardwaremap.bottomWrist, gamepad1.a, gamepad1.b, buttonArray[0]);

        }
    }


}
