import java.util.*;

public class HealthRecordStore<T extends HealthProfile> {
    private List<T> records = new ArrayList<>();

    public void add(T profile) { records.add(profile); }
    public List<T> getAll()    { return Collections.unmodifiableList(records); }
    public int size()          { return records.size(); }
}
