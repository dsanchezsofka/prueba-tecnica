package sofka.pruebatecnica.domain.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtils {

    public static <T,L> List<L> convertList(List<T> objecList, Function<T,L> tlFunction){
        return Objects.isNull(objecList) || objecList.isEmpty() ?Collections.emptyList() : objecList.stream()
        .map(tlFunction).collect(Collectors.toList());
    }

}
