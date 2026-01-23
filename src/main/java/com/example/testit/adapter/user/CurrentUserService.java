package com.example.testit.adapter.user;

import java.util.Optional;

public interface CurrentUserService {

    /**
     * Retourne l'id de l'utilsiateur connecte
     * @return
     */
    Optional<Long> getCurrentUserId();
}
