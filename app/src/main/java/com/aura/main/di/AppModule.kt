package com.aura.main.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * The Hilt App Module who provide the retrofit instance.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Moshi instance
     * build with KotlinJsonAdapterFactory.
     *
     */
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    /**
     * Method to provide the Retrofit instance to use.
     *
     * @return a retrofit instance ready to use with the good URL and parser.
     */
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


}