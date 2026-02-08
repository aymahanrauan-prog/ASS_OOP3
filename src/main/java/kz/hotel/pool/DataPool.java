package kz.hotel.pool;

import java.util.List;

public interface DataPool<T> {
    void refresh();
    List<T> all();
}
