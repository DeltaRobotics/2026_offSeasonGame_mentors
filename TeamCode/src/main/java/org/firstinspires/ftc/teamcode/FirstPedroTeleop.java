package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="FirstPedroTeleop")
//@Disabled
public class FirstPedroTeleop extends LinearOpMode {

    public static Follower follower;

    boolean[] buttonArray = new boolean[20];
    int extendPos = 0;
    int liftPos = 0;
    int liftStage = 2;
    int armStage = 0;
    int armPos = 200;
    int closingCycles = 0;
    double extensionReleasePos = 0.5;
    double extensionPos;
    double lastExtendPos;

    boolean ExtendingOut = false;
    int Placing = 0;
    boolean ButtonA = true;
    boolean ButtonX = true;
    boolean ButtonDU = true;
    boolean ButtonY = true;
    boolean ButtonLB = true;
    boolean transferring = false;

    @Override
    public void runOpMode() throws InterruptedException
    {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose());


        follower.startTeleopDrive();
        follower.update();

        FirstHardwaremap hardwaremap = new FirstHardwaremap(hardwareMap, telemetry);

        hardwaremap.arm.setTargetPosition(300);
        hardwaremap.arm.setPower(1);

        sleep(500);

        hardwaremap.liftClaw.setPosition(0.7);
        hardwaremap.liftWrist.setPosition(0.8);

        sleep(1000);

        hardwaremap.bottomWrist.setPosition(0.36);
        hardwaremap.bottomClaw.setPosition(0.5);

        hardwaremap.arm.setTargetPosition(50);
        hardwaremap.arm.setPower(.5);

        sleep(1000);

        hardwaremap.arm.setTargetPosition(0);
        hardwaremap.arm.setPower(0);

        sleep(1000);

        hardwaremap.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sleep(500);

        hardwaremap.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hardwaremap.arm.setTargetPosition(300);
        hardwaremap.arm.setPower(1);

        sleep(1000);

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

            //bottom extender
            if (gamepad1.right_trigger > lastExtendPos){
                hardwaremap.bottomClaw.setPosition(0.3);
            }
            else if (gamepad1.right_trigger < lastExtendPos){
                hardwaremap.bottomClaw.setPosition(0.65);
                closingCycles = 10;
            }

            if (closingCycles > 0){
                closingCycles--;
                if (closingCycles < 5){
                    hardwaremap.bottomWrist.setPosition(0.9);
                }
            }
            else {
                //max is 2300
                extendPos = (int) (gamepad1.right_trigger * 1200);
            }
            if (hardwaremap.extend.getCurrentPosition() > 500 && gamepad1.right_trigger > 0.5){
                hardwaremap.bottomWrist.setPosition(0.36);
            }
            hardwaremap.extend.setPower(.75);
            hardwaremap.extend.setTargetPosition(extendPos);

            //auto open claw on extend
            if(gamepad1.right_trigger > 0.05){
                //moving out
                ExtendingOut = true;
                //hardwaremap.bottomClaw.setPosition(0.3);
            } else {
                // coming back
                if(ExtendingOut){
                    //hardwaremap.bottomClaw.setPosition(0.6);
                    ExtendingOut = false;
                }
            }
            lastExtendPos = gamepad1.right_trigger;


            //Extension claw release for ground junctions
            if (gamepad1.x && ButtonX){
                if(hardwaremap.bottomClaw.getPosition() > 0.4){
                    hardwaremap.bottomClaw.setPosition(0.3);
                } else {
                    hardwaremap.bottomClaw.setPosition(0.65);
                }
                ButtonX = false;
            } else if(!gamepad1.x && !ButtonX){
                ButtonX = true;
            }

            //bottom wrist movement
            if (gamepad1.dpad_up && ButtonDU){
                if(hardwaremap.bottomWrist.getPosition() > 0.5){
                    hardwaremap.bottomWrist.setPosition(0.36);
                } else {
                    hardwaremap.bottomWrist.setPosition(0.9);
                }
                ButtonDU = false;
            } else if(!gamepad1.dpad_up && !ButtonDU){
                ButtonDU = true;
            }

            //load arm and place driver assist
            if (gamepad1.left_bumper && ButtonLB){
                if(Placing == 2){
                    //move to wait
                    hardwaremap.liftClaw.setPosition(0.7);
                    hardwaremap.liftWrist.setPosition(0.8);
                    liftPos = 25;
                    armPos = 200;
                    Placing = 0;
                } else if (Placing == 1){
                    //move to the placing position

                    hardwaremap.liftClaw.setPosition(0.9);
                    hardwaremap.bottomClaw.setPosition(0.3);
                    //TODO fix all things and use the boolean to make work out side of this if

                    hardwaremap.liftWrist.setPosition(1);
                    liftStage = 2;
                    Placing = 2;
                }
                else {
                    //move back to the transfer
                    hardwaremap.liftClaw.setPosition(0.7);
                    hardwaremap.liftWrist.setPosition(0.8);
                    liftPos = 25;
                    armPos = 100;
                    Placing = 1;
                }
                ButtonLB = false;
            } else if(!gamepad1.left_bumper && !ButtonLB){
                ButtonLB = true;
            }

            //lift and arm positions
            if(Placing == 1){
                //move down
                if (gamepad1.a && ButtonA){
                    liftStage -= 1;
                    ButtonA = false;
                } else if(!gamepad1.a && !ButtonA){
                    ButtonA = true;
                }

                //move up
                if (gamepad1.y && ButtonY){
                    liftStage += 1;
                    ButtonY = false;
                } else if(!gamepad1.y && !ButtonY){
                    ButtonY = true;
                }

                if(liftStage > 3){
                    liftStage = 1;
                }
                if(liftStage < 1){
                    liftStage = 3;
                }

                switch(liftStage){
                    case 1:
                        liftPos = 25;
                        armPos = 650;
                        break;
                    case 2:
                        liftPos = 25;
                        armPos = 600;
                        break;
                    case 3:
                        liftPos = 1000;
                        armPos = 500;
                        break;
                }

            }

            hardwaremap.arm.setPower(1);
            hardwaremap.arm.setTargetPosition(armPos);

            hardwaremap.liftL.setPower(1);
            hardwaremap.liftR.setPower(1);
            hardwaremap.liftL.setTargetPosition(liftPos);
            hardwaremap.liftR.setTargetPosition(liftPos);

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

            telemetry.addData("\nX pos", follower.getPose().getX());
            telemetry.addData("Y pos", follower.getPose().getY());
            telemetry.addData("Heading", follower.getPose().getHeading());

            telemetry.addData("\nLF pos", hardwareMap.dcMotor.get("motorLF").getCurrentPosition());
            telemetry.addData("RB pos", hardwareMap.dcMotor.get("motorRB").getCurrentPosition());
            telemetry.addData("RF pos", hardwareMap.dcMotor.get("motorRF").getCurrentPosition());


            telemetry.update();

        }
    }


}
