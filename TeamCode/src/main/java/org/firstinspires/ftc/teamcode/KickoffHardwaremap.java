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
public class KickoffHardwaremap {

    //FtcDashboard dashboard = FtcDashboard.getInstance();
    //drive motors
    public DcMotor motorRF = null;
    public DcMotor motorLF = null;
    public DcMotor motorRB = null;
    public DcMotor motorLB = null;
    public DcMotor intake = null;
    public DcMotor launchL = null;
    public DcMotor launchR = null;



    public static double F = .175; // = 32767 / maxV      (do not edit from this number)
    public static double P = 0.025; // = 0.1 * F           (raise till real's apex touches Var apex)
    public static double I = 0;// = 0.1 * P           (fine ajustment of P)
    public static double D = 0.000;


    double PIDCurrentTime = 0;
    double PIDTime = 0;
    double PIDLastTime = 0;
    double PIDError = 0;
    double PIDPreviousError = 0;
    double PIDTotalError = 0;
    double PIDMinIntegral = -1.0;
    double PIDMaxIntegral = 1.0;
    double PIDMotorPower = 0;


    public KickoffHardwaremap(HardwareMap ahwMap, Telemetry telemetry) {


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

        intake = ahwMap.dcMotor.get("intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        intake.setPower(0);


        launchL = ahwMap.dcMotor.get("launchL");
        launchL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launchL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launchL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launchR = ahwMap.dcMotor.get("launchR");
        launchR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launchR.setDirection(DcMotorSimple.Direction.REVERSE);
        launchR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        launchR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    public void mecanumDrive(double forward, double strafe, double heading, double speed){

        motorRF.setPower((((forward - strafe) * 1) - (heading * 1)) * speed);
        motorRB.setPower((((forward + strafe) * 1) - (heading * 1)) * speed);
        motorLB.setPower((((forward + strafe) * 1) + (heading * 1)) * speed);
        motorLF.setPower((((forward - strafe) * 1) + (heading * 1)) * speed);
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

    public void setLaunch(int location, double power){

        double fPower = PID(location, launchR.getCurrentPosition()) * power;

        launchL.setPower(fPower);
        launchR.setPower(fPower);

    }

    public double PID(double target, double current) {
        PIDPreviousError = PIDError;
        PIDError = target - current;
        PIDLastTime = PIDCurrentTime;
        PIDCurrentTime = (double) System.nanoTime() / 1E9;
        double time = PIDCurrentTime - PIDLastTime;
        PIDTotalError += time * PIDError;
        PIDTotalError = PIDTotalError < PIDMinIntegral ? PIDMinIntegral : Math.min(PIDMaxIntegral, PIDTotalError);

        PIDMotorPower = (P * PIDError)
                + (I * PIDTotalError)
                + (D * (PIDError - PIDPreviousError) / time)
                + (F * (PIDError / Math.abs(PIDError)));
        return PIDMotorPower;
    }


}