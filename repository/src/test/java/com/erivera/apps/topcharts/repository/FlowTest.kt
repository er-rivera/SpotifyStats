package com.erivera.apps.topcharts.repository

import com.erivera.apps.topcharts.repository.keyvalue.DataStore
import com.erivera.apps.topcharts.repository.models.api.ArtistResponse
import com.erivera.apps.topcharts.repository.models.api.ArtistsRetrofit
import com.erivera.apps.topcharts.repository.persistance.artist.Artist
import com.erivera.apps.topcharts.repository.persistance.helper.PersistenceMigrationHelperImpl
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class FlowTest {
    val dataStore: DataStore = mock(DataStore::class.java)

    val persistenceMigrationHelperImpl = PersistenceMigrationHelperImpl(dataStore)

    var shortArtistsFlow: Flow<List<ArtistsRetrofit>> = emptyFlow()

    var midArtistsFlow: Flow<List<ArtistsRetrofit>> = emptyFlow()

    var longArtistsFlow: Flow<List<ArtistsRetrofit>> = emptyFlow()

    @Before
    fun before() {
        val mapper = JsonResourceObjectMapper(ArtistResponse::class.java)

        val longTermArtists = mapper.getObjectFromFile("artist_longterm.json")
        val midTermArtists = mapper.getObjectFromFile("artist_midterm.json")
        val shortTermArtists = mapper.getObjectFromFile("artist_shortterm.json")
        shortArtistsFlow = flowOf(shortTermArtists.items.orEmpty())
        midArtistsFlow = flowOf(midTermArtists.items.orEmpty())
        longArtistsFlow = flowOf(longTermArtists.items.orEmpty())
    }

    @FlowPreview
    @Test
    fun combineTest() = runBlocking {
        persistenceMigrationHelperImpl.updatePersistenceTables(
            flowOf(),
            shortArtistsFlow,
            midArtistsFlow,
            longArtistsFlow
        )
        val firstItem = flow {
            emit("test1")
            emit("test2")
            emit("test3")
        }.first()
        assertThat(firstItem, IsEqual("test1"))
    }
}