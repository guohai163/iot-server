package org.guohai.iot.protocol;

import lombok.Data;


/**
 * @author guohai
 */
@Data
public class LoginProtocol extends ProtocolBase{

    private String version;

    private String devId;

    private String sign;



}
