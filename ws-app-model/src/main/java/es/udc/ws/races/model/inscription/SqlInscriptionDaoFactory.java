package es.udc.ws.races.model.inscription;

import es.udc.ws.races.model.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlInscriptionDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlInscriptionDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlInscriptionDao</code>.</li> </ul>
 */
public class SqlInscriptionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlInscriptionDaoFactory.className";
    private static SqlInscriptionDao dao = null;

    private SqlInscriptionDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlInscriptionDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlInscriptionDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlInscriptionDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}