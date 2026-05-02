package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

//@Config //We need this for Dashboard to change variables
public class FirstHardwaremap {

    //FtcDashboard dashboard = FtcDashboard.getInstance();
    //drive motors
    public DcMotor motorRF = null;
    public DcMotor motorLF = null;
    public DcMotor motorRB = null;
    public DcMotor motorLB = null;
    public DcMotor extend = null;

    public IMU imu         = null;

    public Servo bottomWrist = null;
    public Servo bottomClaw = null;
    public Telemetry telemetry;


    public FirstHardwaremap(HardwareMap ahwMap, Telemetry telemetry) {

        this.telemetry = telemetry;
        /*
        //drive motors
        motorRF = ahwMap.dcMotor.get("motorRF");
        motorLF = ahwMap.dcMotor.get("motorLF");
        motorRB = ahwMap.dcMotor.get("motorRB");
        motorLB = ahwMap.dcMotor.get("motorLB");

        //drive motors and odometry encoders
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorLF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorRF.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRB.setDirection(DcMotorSimple.Direction.REVERSE);

        motorRF.setPower(0);
        motorLF.setPower(0);
        motorRB.setPower(0);
        motorLB.setPower(0);

         */

        extend = ahwMap.dcMotor.get("extend");

        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        extend.setTargetPosition(0);
        extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);



        bottomClaw = ahwMap.servo.get("bottomClaw");
        bottomWrist = ahwMap.servo.get("bottomWrist");
    }

    public void mecanumDrive(double forward, double strafe, double heading, double speed){

        motorRF.setPower((((forward - strafe) * 1) - (heading * 1)) * speed);
        motorRB.setPower((((forward + strafe) * 1) - (heading * 1)) * speed);
        motorLB.setPower((((forward - strafe) * 1) + (heading * 1)) * speed);
        motorLF.setPower((((forward + strafe) * 1) + (heading * 1)) * speed);
    }

    public boolean servoFineAdjust(Servo servo, boolean up, boolean down, boolean checker){

        if (up){
            if (checker){
                servo.setPosition(servo.getPosition() + 0.05);
                checker = false;
            }
            return checker;
        }
        if (down){
            if (checker){
                servo.setPosition(servo.getPosition() - 0.05);
                checker = false;
            }
            return checker;
        }

        if (!up && !down && !checker){
            return true;
        }

        return checker;
    }




}