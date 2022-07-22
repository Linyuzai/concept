package com.github.linyuzai.concept.sample.plugin;

public interface DeviceOperation {

    /**
     * 设备操作
     *
     * @param device  设备
     * @param opType  操作类型
     * @param opValue 操作值
     * @return 操作结果
     */
    OperationResult operate(Device device, String opType, Object opValue);

}
