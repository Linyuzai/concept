package com.github.linyuzai.concept.sample.domain;

import com.github.linyuzai.domain.core.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPO implements Identifiable {

    private String id;
}
