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
    int extendPos = 0;

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


            //bottom: 0.05         top: 0.75
            //buttonArray[0] = hardwaremap.servoFineAdjust(hardwaremap.bottomWrist, gamepad1.a, gamepad1.b, buttonArray[0]);

            //open pos: 0.9     close pos: 0.55
            //buttonArray[1] = hardwaremap.servoFineAdjust(hardwaremap.bottomClaw, gamepad1.x, gamepad1.y, buttonArray[1]);

            if (gamepad1.a){
                hardwaremap.bottomWrist.setPosition(0.05);//down
            }
            else if (gamepad1.b){
                hardwaremap.bottomWrist.setPosition(0.75);//up
            }

            if (gamepad1.x){
                hardwaremap.bottomClaw.setPosition(0.9);//open
            }
            else if (gamepad1.y){
                hardwaremap.bottomClaw.setPosition(0.55);//open
            }


            if (gamepad1.right_bumper){
                extendPos++;
            }
            else if (gamepad1.left_bumper){
                extendPos--;
            }
            hardwaremap.extend.setPower(1);
            hardwaremap.extend.setTargetPosition(extendPos);


            telemetry.addData("Extend theory pos", extendPos);
            telemetry.addData("Extend pos", hardwaremap.extend.getCurrentPosition());
            telemetry.addData("Wrist pos", hardwaremap.bottomWrist.getPosition());
            telemetry.addData("Claw pos", hardwaremap.bottomClaw.getPosition());
            telemetry.update();

        }
    }


}
