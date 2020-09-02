package com.movies.sample.core.interactor

import com.movies.sample.AndroidTest
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Test

class UseCaseTest : AndroidTest() {

    private val TYPE_TEST = "Test"
    private val TYPE_PARAM = "ParamTest"

    private val useCase = MyUseCase()

    @Test
    fun `running use case should return 'Either' of use case type`() {
        val params = MyParams(TYPE_PARAM)
        val result = runBlocking { useCase.run(params) }

        result shouldEqual Result.Success(MyType(TYPE_TEST))
    }

    @Test
    fun `should return correct data when executing use case`() {
        var result: Result<MyType>? = null

        val params = MyParams("TestParam")
        val onResult = { myResult: Result<MyType> -> result = myResult }

        runBlocking { useCase(params, onResult) }

        result shouldEqual Result.Success(MyType(TYPE_TEST))
    }

    data class MyType(val name: String)
    data class MyParams(val name: String)

    private inner class MyUseCase : UseCase<MyType, MyParams>() {
        override suspend fun run(params: MyParams) = Result.Success(MyType(TYPE_TEST))
    }
}
