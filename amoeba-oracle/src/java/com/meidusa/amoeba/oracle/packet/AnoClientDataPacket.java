package com.meidusa.amoeba.oracle.packet;

import org.apache.log4j.Logger;

/**
 * @author hexianmao
 * @version 2008-8-11 ����04:17:45
 */
public class AnoClientDataPacket extends DataPacket implements AnoServices {

    private static Logger logger = Logger.getLogger(AnoClientDataPacket.class);

    int                   m;
    long                  version;
    int                   anoServiceSize;
    short                 h;
    AnoService[]          anoService;

    @Override
    public void init(byte[] buffer) {
        super.init(buffer);

        AnoPacketBuffer ano = new AnoPacketBuffer(buffer);
        ano.setPosition(pktOffset);

        if (ano.readUB4() != NA_MAGIC) {
            throw new RuntimeException("Wrong Magic number in na packet");
        }
        m = ano.readUB2();
        version = ano.readUB4();
        anoServiceSize = ano.readUB2();
        h = ano.readUB1();

        anoService = new AnoService[anoServiceSize];
        try {
            String pkgPrefix = "com.meidusa.amoeba.oracle.packet.";
            for (int i = 0; i < SERV_INORDER_CLASSNAME.length; i++) {
                anoService[i] = (AnoService) Class.forName(pkgPrefix + SERV_INORDER_CLASSNAME[i]).newInstance();
                anoService[i].setAno(ano);
                anoService[i].readClient();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this.toString());
        }
    }

    public AnoService[] getAnoService() {
        return anoService;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("AnoClientDataPacket info ==============================\n");
        return sb.toString();
    }

}