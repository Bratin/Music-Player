package com.bratin.musicplayer.data.mapper

interface BaseMapper<Dto, Entity> {
    fun mapToEntityFromDto(data: Dto): Entity
}