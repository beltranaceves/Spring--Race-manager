package es.udc.ws.races.model.race;


import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRaceDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlRaceDaoFactory.className";
    private static SqlRaceDao dao = null;

    private SqlRaceDaoFactory() {
    }

    private static SqlRaceDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRaceDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlRaceDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
