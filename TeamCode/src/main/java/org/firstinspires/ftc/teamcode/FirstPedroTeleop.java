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
    int liftPos = 0;
    int liftStage = 0;
    int armStage = 0;
    int armPos = 0;
    double extensionReleasePos = 0.5;

    boolean ButtonA = true;
    boolean ButtonB = true;
    boolean ButtonX = true;
    boolean ButtonY = true;
    boolean ButtonRB = true;
    boolean ButtonLB = true;

    @Override
    public void runOpMode() throws InterruptedException
    {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose());


        follower.startTeleopDrive();
        follower.update();

        FirstHardwaremap hardwaremap = new FirstHardwaremap(hardwareMap, telemetry);

        hardwaremap.bottomWrist.setPosition(0.4);
        hardwaremap.bottomClaw.setPosition(0.5);
        hardwaremap.liftClaw.setPosition(0.7);
        hardwaremap.liftWrist.setPosition(0.8);

        hardwaremap.arm.setTargetPosition(50);
        hardwaremap.arm.setPower(1);

        hardwaremap.extend.setTargetPosition(0);
        hardwaremap.extend.setPower(1);

        waitForStart();


        while (opModeIsActive() && !isStopRequested())
        {
            follower.setTeleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
            follower.update();


            //wrist WIP intake: 0.8   output: 1
            //buttonArray[0] = hardwaremap.servoFineAdjust(hardwaremap.liftWrist, gamepad1.dpad_up, gamepad1.dpad_down, buttonArray[0]);
            //claw open pos: 0.6      close: 0.8
            //buttonArray[1] = hardwaremap.servoFineAdjust(hardwaremap.liftClaw, gamepad1.dpad_left, gamepad1.dpad_right, buttonArray[1]);




            //bottom: 0.4         top: 0.9
            //buttonArray[2] = hardwaremap.servoFineAdjust(hardwaremap.bottomWrist, gamepad1.a, gamepad1.b, buttonArray[2]);

            //open pos: 0.3     close pos: 0.6
            //buttonArray[3] = hardwaremap.servoFineAdjust(hardwaremap.bottomClaw, gamepad1.x, gamepad1.y, buttonArray[3]);

            if (gamepad1.a && ButtonA){
                hardwaremap.bottomWrist.setPosition(0.4);
                ButtonA = false;
            } else if(!gamepad1.a && !ButtonA){
                hardwaremap.bottomWrist.setPosition(0.9);
                ButtonA = true;
            }

            if (gamepad1.b && ButtonB){
                hardwaremap.bottomClaw.setPosition(0.3);
                ButtonB = false;
            } else if(!gamepad1.b && !ButtonB){
                hardwaremap.bottomClaw.setPosition(0.6);
                ButtonB = true;
            }

            if (gamepad1.x && ButtonX){
                hardwaremap.liftWrist.setPosition(0.8);
                ButtonX = false;
            } else if(!gamepad1.x && !ButtonX){
                hardwaremap.liftWrist.setPosition(1);
                ButtonX = true;
            }

            if (gamepad1.y && ButtonY){
                hardwaremap.liftClaw.setPosition(0.6);
                ButtonY = false;
            } else if(!gamepad1.y && !ButtonY){
                hardwaremap.liftClaw.setPosition(0.8);
                ButtonY = true;
            }


            //full extension 2300
//            if (gamepad1.right_bumper){
//                extendPos += 5;
//            }
//            else if (gamepad1.left_bumper){
//                extendPos -= 5;
//            }
            extendPos = (int) gamepad1.right_trigger * 2300;
            hardwaremap.extend.setPower(1);
            hardwaremap.extend.setTargetPosition(extendPos);


            if (gamepad1.left_trigger < 0.5 && buttonArray[10]){
                liftStage++;
                if (liftStage > 2){
                    liftStage = 0;
                }
                buttonArray[10] = false;
            } else if (gamepad1.left_trigger > 0.5 && !buttonArray[10]){
                buttonArray[10] = true;
            }
            switch (liftStage){
                case 0:
                    liftPos = 25;
                    break;
                case 1:
                    liftPos = 2000;
                    break;
                case 2:
                    liftPos = 3000;
                    break;
            }
            //max lift height: 5350
            hardwaremap.liftL.setPower(1);
            hardwaremap.liftR.setPower(1);
            hardwaremap.liftL.setTargetPosition(liftPos);
            hardwaremap.liftR.setTargetPosition(liftPos);


            if (gamepad1.right_bumper && buttonArray[11]){
                armStage++;
                if (armStage > 3){
                    armStage = 0;
                }
                buttonArray[11] = false;
            } else if (!gamepad1.right_bumper && !buttonArray[11]){
                buttonArray[11] = true;
            }
            switch (armStage){
                case 0:
                    armPos = 25;
                    break;
                case 1:
                    armPos = 150;
                    break;
                case 2:
                    armPos = 600;
                    break;
                case 3:
                    armPos = 100;
                    break;
            }
            hardwaremap.arm.setPower(1);
            hardwaremap.arm.setTargetPosition(armPos);

            //extension release adjustment
            if (gamepad2.b){
                extensionReleasePos -= 0.1;
                hardwaremap.extensionRelease.setPosition(extensionReleasePos);
            }

            /*
            telemetry.addData("X pos: ", follower.poseTracker.getPose().getX());
            telemetry.addData("Y pos: ", follower.poseTracker.getPose().getY());
            telemetry.addData("Heading: ", follower.poseTracker.getPose().getHeading());

            telemetry.addData("Left pod value: ", follower.poseTracker);


             */

            telemetry.addData("Lift theory pos", liftPos);
            telemetry.addData("Lift pos", hardwaremap.liftL.getCurrentPosition());

            telemetry.addData("Arm theory pos", armPos);
            telemetry.addData("Arm pos", hardwaremap.arm.getCurrentPosition());

            telemetry.addData("Extend theory pos", extendPos);
            telemetry.addData("Extend pos", hardwaremap.extend.getCurrentPosition());
            telemetry.addData("Extend wrist pos", hardwaremap.bottomWrist.getPosition());
            telemetry.addData("Extend claw pos", hardwaremap.bottomClaw.getPosition());
            telemetry.addData("Lift wrist pos", hardwaremap.liftWrist.getPosition());
            telemetry.addData("Lift claw pos", hardwaremap.liftClaw.getPosition());


            telemetry.update();

        }
    }


}
