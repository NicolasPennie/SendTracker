package sh.now.npennie.sendtracker.sendservice.providers;

import sh.now.npennie.sendtracker.sendservice.models.Send;

import java.util.List;

public interface SendProvider {
    List<Send> getAllSends();
}
