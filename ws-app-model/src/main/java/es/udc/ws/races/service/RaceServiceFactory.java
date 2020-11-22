package es.udc.ws.races.service;

import es.udc.ws.races.model.util.configuration.ConfigurationParametersManager;

public class RaceServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "RaceServiceFactory.className";
    private static RaceService service = null;

    private RaceServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static RaceService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (RaceService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static RaceService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
