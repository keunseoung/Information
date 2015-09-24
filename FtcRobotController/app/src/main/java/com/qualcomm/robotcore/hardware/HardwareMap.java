package com.qualcomm.robotcore.hardware;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;

public class HardwareMap
{
    public DeviceMapping<AccelerationSensor> accelerationSensor;
    public DeviceMapping<AnalogInput> analogInput;
    public DeviceMapping<AnalogOutput> analogOutput;
    public Context appContext;
    public DeviceMapping<ColorSensor> colorSensor;
    public DeviceMapping<CompassSensor> compassSensor;
    public DeviceMapping<DcMotor> dcMotor;
    public DeviceMapping<DcMotorController> dcMotorController;
    public DeviceMapping<DeviceInterfaceModule> deviceInterfaceModule;
    public DeviceMapping<DigitalChannel> digitalChannel;
    public DeviceMapping<GyroSensor> gyroSensor;
    public DeviceMapping<I2cDevice> i2cDevice;
    public DeviceMapping<IrSeekerSensor> irSeekerSensor;
    public DeviceMapping<LED> led;
    public DeviceMapping<LegacyModule> legacyModule;
    public DeviceMapping<LightSensor> lightSensor;
    public DeviceMapping<OpticalDistanceSensor> opticalDistanceSensor;
    public DeviceMapping<PWMOutput> pwmOutput;
    public DeviceMapping<Servo> servo;
    public DeviceMapping<ServoController> servoController;
    public DeviceMapping<TouchSensor> touchSensor;
    public DeviceMapping<TouchSensorMultiplexer> touchSensorMultiplexer;
    public DeviceMapping<UltrasonicSensor> ultrasonicSensor;
    public DeviceMapping<VoltageSensor> voltageSensor;
    
    public HardwareMap() {
        this.dcMotorController = new DeviceMapping<DcMotorController>();
        this.dcMotor = new DeviceMapping<DcMotor>();
        this.servoController = new DeviceMapping<ServoController>();
        this.servo = new DeviceMapping<Servo>();
        this.legacyModule = new DeviceMapping<LegacyModule>();
        this.touchSensorMultiplexer = new DeviceMapping<TouchSensorMultiplexer>();
        this.deviceInterfaceModule = new DeviceMapping<DeviceInterfaceModule>();
        this.analogInput = new DeviceMapping<AnalogInput>();
        this.digitalChannel = new DeviceMapping<DigitalChannel>();
        this.opticalDistanceSensor = new DeviceMapping<OpticalDistanceSensor>();
        this.touchSensor = new DeviceMapping<TouchSensor>();
        this.pwmOutput = new DeviceMapping<PWMOutput>();
        this.i2cDevice = new DeviceMapping<I2cDevice>();
        this.analogOutput = new DeviceMapping<AnalogOutput>();
        this.colorSensor = new DeviceMapping<ColorSensor>();
        this.led = new DeviceMapping<LED>();
        this.accelerationSensor = new DeviceMapping<AccelerationSensor>();
        this.compassSensor = new DeviceMapping<CompassSensor>();
        this.gyroSensor = new DeviceMapping<GyroSensor>();
        this.irSeekerSensor = new DeviceMapping<IrSeekerSensor>();
        this.lightSensor = new DeviceMapping<LightSensor>();
        this.ultrasonicSensor = new DeviceMapping<UltrasonicSensor>();
        this.voltageSensor = new DeviceMapping<VoltageSensor>();
        this.appContext = null;
    }
    
    public void logDevices() {
        RobotLog.i("========= Device Information ===================================================");
        RobotLog.i(String.format("%-45s %-30s %s", "Type", "Name", "Connection"));
        this.dcMotorController.logDevices();
        this.dcMotor.logDevices();
        this.servoController.logDevices();
        this.servo.logDevices();
        this.legacyModule.logDevices();
        this.touchSensorMultiplexer.logDevices();
        this.deviceInterfaceModule.logDevices();
        this.analogInput.logDevices();
        this.digitalChannel.logDevices();
        this.opticalDistanceSensor.logDevices();
        this.touchSensor.logDevices();
        this.pwmOutput.logDevices();
        this.i2cDevice.logDevices();
        this.analogOutput.logDevices();
        this.colorSensor.logDevices();
        this.led.logDevices();
        this.accelerationSensor.logDevices();
        this.compassSensor.logDevices();
        this.gyroSensor.logDevices();
        this.irSeekerSensor.logDevices();
        this.lightSensor.logDevices();
        this.ultrasonicSensor.logDevices();
        this.voltageSensor.logDevices();
    }
    
    public static class DeviceMapping<DEVICE_TYPE> implements Iterable<DEVICE_TYPE>
    {
        private Map<String, DEVICE_TYPE> a;
        
        public DeviceMapping() {
            this.a = new HashMap<String, DEVICE_TYPE>();
        }
        
        public Set<Map.Entry<String, DEVICE_TYPE>> entrySet() {
            return this.a.entrySet();
        }
        
        public DEVICE_TYPE get(final String s) {
            final DEVICE_TYPE value = this.a.get(s);
            if (value == null) {
                throw new IllegalArgumentException(String.format("Unable to find a hardware device with the name \"%s\"", s));
            }
            return value;
        }
        
        @Override
        public Iterator<DEVICE_TYPE> iterator() {
            return this.a.values().iterator();
        }
        
        public void logDevices() {
            if (!this.a.isEmpty()) {
                for (final Map.Entry<String, DEVICE_TYPE> entry : this.a.entrySet()) {
                    if (entry.getValue() instanceof HardwareDevice) {
                        final HardwareDevice hardwareDevice = (HardwareDevice)entry.getValue();
                        RobotLog.i(String.format("%-45s %-30s %s", hardwareDevice.getDeviceName(), entry.getKey(), hardwareDevice.getConnectionInfo()));
                    }
                }
            }
        }
        
        public void put(final String s, final DEVICE_TYPE device_TYPE) {
            this.a.put(s, device_TYPE);
        }
        
        public int size() {
            return this.a.size();
        }
    }
}
