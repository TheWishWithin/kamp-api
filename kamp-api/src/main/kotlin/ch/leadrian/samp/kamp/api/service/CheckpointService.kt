package ch.leadrian.samp.kamp.api.service

import ch.leadrian.samp.kamp.api.data.Sphere
import ch.leadrian.samp.kamp.api.data.Vector3D
import ch.leadrian.samp.kamp.api.entity.Checkpoint
import ch.leadrian.samp.kamp.api.entity.RaceCheckpoint

interface CheckpointService {

    fun createCheckpoint(coordinates: Vector3D, size: Vector3D): Checkpoint

    fun createCheckpoint(sphere: Sphere): RaceCheckpoint

}