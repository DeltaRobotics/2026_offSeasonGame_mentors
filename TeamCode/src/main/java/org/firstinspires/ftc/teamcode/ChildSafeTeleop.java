package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="ChildSafeTeleop")
//@Disabled
public class ChildSafeTeleop extends LinearOpMode {

    public static Follower follower;

    boolean[] buttonArray = new boolean[20];
    int extendPos = 0;
    int liftPos = 0;
    int liftStage = 0;
    int armStage = 0;
    int armPos = 0;
    double extensionPos;

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
        hardwaremap.liftWrist.setPosition(0.7);

        hardwaremap.arm.setDirection(DcMotorSimple.Direction.REVERSE);
        hardwaremap.arm.setTargetPosition(50);
        hardwaremap.arm.setPower(1);

        boolean resetExtension = true;
        hardwaremap.extend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionPos = 0.0;//starting pos
        hardwaremap.extend.setPower(-0.25);
        sleep(100);

        //pull back lightly until we stop seeing encoder changes then stop
        while(resetExtension){
            sleep(100);
            if(extensionPos < hardwaremap.extend.getCurrentPosition() + 25 && extensionPos > hardwaremap.extend.getCurrentPosition() - 25) { // didn't move much
                hardwaremap.extend.setPower(0.25);
                sleep(750);
                hardwaremap.extend.setPower(0);
                hardwaremap.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                resetExtension = false;
            } else {
                extensionPos = hardwaremap.extend.getCurrentPosition();
            }
        }

        hardwaremap.extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardwaremap.extend.setTargetPosition(0);
        hardwaremap.extend.setPower(1);

        waitForStart();


        while (opModeIsActive() && !isStopRequested())
        {
            follower.setTeleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
            follower.update();


            if (gamepad1.a && ButtonA){
                if(hardwaremap.bottomWrist.getPosition() > 0.5){
                    hardwaremap.bottomWrist.setPosition(0.4);
                } else {
                    hardwaremap.bottomWrist.setPosition(0.9);
                }
                ButtonA = false;
            } else if(!gamepad1.a && !ButtonA){
                ButtonA = true;
            }

            if (gamepad1.b && ButtonB){
                if(hardwaremap.bottomClaw.getPosition() > 0.4){
                    hardwaremap.bottomClaw.setPosition(0.3);
                } else {
                    hardwaremap.bottomClaw.setPosition(0.6);
                }
                ButtonB = false;
            } else if(!gamepad1.b && !ButtonB){
                ButtonB = true;
            }

            if (gamepad1.x && ButtonX){
                if(hardwaremap.liftWrist.getPosition() > 0.75){
                    hardwaremap.liftWrist.setPosition(0.7);
                } else {
                    hardwaremap.liftWrist.setPosition(0.8);
                }
                ButtonX = false;
            } else if(!gamepad1.x && !ButtonX){
                ButtonX = true;
            }

            if (gamepad1.y && ButtonY){
                if(hardwaremap.liftClaw.getPosition() > 0.7){
                    hardwaremap.liftClaw.setPosition(0.6);
                } else {
                    hardwaremap.liftClaw.setPosition(0.8);
                }
                ButtonY = false;
            } else if(!gamepad1.y && !ButtonY){
                ButtonY = true;
            }


            //full extension 2300
//            if (gamepad1.right_bumper){
//                extendPos += 5;
//            }
//            else if (gamepad1.left_bumper){
//                extendPos -= 5;
//            }
            extendPos = (int) gamepad1.right_trigger * 500;
            hardwaremap.extend.setPower(1);
            hardwaremap.extend.setTargetPosition(extendPos);


//            if (gamepad1.left_trigger < 0.5 && buttonArray[10]){
//                liftStage++;
//                if (liftStage > 2){
//                    liftStage = 0;
//                }
//                buttonArray[10] = false;
//            } else if (gamepad1.left_trigger > 0.5 && !buttonArray[10]){
//                buttonArray[10] = true;
//            }
//            switch (liftStage){
//                case 0:
//                    liftPos = 25;
//                    break;
//                case 1:
//                    liftPos = 2000;
//                    break;
//                case 2:
//                    liftPos = 3000;
//                    break;
//            }
//            //max lift height: 5350
//            hardwaremap.liftL.setPower(1);
//            hardwaremap.liftR.setPower(1);
//            hardwaremap.liftL.setTargetPosition(liftPos);
//            hardwaremap.liftR.setTargetPosition(liftPos);


            if (gamepad1.right_bumper && buttonArray[11]){
                armStage++;
                if (armStage > 2){
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
                    armPos = 300;
                    break;
            }
            hardwaremap.arm.setPower(1);
            hardwaremap.arm.setTargetPosition(armPos);

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
