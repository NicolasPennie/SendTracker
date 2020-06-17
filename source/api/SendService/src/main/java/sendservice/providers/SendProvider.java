package sendservice.providers;

import sendservice.models.Send;

import java.util.List;

public interface SendProvider {
    public List<Send> getAllSends() throws Exception;
}
