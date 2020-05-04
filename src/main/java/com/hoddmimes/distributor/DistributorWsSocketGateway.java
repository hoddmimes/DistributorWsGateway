package com.hoddmimes.distributor;


import com.hoddmimes.jaux.AuxParseArguments;
import com.hoddmimes.jaux.AuxXml;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class DistributorWsSocketGateway {

    public static void main(String[] args) {
        parseArguments(args);
        try {
            Configuration tConfig = Configuration.getInstance();

            SpringApplication tApplication = new SpringApplication(DistributorWsSocketGateway.class);
            Properties tAppProperties = new Properties();

            tAppProperties.setProperty("server.port", String.valueOf(tConfig.getHttpPort()));
            if (tConfig.isSSLEnabled()) {
                tAppProperties.setProperty("server.ssl.key-store-type","PKCS12");
                tAppProperties.setProperty("server.ssl.key-store",tConfig.getSSLKeyStore());
                tAppProperties.setProperty("server.ssl.key-alias",tConfig.getKeyStoreAlias());
                tAppProperties.setProperty("server.ssl.key-store-password",tConfig.getKeyStorePassword());
            }

            tApplication.setDefaultProperties(tAppProperties);
            tApplication.setBannerMode(Banner.Mode.OFF);
            tApplication.run(args);
            //SpringApplication.run(DistributorWsSocketGateway.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseArguments(String[] args) {
        Configuration tConfig = Configuration.getInstance();
        String tConfigFilename = AuxParseArguments.parse(args, "configuration", null);
        if (tConfigFilename != null) {
            try {
                Element tRoot = AuxXml.loadXMLFromFile(tConfigFilename).getDocumentElement();
                tConfig.setDistributorAppName(AuxXml.getStringAttribute(tRoot, "appName", "DistributorWebSocketGateway"));
                tConfig.setClientSenderThreads(AuxXml.getIntAttribute(tRoot, "clientSenderThreads", 2));
                tConfig.setLocalIpAddress(AuxXml.getStringAttribute(tRoot, "localIpAddress", null));
                tConfig.setEthDevice(AuxXml.getStringAttribute(tRoot, "ethDevice", null));
                tConfig.setCmaAddress(AuxXml.getStringAttribute(tRoot, "cmaAddress", DistributorApplicationConfiguration.DEFAULT_CMA_ADDRESS));
                tConfig.setCmaPort(AuxXml.getIntAttribute(tRoot, "cmaPort", DistributorApplicationConfiguration.DEAFULT_CMA_PORT));
                tConfig.setIpBufferSize(AuxXml.getIntAttribute(tRoot, "ipBufferSize", 128000));
                tConfig.setSegmentSize(AuxXml.getIntAttribute(tRoot, "segmentSize", 8192));
                tConfig.setPayloadB64(AuxXml.getBooleanAttribute(tRoot, "payloadB64", false));
                if (AuxXml.isAttributePresent(tRoot, "logFlags")) {
                    tConfig.setLogFlags(AuxXml.getIntAttribute(tRoot, "logFlags", 0));
                }

                tConfig.setHttpPort(AuxXml.getIntAttribute(tRoot, "httpPort", 8080));
                tConfig.setSSLKeyStore(AuxXml.getStringAttribute(tRoot, "sslKeyStore", null));
                tConfig.setKeyStoreAlias(AuxXml.getStringAttribute(tRoot, "keyStoreAlias", null));
                tConfig.setKeyStorePassword(AuxXml.getStringAttribute(tRoot, "keyStorePassword", null));

                Element tElement = AuxXml.getElement(tRoot, "AllowedClients");
                if (AuxXml.getBooleanAttribute(tElement,"enabled", false)) {
                    List<Element> tAllowedClientElements = AuxXml.findChildElements(tRoot, "AllowedClients");
                    if (tAllowedClientElements != null) {
                        for (Element tHostElement : tAllowedClientElements) {
                            tConfig.addAllowedClientAddresses(AuxXml.getStringAttribute(tHostElement, "ipAddress", null));
                        }
                    }
                }

                tElement = AuxXml.getElement(tRoot, "AuthorizedUsers");
                if (AuxXml.getBooleanAttribute(tElement,"enabled", false)) {
                    List<Element> tAuthorizedUsersElements = AuxXml.findChildElements(tRoot, "AuthorizedUsers");
                    if (tAuthorizedUsersElements != null) {
                        for (Element tUserElement : tAuthorizedUsersElements) {
                            tConfig.addAuthorizedUser(  AuxXml.getStringAttribute(tUserElement, "username", null),
                                                        AuxXml.getStringAttribute(tUserElement, "password", null));
                        }
                    }
                }

                List<Element> tMulticastGroups = AuxXml.findChildElements(tRoot, "MulticastGroups");
                for (Element mca : tMulticastGroups) {
                    tConfig.addMca(AuxXml.getStringAttribute(mca, "address", null),
                            AuxXml.getIntAttribute(mca, "port", 0),
                            AuxXml.getIntAttribute(mca, "groupId", 0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            tConfig.setDistributorAppName(AuxParseArguments.parse(args, "appName", "DistributorWebSocketGateway"));
            tConfig.setEthDevice(AuxParseArguments.parse(args, "ethDevice", null));
            tConfig.setLocalIpAddress(AuxParseArguments.parse(args, "localIpAddress", null));
            String tAddr = AuxParseArguments.parse(args, "mcaAddress", null);
            int tPort = AuxParseArguments.parseInt(args, "mcaPort", 0);
            if ((tAddr != null) || (tPort != 0)) {
                tConfig.addMca(tAddr, tPort, 1);
            }
        }
    }
}