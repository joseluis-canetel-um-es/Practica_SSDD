package es.um.sisdist.backend.grpc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import es.um.sisdist.backend.grpc.GrpcServiceGrpc;
import es.um.sisdist.backend.grpc.PingRequest;
import es.um.sisdist.backend.grpc.PingResponse;
import es.um.sisdist.backend.grpc.impl.jscheme.JSchemeProvider;
import es.um.sisdist.backend.grpc.impl.jscheme.MapReduceApply;
import es.um.sisdist.backend.grpc.impl.jscheme.MapperApply;
import io.grpc.stub.StreamObserver;
import jscheme.JScheme;

class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase 
{
	private Logger logger;
	/**
	 * se llama al método js() de JSchemeProvider para obtener una 
	 * instancia de JScheme y se asigna a la variable js
	 */
    private JScheme js = JSchemeProvider.js(); 

	
    public GrpcServiceImpl(Logger logger) 
    {
		super();
		this.logger = logger;
	}

	@Override
	public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) 
	{
		logger.info("Recived PING request, value = " + request.getV());
		responseObserver.onNext(PingResponse.newBuilder().setV(request.getV()).build());
		responseObserver.onCompleted();
	}
	
	// Método que ejecuta el map/reduce
    public List<String> executeMapReduce(List<String> pares) {
        // la función de map y reduce
        String scheme_map_function = "(define (map-function key value)\n"
                + "  (cons key (list value)))";

        String scheme_reduce_function = "(define (reduce-function key values)\n"
                + "  (apply + values))";

        // Crear instancia de MapReduceApply
        MapReduceApply mapReduce = new MapReduceApply(js, scheme_map_function, scheme_reduce_function);

        // Ejecutar la función apply para cada entrada
        for (String par : pares) {
            String[] parts = par.split(":");
            String key = parts[0];
            String value = parts[1];
            mapReduce.apply(key, value);
        }

        // Ejecutar el map/reduce y obtener el resultado
        Map<Object, Object> resultMap = mapReduce.map_reduce();

        // Convertir el mapa en una lista de cadenas "k:v"
        List<String> resultList = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            String result = key + ":" + value;
            resultList.add(result);
        }

        return resultList;
    }
}