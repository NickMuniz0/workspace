package io.github.prefeituradorecife.jogospessoaidosa.Utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public final class PageSortingUtils {

    private PageSortingUtils() {
    }

    public static <T> Page<T> orderByName(Page<T> page, Pageable pageable, Function<T, String> nameExtractor) {
        if (page == null || page.getContent() == null || page.getContent().isEmpty()) {
            return page;
        }

        List<T> orderedContent = page.getContent().stream()
                .sorted(Comparator.comparing(
                        nameExtractor,
                        Comparator.nullsLast(String::compareToIgnoreCase)
                ))
                .toList();

        return new PageImpl<>(orderedContent, pageable, page.getTotalElements());
    }
}
