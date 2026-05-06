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
        hardwaremap.liftClaw.setPosition(0.65);
        hardwaremap.liftWrist.setPosition(0.5);

        waitForStart();


        while (opModeIsActive() && !isStopRequested())
        {
            follower.setTeleOpDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, true);
            follower.update();


            //claw open pos: 1      close: 0.55
            buttonArray[0] = hardwaremap.servoFineAdjust(hardwaremap.liftClaw, gamepad1.dpad_up, gamepad1.dpad_down, buttonArray[0]);
            //wrist WIP
            buttonArray[1] = hardwaremap.servoFineAdjust(hardwaremap.liftWrist, gamepad1.dpad_left, gamepad1.dpad_right, buttonArray[1]);




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
                extendPos += 5;
            }
            else if (gamepad1.left_bumper){
                extendPos -= 5;
            }
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
                    liftPos = 150;
                    break;
                case 1:
                    liftPos = 2500;
                    break;
                case 2:
                    liftPos = 5350;
                    break;
            }
            //max lift height: 5350
            hardwaremap.liftL.setPower(1);
            hardwaremap.liftR.setPower(1);
            hardwaremap.liftL.setTargetPosition(liftPos);
            hardwaremap.liftR.setTargetPosition(liftPos);




            if (gamepad1.right_trigger < 0.5 && buttonArray[11]){
                armStage++;
                if (armStage > 1){
                    armStage = 0;
                }
                buttonArray[11] = false;
            } else if (gamepad1.right_trigger > 0.5 && !buttonArray[11]){
                buttonArray[11] = true;
            }
            switch (armStage){
                case 0:
                    armPos = 25;
                    break;
                case 1:
                    armPos = 550;
                    break;
            }
            hardwaremap.arm.setPower(1);
            hardwaremap.arm.setTargetPosition(armPos);



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
