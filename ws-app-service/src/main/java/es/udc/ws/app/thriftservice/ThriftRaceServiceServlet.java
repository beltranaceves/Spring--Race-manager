package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftRaceService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

public class ThriftRaceServiceServlet extends TServlet {

    public ThriftRaceServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {

        return new ThriftRaceService.Processor<ThriftRaceService.Iface>(
                new ThriftRaceServiceImpl());

    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }

}
