package com.sogoeslight.instructly.validation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.Year

class ValidYearTest {

    private val validator = ValidYearValidator()

    @ParameterizedTest
    @ValueSource(ints = [1, 1989, 1990, 159999])
    fun `should return validation error, if value is incorrect`(value: Int) {
        assertThat(validator.isValid(value = value, context = null)).isFalse
    }

    @ParameterizedTest
    @ValueSource(ints = [1991, 2000, 2021])
    fun `should return no error, if value is correct`(value: Int) {
        assertThat(validator.isValid(value = value, context = null)).isTrue
    }

    @Test
    fun `should return validation error, if value is current year + 1`() {
        assertThat(validator.isValid(value = Year.now().value + 1, context = null)).isFalse
    }

    @Test
    fun `should return no error, if value is current year`() {
        assertThat(validator.isValid(value = Year.now().value, context = null)).isTrue
    }
}