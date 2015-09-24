package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogInputController extends HardwareDevice {
   int getAnalogInputValue(int var1);

   SerialNumber getSerialNumber();
}
