package sendservice.providers;

import sendservice.models.Send;

import java.util.List;

public interface SendProvider {
    List<Send> getAllSends() throws Exception;

    int addSend(Send send) throws Exception;

    void editSend(int id, Send send) throws Exception;

    void deleteSend(int id) throws  Exception;
}
