package es.udc.ws.races.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientRaceServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientRaceServiceFactory.className";
    private static Class<ClientRaceService> serviceClass = null;

    private ClientRaceServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientRaceService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientRaceService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientRaceService getService() {

        try {
            return (ClientRaceService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

}
