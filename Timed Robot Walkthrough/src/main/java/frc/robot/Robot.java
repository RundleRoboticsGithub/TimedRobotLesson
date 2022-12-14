// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Spark frontLeft;
  private Spark frontRight;
  private Spark backLeft;
  private Spark backRight;

  private MotorControllerGroup leftMotors;
  private MotorControllerGroup rightMotors;

  private DifferentialDrive drive;

  private XboxController joystick;

  private PWMVictorSPX name;

  private DoubleSolenoid solenoid;

  private Compressor compress;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    frontLeft = new Spark(0);
    frontRight = new Spark(1);
    backLeft = new Spark(2);
    backRight = new Spark(3);

    leftMotors = new MotorControllerGroup(frontLeft, backLeft);
    rightMotors = new MotorControllerGroup(frontRight, backRight);

    leftMotors.setInverted(true);

    drive = new DifferentialDrive(leftMotors, rightMotors);

    joystick = new XboxController(0);

    solenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

    compress = new Compressor(PneumaticsModuleType.CTREPCM);
    
    //you are all such great coders
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    compress.enableDigital();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    double leftTrigger = joystick.getLeftTriggerAxis();
    double rightTrigger = joystick.getRightTriggerAxis();

    double forwardSpeed = rightTrigger - leftTrigger;

    double turnSpeed = joystick.getLeftX();

    drive.arcadeDrive(forwardSpeed, turnSpeed);

    if (joystick.getAButtonPressed()){
      solenoid.toggle();
    }

    else if (joystick.getBButtonPressed()){
      solenoid.set(DoubleSolenoid.Value.kForward);
    }


  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    compress.disable();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
