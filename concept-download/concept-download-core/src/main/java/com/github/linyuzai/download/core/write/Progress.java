package com.github.linyuzai.download.core.write;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Progress {

    long total;

    long current;

    long increase;
}
