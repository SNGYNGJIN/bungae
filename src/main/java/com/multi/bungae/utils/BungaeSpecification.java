package com.multi.bungae.utils;

import com.multi.bungae.domain.Bungae;
import com.multi.bungae.domain.BungaeStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BungaeSpecification {
    public static Specification<Bungae> containsKeywordInNameDescriptionLocation(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(keyword)) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("bungaeName")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("bungaeDescription")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("bungaeLocation").get("keyword")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("bungaeLocation").get("address")), pattern)
                );
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Bungae> hasStatus(BungaeStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bungaeStatus"), status);
    }
}
