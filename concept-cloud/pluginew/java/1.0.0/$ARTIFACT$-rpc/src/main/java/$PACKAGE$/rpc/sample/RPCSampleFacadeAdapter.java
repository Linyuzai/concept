package $PACKAGE$.rpc.sample;

import $PACKAGE$.domain.sample.Sample;
import $PACKAGE$.rpc.RemoteObjectFacadeAdapter;

/**
 * 示例领域模型和示例远程对象转换适配器
 */
public interface RPCSampleFacadeAdapter extends RemoteObjectFacadeAdapter<Sample, SampleRO> {
}