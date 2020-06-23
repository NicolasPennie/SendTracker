package sendservice.providers;

import sendservice.models.Send;

import java.util.List;
import java.util.UUID;

public interface SendProvider {
    List<Send> getAllSends() throws Exception;

    UUID addSend(Send send) throws Exception;

    void editSend(UUID id, Send send) throws Exception;

    void deleteSend(UUID id) throws  Exception;
}
