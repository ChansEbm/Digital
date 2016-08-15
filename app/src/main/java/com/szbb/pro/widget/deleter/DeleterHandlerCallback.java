package com.szbb.pro.widget.deleter;

import java.util.List;
import java.util.Set;

/**
 * Created by KenChan on 16/5/27.
 */
public interface DeleterHandlerCallback {
    void success(Set<Integer> keySet, List<String> photoPaths);
}
