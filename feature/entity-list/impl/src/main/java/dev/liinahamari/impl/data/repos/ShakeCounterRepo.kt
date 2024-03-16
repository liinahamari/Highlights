package dev.liinahamari.impl.data.repos

import androidx.annotation.MainThread
import dev.liinahamari.api.domain.repo.ShakeCounterRepo
import javax.inject.Inject

class ShakeCounterRepoImpl @Inject constructor(): ShakeCounterRepo {
    @get:MainThread
    override var shaked = false
}
