package services;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import jakarta.ws.rs.sse.SseBroadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Path("broadcast")
public class BroadcastingService {
    private Sse sse;
    static Logger logger = LoggerFactory.getLogger(BroadcastingService.class);

    public BroadcastingService(@Context final Sse sse) {
        this.sse = sse;
        BroadcasterEnumSingleton.INSTANCE.setSse(sse);
        logger.info(this.toString());
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String broadcastMessage(String message) {
        BroadcasterEnumSingleton.INSTANCE.publishEvent(message);
        logger.info(this.toString());
        return "Message '" + message + "' has been broadcast.";
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void listenToBroadcast(@Context SseEventSink eventSink) {
        BroadcasterEnumSingleton.INSTANCE.getBroadcaster().register(eventSink);
    }

    public enum BroadcasterEnumSingleton {

        INSTANCE;

        private SseBroadcaster info;
        private Sse sse;

        BroadcasterEnumSingleton(SseBroadcaster sseBroadcaster) {
            this.info = sseBroadcaster;
        }

        BroadcasterEnumSingleton() {

        }

        public BroadcasterEnumSingleton getInstance() {
            return INSTANCE;
        }

        public void setSse(Sse sse){
            this.sse = sse;
            this.info = sse.newBroadcaster();
        }

        public SseBroadcaster getBroadcaster(){
            return info;
        }

        public Sse getSse(){
            return this.sse;
        }

        public void publishEvent(String message){
            final OutboundSseEvent event = sse.newEventBuilder()
                    .name("message")
                    .mediaType(MediaType.TEXT_PLAIN_TYPE)
                    .data(String.class, message)
                    .build();

            BroadcasterEnumSingleton.INSTANCE.getBroadcaster().broadcast(event);
        }

    }
}
